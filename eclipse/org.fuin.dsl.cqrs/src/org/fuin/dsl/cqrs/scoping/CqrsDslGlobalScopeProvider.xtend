package org.fuin.dsl.cqrs.scoping

import com.google.common.base.Predicate
import com.google.inject.Inject
import org.apache.log4j.Logger
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.Scopes
import org.eclipse.xtext.scoping.impl.DefaultGlobalScopeProvider

/**
 * Global scope provider that makes the elements of a declared <code>dependency</code> resolvable.
 *
 * <p>Every <code>dependency "groupId:artifactId:version"</code> of the model - those of the project
 * (including same named project blocks in sibling files) and those of its contexts - is resolved by
 * {@link CqrsDependencies} to the <code>.cqrs</code> files the artifact provides. Artifacts are
 * downloaded once and cached on disk by the {@link RemoteScopeCache}; their objects are then exposed
 * by fully qualified name on top of the standard global scope. A model without dependencies adds
 * nothing, so resolution falls back to the normal file based mechanism. Any failure (offline and
 * uncached, malformed coordinate, parse error) is logged and degrades gracefully to the local scope
 * so editing and generation never break.</p>
 */
class CqrsDslGlobalScopeProvider extends DefaultGlobalScopeProvider {

	static val Logger LOG = Logger.getLogger(CqrsDslGlobalScopeProvider)

	@Inject IQualifiedNameProvider qualifiedNameProvider
	@Inject CqrsDependencies dependencies

	override protected IScope getScope(Resource resource, boolean ignoreCase, EClass type,
		Predicate<IEObjectDescription> filter) {
		val parent = super.getScope(resource, ignoreCase, type, filter)
		val rs = resource.resourceSet
		if(rs === null) return parent
		try {
			val remoteUris = dependencies.modelUris(resource)
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
			LOG.error("Dependency scope resolution failed; using local scope only: " + ex.message, ex)
			return parent
		}
	}
}
