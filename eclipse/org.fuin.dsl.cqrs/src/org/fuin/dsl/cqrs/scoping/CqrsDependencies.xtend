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
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.fuin.dsl.cqrs.cqrsDsl.Dependency
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel

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
	 * The <code>context.module</code> names the dependency models declare. These are the scopes a
	 * <code>dependency</code> makes implicitly visible.
	 */
	def Iterable<QualifiedName> providedScopes(Resource resource) {
		val result = <QualifiedName>newLinkedHashSet
		val rs = resource.resourceSet
		if(rs === null) return result
		for (uri : modelUris(resource)) {
			try {
				val remote = rs.getResource(uri, true)
				for (model : remote.contents.filter(DomainModel)) {
					for (context : model.contexts) {
						add(result, context)
						for (module : context.modules) {
							add(result, module)
						}
					}
				}
			} catch (Exception ex) {
				LOG.error("Could not read dependency model '" + uri + "': " + ex.message, ex)
			}
		}
		return result
	}

	private def void add(Set<QualifiedName> target, EObject obj) {
		val name = qualifiedNameProvider.getFullyQualifiedName(obj)
		if(name !== null) target.add(name)
	}

	/** URI a 'local' directory is resolved against: the file that declares the dependency. */
	private def URI declaringUri(Dependency dependency, Resource fallback) {
		return dependency.eResource?.URI ?: fallback.URI
	}
}
