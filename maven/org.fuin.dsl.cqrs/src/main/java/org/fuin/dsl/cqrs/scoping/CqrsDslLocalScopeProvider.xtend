package org.fuin.dsl.cqrs.scoping

import com.google.inject.Inject
import java.util.List
import org.apache.log4j.Logger
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.resource.IContainer
import org.eclipse.xtext.resource.IResourceDescriptions
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider
import org.eclipse.xtext.scoping.impl.ImportNormalizer
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider
import org.fuin.dsl.cqrs.cqrsDsl.Context
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Import
import org.fuin.dsl.cqrs.cqrsDsl.Module

/**
 * Turns the declared <code>import</code> statements into the resolvers that decide which names are
 * visible under their simple name.
 *
 * <p>A <code>module</code> is the unit of visibility. Without any import only the module's own
 * elements can be addressed by their simple name - everything else, including a sibling module of
 * the very same context, has to be imported:</p>
 *
 * <ul>
 * <li>inside a <code>module</code> &mdash; that module itself, so a module split across several
 * files still sees all of its own elements, plus everything the module imports;</li>
 * <li>inside a <code>context</code> &mdash; everything the context imports, which every module of
 * that context therefore inherits;</li>
 * <li>at the model root &mdash; nothing. A <code>dependency</code> only makes the models of another
 * project <em>resolvable</em>; an <code>import</code> decides what of them is visible.</li>
 * </ul>
 *
 * <p>Because {@code ImportedNamespaceAwareLocalScopeProvider} applies these per container from the
 * outside in, the innermost block wins: an import of the module shadows a same named one the
 * context contributes. A name that really is ambiguous at one level resolves to nothing rather than
 * to an arbitrary pick, and has to be qualified. A fully qualified reference always resolves through
 * the global scope, with or without a matching import.</p>
 *
 * <p>An import may end in a wildcard on any level - <code>ctx.*</code>, <code>ctx.mod.*</code> or the
 * single type <code>ctx.mod.Type</code>. A wildcard {@link ImportNormalizer} maps exactly <em>one</em>
 * segment and does not recurse, and both a context and a module name may themselves be dotted, so a
 * wildcard import cannot simply be handed over as it is written: besides the literal prefix, one
 * resolver per module whose qualified name starts with that prefix is added. Those modules are read
 * from the Xtext index rather than from the resource set, so the result is complete in the Eclipse
 * editor (builder state, live shadowed for dirty editors) as well as in a headless SrcGen4J run
 * (where the index is backed by the resource set). Note that the result is memoized per resource, so
 * adding a module in one file may need the referencing file to be re-parsed before it is seen.</p>
 */
class CqrsDslLocalScopeProvider extends ImportedNamespaceAwareLocalScopeProvider {

	static val Logger LOG = Logger.getLogger(CqrsDslLocalScopeProvider)

	@Inject IQualifiedNameProvider qualifiedNameProvider

	@Inject ResourceDescriptionsProvider resourceDescriptionsProvider

	@Inject IContainer.Manager containerManager

	override protected List<ImportNormalizer> internalGetImportedNamespaceResolvers(EObject obj,
		boolean ignoreCase) {
		try {
			switch obj {
				DomainModel: return emptyList
				Context: return resolvers(obj.imports, obj, ignoreCase)
				Module: {
					val result = <ImportNormalizer>newArrayList
					val own = qualifiedNameProvider.getFullyQualifiedName(obj)
					if(own !== null) result.add(doCreateImportNormalizer(own, true, ignoreCase))
					result.addAll(resolvers(obj.imports, obj, ignoreCase))
					return result
				}
			}
		} catch (Exception ex) {
			// Never let scoping fail: degrade to "nothing extra is visible" instead of breaking the editor.
			LOG.error("Could not compute imports for " + obj + ": " + ex.message, ex)
		}
		return emptyList
	}

	/** Turns every declared import of a block into its resolvers. */
	private def List<ImportNormalizer> resolvers(Iterable<Import> imports, EObject context,
		boolean ignoreCase) {
		val result = <ImportNormalizer>newArrayList
		for (imp : imports) {
			result.addAll(resolvers(imp.importedNamespace, context, ignoreCase))
		}
		return result
	}

	/**
	 * The resolvers a single imported name contributes. The inherited
	 * {@link #createImportedNamespaceResolver(String, boolean)} already turns the written name into
	 * the one normalizer Xtext would use - a plain one for <code>ctx.mod.Type</code>, a wildcard one
	 * for the prefix of <code>ctx.mod.*</code>. Only the wildcard case needs more, because such a
	 * normalizer covers a single segment: one additional resolver per module below the prefix.
	 */
	private def List<ImportNormalizer> resolvers(String importedNamespace, EObject context,
		boolean ignoreCase) {
		val result = <ImportNormalizer>newArrayList
		if(importedNamespace.nullOrEmpty) return result

		val normalizer = createImportedNamespaceResolver(importedNamespace, ignoreCase)
		if(normalizer === null) return result
		result.add(normalizer)

		if (normalizer.hasWildCard) {
			for (module : modulesBelow(normalizer.importedNamespacePrefix, context)) {
				result.add(doCreateImportNormalizer(module, true, ignoreCase))
			}
		}
		return result
	}

	/**
	 * Every module in the index whose qualified name starts with (but is not equal to) the given
	 * prefix. This is what makes <code>context.*</code> reach the types of a module, and what covers
	 * a module name that is itself dotted.
	 */
	private def Iterable<QualifiedName> modulesBelow(QualifiedName prefix, EObject context) {
		val result = <QualifiedName>newLinkedHashSet

		val resource = context.eResource
		val resourceSet = resource?.resourceSet
		if(resourceSet === null) return result

		val IResourceDescriptions descriptions = resourceDescriptionsProvider.getResourceDescriptions(resourceSet)
		val self = descriptions.getResourceDescription(resource.URI)
		if(self === null) return result

		for (container : containerManager.getVisibleContainers(self, descriptions)) {
			for (description : container.getExportedObjectsByType(CqrsDslPackage.Literals.MODULE)) {
				val name = description.name
				if(name !== null && name != prefix && name.startsWith(prefix)) result.add(name)
			}
		}
		return result
	}
}
