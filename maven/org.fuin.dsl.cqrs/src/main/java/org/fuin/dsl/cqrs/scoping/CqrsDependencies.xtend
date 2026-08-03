package org.fuin.dsl.cqrs.scoping

import com.google.inject.Inject
import com.google.inject.Singleton
import java.util.ArrayList
import java.util.Collection
import java.util.List
import java.util.Map
import java.util.Set
import org.apache.log4j.Logger
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.resource.IContainer
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage
import org.fuin.dsl.cqrs.cqrsDsl.Dependency
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Module

/**
 * Resolves the <code>dependency</code> declarations of a model to the <code>.cqrs</code> files they
 * provide.
 *
 * <p>A dependency may be declared on a <code>context</code> - where it applies to every module of
 * that context - or on a single <code>module</code>. Because a context block may be split across
 * several files (all blocks with the same name denote the same logical context), the context level
 * dependencies are collected from <em>every</em> same-named context block in the resource set, not
 * only from the block the resource happens to contain. This mirrors how the generator finds a
 * context's "SrcGen4J" hint.</p>
 *
 * <p>This class is shared by {@link CqrsDslGlobalScopeProvider} (which loads the models) and
 * {@link CqrsDslLocalScopeProvider} (which derives the visible modules from them), so
 * the two can never disagree about what a model depends on.</p>
 */
@Singleton
class CqrsDependencies {

	static val Logger LOG = Logger.getLogger(CqrsDependencies)

	@Inject CqrsModelArchives archives
	@Inject IQualifiedNameProvider qualifiedNameProvider
	@Inject ResourceDescriptionsProvider resourceDescriptionsProvider
	@Inject IContainer.Manager containerManager

	/**
	 * All dependencies that apply to the given resource: those of every context block sharing a name
	 * with a context declared here, plus those of the modules declared here. Duplicates (the same
	 * coordinate declared more than once) are collapsed so an artifact is resolved only once.
	 */
	def Collection<Dependency> declared(Resource resource) {
		val result = <String, Dependency>newLinkedHashMap
		val rs = resource.resourceSet
		if(rs === null) return result.values

		val contextNames = <String>newLinkedHashSet
		for (model : resource.contents.filter(DomainModel)) {
			for (context : model.contexts) {
				if(context.name !== null) contextNames.add(context.name)
				for (dependency : context.dependencies) {
					put(result, dependency)
				}
				for (module : context.modules) {
					for (dependency : module.dependencies) {
						put(result, dependency)
					}
				}
			}
		}
		if(contextNames.empty) return result.values

		// The other halves of this context have to be in the resource set before they can be read,
		// and in an IDE they are not: an Xtext editor's resource set holds the one file being edited
		// and finds everything else through the index. A 'dependency' declared on the context in
		// another file would then be invisible - and with it every type the artifact provides.
		loadContextResources(resource, contextNames)

		// A context may be split across files: pick up the context level dependencies declared in
		// the sibling resources too. Iterate a copy - resolving a dependency adds resources to the
		// very set being iterated.
		for (other : new ArrayList(rs.resources)) {
			if (other !== resource && other.isLoaded) {
				for (model : other.contents.filter(DomainModel)) {
					for (context : model.contexts) {
						if (contextNames.contains(context.name)) {
							for (dependency : context.dependencies) {
								put(result, dependency)
							}
						}
					}
				}
			}
		}
		return result.values
	}

	/**
	 * Loads every file that declares a context of one of the given names, so the walk over the
	 * resource set below sees all halves of the context.
	 *
	 * <p>Which files those are is asked of the Xtext index, because that is the pool which is complete
	 * in an IDE - the resource set is not. A <code>dependency</code> is not in the index itself (it has
	 * no name, so nothing exports it), so the files still have to be read; the index only says
	 * <em>which</em>. Headless this finds what the resource set already holds and changes nothing.</p>
	 */
	private def void loadContextResources(Resource resource, Set<String> contextNames) {
		val rs = resource.resourceSet
		try {
			val descriptions = resourceDescriptionsProvider.getResourceDescriptions(rs)
			val self = descriptions.getResourceDescription(resource.URI)
			if(self === null) return;
			for (container : containerManager.getVisibleContainers(self, descriptions)) {
				for (description : container.getExportedObjectsByType(CqrsDslPackage.Literals.CONTEXT)) {
					val name = description.name
					if (name !== null && contextNames.contains(name.toString)) {
						load(rs, description.EObjectURI.trimFragment, resource.URI)
					}
				}
			}
		} catch (Exception ex) {
			LOG.error("Could not look up the context blocks of '" + resource.URI + "': " + ex.message, ex)
		}
	}

	/**
	 * Whether the given model is one this project <em>reads</em> rather than one it authors - the
	 * question being who is responsible for it, not where it lies.
	 *
	 * <p>The index is what answers that: it holds the files of the project and nothing else. A model
	 * inside an artifact is never in it; a model of a <code>local</code> directory outside the project
	 * is not either, while one inside the project is. Headless, where the index is the resource set,
	 * every model read is in it - correctly, because there they all share one resource set and already
	 * see each other.</p>
	 */
	private def boolean readNotAuthored(Resource resource) {
		if(CqrsModelArchives.isArchived(resource.URI)) return true
		val rs = resource.resourceSet
		if(rs === null) return false
		try {
			val descriptions = resourceDescriptionsProvider.getResourceDescriptions(rs)
			return descriptions.getResourceDescription(resource.URI) === null
		} catch (Exception ex) {
			LOG.error("Could not tell whether '" + resource.URI + "' is indexed: " + ex.message, ex)
			return false
		}
	}

	/** Pulls one file into the resource set. A file that cannot be read costs only itself. */
	private def void load(ResourceSet rs, URI uri, URI self) {
		if(uri === null || uri == self) return;
		try {
			rs.getResource(uri, true)
		} catch (Exception ex) {
			LOG.error("Could not read the model '" + uri + "': " + ex.message, ex)
		}
	}

	private def void put(Map<String, Dependency> target, Dependency dependency) {
		val key = key(dependency)
		if(key !== null && !target.containsKey(key)) target.put(key, dependency)
	}

	/** Identity of a dependency: its coordinate plus any local override. */
	private def String key(Dependency dependency) {
		if(dependency?.coordinate === null) return null
		return dependency.coordinate + "|" + (dependency.local ?: "")
	}

	/**
	 * URIs of every <code>.cqrs</code> model the resource's dependencies provide - entries inside the
	 * artifact's zip in the local repository, or files of a <code>local</code> directory. A malformed
	 * coordinate contributes nothing (the validator reports it); a failing dependency is skipped so
	 * the rest still resolve.
	 */
	def List<URI> modelUris(Resource resource) {
		val result = <URI>newLinkedHashSet
		// A model that was itself read as a dependency has to reach the models beside it. It is in no
		// index and in no scope, so a published model split over several files - the very split a model
		// that publishes only part of itself makes - would resolve nowhere but in a headless run, where
		// every model read happens to share one resource set.
		if(readNotAuthored(resource)) result.addAll(archives.siblingModels(resource.URI))
		for (dependency : declared(resource)) {
			val entry = RemoteScopeEntry.parse(dependency.coordinate, dependency.local)
			if (entry === null) {
				LOG.warn("Ignoring malformed dependency coordinate: " + dependency.coordinate)
			} else {
				try {
					result.addAll(archives.modelUris(resource.resourceSet, declaringUri(dependency, resource), entry))
				} catch (Exception ex) {
					LOG.error("Could not resolve dependency '" + dependency.coordinate + "': " + ex.message, ex)
				}
			}
		}
		return new ArrayList(result)
	}

	/**
	 * Why the given dependency cannot be resolved, or <code>null</code> when it resolves. A malformed
	 * coordinate reports nothing here - {@code checkDependencyCoordinate} already covers it - and
	 * neither does a model without a usable location on disk, where resolution is never attempted.
	 */
	def String resolutionProblem(Dependency dependency) {
		val resource = dependency?.eResource
		if(resource === null || resource.resourceSet === null) return null
		val entry = RemoteScopeEntry.parse(dependency.coordinate, dependency.local)
		if(entry === null) return null
		try {
			return archives.problem(resource.resourceSet, declaringUri(dependency, resource), entry)
		} catch (Exception ex) {
			LOG.error("Could not check dependency '" + dependency.coordinate + "': " + ex.message, ex)
			return null
		}
	}

	/**
	 * Everything the resource's dependencies provide, by fully qualified name - contexts, modules and
	 * the elements below them, exactly as the Xtext index would hold them if it knew these models.
	 *
	 * <p>This is the pool that lets validation and content assist answer from the same set the scope
	 * resolves against. They cannot read it from the index in an IDE: there the index is the JDT
	 * builder state, which knows workspace files and never a model read out of an artifact's zip.
	 * The models are already loaded by then - {@link CqrsDslGlobalScopeProvider} loads exactly these
	 * URIs - so walking them again costs nothing but the walk. The first name wins when two
	 * dependencies provide the same one, which is what the scope does too.</p>
	 */
	def Map<QualifiedName, EObject> providedElements(Resource resource) {
		val result = <QualifiedName, EObject>newLinkedHashMap
		val rs = resource.resourceSet
		if(rs === null) return result
		for (uri : modelUris(resource)) {
			try {
				val contents = rs.getResource(uri, true).allContents
				while (contents.hasNext) {
					val obj = contents.next
					val name = qualifiedNameProvider.getFullyQualifiedName(obj)
					if(name !== null && !result.containsKey(name)) result.put(name, obj)
				}
			} catch (Exception ex) {
				LOG.error("Could not read dependency model '" + uri + "': " + ex.message, ex)
			}
		}
		return result
	}

	/** The fully qualified names of {@link #providedElements}. */
	def Iterable<QualifiedName> providedNames(Resource resource) {
		return providedElements(resource).keySet
	}

	/**
	 * The <code>context.module</code> names of the modules a dependency provides. These are the ones a
	 * wildcard import has to be expanded over, the same way {@code CqrsDslLocalScopeProvider} expands
	 * it over the modules of the index.
	 */
	def Iterable<QualifiedName> providedModules(Resource resource) {
		val result = <QualifiedName>newLinkedHashSet
		for (entry : providedElements(resource).entrySet) {
			if(entry.value instanceof Module) result.add(entry.key)
		}
		return result
	}

	/** URI a 'local' directory is resolved against: the file that declares the dependency. */
	private def URI declaringUri(Dependency dependency, Resource fallback) {
		return dependency.eResource?.URI ?: fallback.URI
	}
}
