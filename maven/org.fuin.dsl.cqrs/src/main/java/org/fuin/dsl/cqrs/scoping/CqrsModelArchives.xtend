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
 * <p>A Maven dependency is resolved to its zip in the local repository by the
 * {@link CqrsArtifactResolver} of the environment, and the models are then addressed
 * <em>inside</em> that archive:</p>
 *
 * <pre>archive:file:/home/me/.m2/repository/.../cqrs-common-model-0.1.0-SNAPSHOT.zip!/model/public/types.cqrs</pre>
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

	/** Model URIs of an archive, keyed by its path - what a model inside it has beside itself. */
	val Map<String, List<URI>> siblings = newHashMap

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

	/**
	 * Whether the given URI addresses a model <em>inside</em> a dependency's archive rather than a
	 * file of the project. Such a model is read, not authored: it is opened read-only and nothing is
	 * reported in it.
	 *
	 * @param uri URI to test - may be <code>null</code>.
	 *
	 * @return TRUE if the model is read out of an archive.
	 */
	def static boolean isArchived(URI uri) {
		return uri !== null && "archive" == uri.scheme
	}

	/**
	 * The models beside the given one when it was itself read as a dependency: every
	 * <code>.cqrs</code> of the same archive, or of the same directory, the given one included.
	 *
	 * <p>A model of a dependency belongs to no project: it is in no index and in no scope, not even for
	 * its own neighbours. Those can therefore only be found the way the model itself was - through the
	 * archive or the directory it was read from, which exists already because the model came out of it.
	 * Without this a published model that spreads its types over several files resolves inside a
	 * headless run, where every model read happens to sit in one resource set, and not in an IDE.</p>
	 *
	 * <p>Only ask this for a model that really is read rather than authored here (see
	 * {@code CqrsDependencies}): for a file of the project the answer is the index's, and the directory
	 * a model happens to lie in means nothing.</p>
	 *
	 * @param uri URI of a model - may be <code>null</code>.
	 *
	 * @return Model URIs, never <code>null</code>.
	 */
	def List<URI> siblingModels(URI uri) {
		if (isArchived(uri)) {
			val archive = archiveOf(uri)
			if(archive === null || !archive.isFile) return #[]
			val key = archive.absolutePath
			if(siblings.containsKey(key)) return siblings.get(key)
			try {
				val entries = entriesOf(archive)
				siblings.put(key, entries)
				return entries
			} catch (Exception ex) {
				LOG.error("Could not read the archive '" + key + "': " + message(ex), ex)
				siblings.put(key, #[])
				return #[]
			}
		}
		// A 'local' directory: exactly the files a dependency declaring that directory reads. Listed
		// afresh every time, the same way the 'local' clause itself is - such a directory is somebody's
		// work in progress, and a model added to it has to show up without a restart.
		if(uri === null || !uri.isFile) return #[]
		val file = new File(uri.toFileString)
		return cqrsFilesOf(file.parentFile)
	}

	/** The zip an <code>archive:</code> URI addresses an entry of. */
	private def File archiveOf(URI uri) {
		val authority = uri.authority
		if(authority.nullOrEmpty) return null
		val nested = URI.createURI(
			if(authority.endsWith("!")) authority.substring(0, authority.length - 1) else authority)
		return if(nested.isFile) new File(nested.toFileString) else null
	}

	/** Forgets what was resolved, so the next call resolves again. */
	def void invalidate() {
		resolved.clear
		problems.clear
		siblings.clear
	}

	private def List<URI> artifactModelUris(RemoteScopeEntry entry) {
		val key = entry.sourceId
		if(key === null) return #[]
		if(resolved.containsKey(key)) return resolved.get(key)
		if(problems.containsKey(key)) return #[]

		try {
			val archive = CqrsArtifactResolvers.get.resolve(entry.groupId, entry.artifactId, entry.version)
			if (archive === null || !archive.toFile.isFile) {
				problems.put(key, "the artifact was not found in the local repository")
				return #[]
			}
			val uris = entriesOf(archive.toFile)
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
	private def List<URI> entriesOf(File archive) {
		val prefix = CqrsArtifactResolver.MODEL_DIR + "/"
		val archiveUri = URI.createFileURI(archive.absolutePath)
		val result = <URI>newArrayList
		val zip = new ZipFile(archive)
		try {
			val entries = zip.entries
			while (entries.hasMoreElements) {
				val entry = entries.nextElement
				val name = entry.name
				if (!entry.directory && name.startsWith(prefix) && name.endsWith(".cqrs")) {
					result.add(URI.createURI("archive:" + archiveUri + "!/" + name))
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
		return cqrsFilesOf(dir)
	}

	/**
	 * Every <code>.cqrs</code> directly in the given directory, by name.
	 *
	 * <p>The paths are normalized, so a directory reached as <code>../provider</code> from one model and
	 * by its own path from another yields one URI rather than two spellings of it. Two would make every
	 * element the directory declares a pair of elements sharing a qualified name - and an ambiguous name
	 * resolves to nothing.</p>
	 */
	private def List<URI> cqrsFilesOf(File dir) {
		if(dir === null || !dir.directory) return #[]
		val files = dir.listFiles
		if(files === null) return #[]
		val result = <URI>newArrayList
		for (f : files.sortBy[name]) {
			if (f.isFile && f.name.endsWith(".cqrs")) {
				result.add(URI.createFileURI(f.toPath.normalize.toString))
			}
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
