package org.fuin.dsl.cqrs.scoping

import com.google.common.base.Predicate
import com.google.inject.Inject
import org.apache.log4j.Logger
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.DefaultGlobalScopeProvider
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel

/**
 * Global scope provider that makes elements of remote (HTTP-only) <code>.cqrs</code> models
 * resolvable.
 *
 * <p>For every <code>import</code> in the model it consults the {@link RemoteScopeCatalog} using the
 * (enclosing context name, imported namespace) coordinate. Configured coordinates are downloaded
 * once and cached on disk by the {@link RemoteScopeCache}; their objects are then exposed by fully
 * qualified name on top of the standard global scope. Coordinates that are not configured add
 * nothing, so resolution falls back to the normal file-based mechanism. Any failure (missing
 * catalog, offline and uncached, parse error) is logged and degrades gracefully to the local scope
 * so editing and generation never break.</p>
 */
class CqrsDslGlobalScopeProvider extends DefaultGlobalScopeProvider {

	static val Logger LOG = Logger.getLogger(CqrsDslGlobalScopeProvider)

	@Inject IQualifiedNameProvider qualifiedNameProvider
	@Inject RemoteScopeCatalog catalog
	@Inject RemoteScopeCache cache

	override protected IScope getScope(Resource resource, boolean ignoreCase, EClass type,
		Predicate<IEObjectDescription> filter) {
		val parent = super.getScope(resource, ignoreCase, type, filter)
		val rs = resource.resourceSet
		if(rs === null) return parent
		try {
			val remoteUris = <URI>newLinkedHashSet
			for (model : resource.contents.filter(DomainModel)) {
				for (project : model.projects) {
					for (context : project.contexts) {
						// Imports may be declared inside a namespace or - when the namespace is
						// omitted - directly on the context itself. Collect both.
						for (^import : context.imports) {
							remoteUris.addAll(cache.getCachedModelUris(rs, resource.URI,
								^import.importedNamespace, catalog))
						}
						for (namespace : context.namespaces) {
							for (^import : namespace.imports) {
								remoteUris.addAll(cache.getCachedModelUris(rs, resource.URI,
									^import.importedNamespace, catalog))
							}
						}
					}
				}
			}
			if(remoteUris.empty) return parent

			val objects = <EObject>newArrayList
			for (uri : remoteUris) {
				val contents = rs.getResource(uri, true).allContents
				while (contents.hasNext) {
					val object = contents.next
					if(type.isInstance(object)) objects.add(object)
				}
			}
			return Scopes.scopeFor(objects, [qualifiedNameProvider.getFullyQualifiedName(it)], parent)
		} catch (Exception ex) {
			LOG.error("Remote scope resolution failed; using local scope only: " + ex.message, ex)
			return parent
		}
	}
}
