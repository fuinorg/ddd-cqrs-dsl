package org.fuin.dsl.cqrs.scoping

import com.google.inject.Singleton
import java.io.File
import java.util.List
import java.util.Map
import java.util.zip.ZipFile
import org.apache.log4j.Logger
import org.eclipse.emf.common.CommonPlugin
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet

/**
 * Turns a <code>dependency</code> into the URIs of the <code>.cqrs</code> models it provides.
 *
 * <p>A Maven dependency is resolved to its jar in the local repository by the
 * {@link CqrsArtifactResolver} of the environment, and the models are then addressed
 * <em>inside</em> that jar:</p>
 *
 * <pre>archive:file:/home/me/.m2/repository/.../cqrs-common-model-0.1.0-SNAPSHOT.jar!/model/types.cqrs</pre>
 *
 * <p>Nothing is ever unpacked. EMF resolves an <code>archive:</code> URI out of the box, the last
 * segment still ends in <code>.cqrs</code> so Xtext's resource factory applies as usual, and the local
 * Maven repository is the only cache there is. Only entries below {@link CqrsArtifactResolver#MODEL_DIR}
 * are considered, and they are taken recursively so a model may sit in a sub folder.</p>
 *
 * <p>A dependency with a <code>local</code> clause skips all of that and reads a directory of
 * <code>.cqrs</code> files directly, relative to the model that declares it.</p>
 *
 * <p>Resolution of an artifact is remembered for the session - success and failure alike - so a
 * coordinate that cannot be resolved is attempted once rather than on every validation run. The flip
 * side is that an artifact appearing later is picked up only after a restart.</p>
 */
@Singleton
class CqrsModelArchives {

	static val Logger LOG = Logger.getLogger(CqrsModelArchives)

	/** Model URIs of an artifact, keyed by {@link RemoteScopeEntry#getSourceId}. */
	val Map<String, List<URI>> resolved = newHashMap

	/** Why an artifact could not be resolved, keyed by {@link RemoteScopeEntry#getSourceId}. */
	val Map<String, String> problems = newHashMap

	/**
	 * The models the given dependency provides, or an empty list when it cannot be resolved (the
	 * caller degrades to "nothing extra is visible" and the validator reports the problem).
	 *
	 * @param rs Resource set, used to turn a platform URI into a file.
	 * @param modelUri URI of the model declaring the dependency - a <code>local</code> path is
	 *            relative to its directory.
	 * @param entry Dependency to resolve.
	 *
	 * @return Model URIs, never <code>null</code>.
	 */
	def List<URI> modelUris(ResourceSet rs, URI modelUri, RemoteScopeEntry entry) {
		if(entry === null) return #[]
		if(!entry.local.nullOrEmpty) return localModelUris(rs, modelUri, entry)
		return artifactModelUris(entry)
	}

	/**
	 * Why the given dependency cannot be resolved, or <code>null</code> when it resolves.
	 *
	 * @param rs Resource set.
	 * @param modelUri URI of the model declaring the dependency.
	 * @param entry Dependency to check.
	 *
	 * @return Problem description, or <code>null</code>.
	 */
	def String problem(ResourceSet rs, URI modelUri, RemoteScopeEntry entry) {
		if(entry === null) return null
		if (!entry.local.nullOrEmpty) {
			return if (localModelUris(rs, modelUri, entry).empty) {
				"the local directory '" + entry.local + "' does not exist or holds no '.cqrs' files"
			} else {
				null
			}
		}
		artifactModelUris(entry)
		return problems.get(entry.sourceId)
	}

	/** Forgets what was resolved, so the next call resolves again. */
	def void invalidate() {
		resolved.clear
		problems.clear
	}

	private def List<URI> artifactModelUris(RemoteScopeEntry entry) {
		val key = entry.sourceId
		if(key === null) return #[]
		if(resolved.containsKey(key)) return resolved.get(key)
		if(problems.containsKey(key)) return #[]

		try {
			val jar = CqrsArtifactResolvers.get.resolve(entry.groupId, entry.artifactId, entry.version)
			if (jar === null || !jar.toFile.isFile) {
				problems.put(key, "the artifact was not found in the local repository")
				return #[]
			}
			val uris = entriesOf(jar.toFile)
			if (uris.empty) {
				problems.put(key,
					"the artifact holds no '.cqrs' files below '" + CqrsArtifactResolver.MODEL_DIR + "/'")
				return #[]
			}
			resolved.put(key, uris)
			return uris
		} catch (Exception ex) {
			val message = message(ex)
			LOG.error("Could not resolve dependency '" + key + "': " + message, ex)
			problems.put(key, message)
			return #[]
		}
	}

	/** Every <code>.cqrs</code> below <code>model/</code>, as an <code>archive:</code> URI. */
	private def List<URI> entriesOf(File jar) {
		val prefix = CqrsArtifactResolver.MODEL_DIR + "/"
		val jarUri = URI.createFileURI(jar.absolutePath)
		val result = <URI>newArrayList
		val zip = new ZipFile(jar)
		try {
			val entries = zip.entries
			while (entries.hasMoreElements) {
				val entry = entries.nextElement
				val name = entry.name
				if (!entry.directory && name.startsWith(prefix) && name.endsWith(".cqrs")) {
					result.add(URI.createURI("archive:" + jarUri + "!/" + name))
				}
			}
		} finally {
			zip.close
		}
		result.sortInplaceBy[toString]
		return result
	}

	/** The <code>.cqrs</code> files of a <code>local</code> directory, relative to the declaring model. */
	private def List<URI> localModelUris(ResourceSet rs, URI modelUri, RemoteScopeEntry entry) {
		val candidate = new File(entry.local)
		val dir = if (candidate.absolute) {
				candidate
			} else {
				val base = toFile(rs, modelUri?.trimSegments(1))
				if(base === null) null else new File(base, entry.local)
			}
		if(dir === null || !dir.directory) return #[]

		val files = dir.listFiles
		if(files === null) return #[]
		val result = <URI>newArrayList
		for (f : files.sortBy[name]) {
			if(f.isFile && f.name.endsWith(".cqrs")) result.add(URI.createFileURI(f.absolutePath))
		}
		return result
	}

	private def File toFile(ResourceSet rs, URI uri) {
		if(uri === null || uri.segmentCount < 1) return null
		var resolved = rs.URIConverter.normalize(uri)
		if(resolved.isPlatformResource || resolved.isPlatformPlugin) resolved = CommonPlugin.resolve(resolved)
		return if(resolved.isFile) new File(resolved.toFileString) else null
	}

	/** The most telling message of a failure chain - the outermost one is often just a wrapper. */
	private def String message(Throwable ex) {
		var Throwable current = ex
		var String result = null
		while (current !== null) {
			if(!current.message.nullOrEmpty) result = current.message
			current = current.cause
		}
		return result ?: ex.class.simpleName
	}
}
