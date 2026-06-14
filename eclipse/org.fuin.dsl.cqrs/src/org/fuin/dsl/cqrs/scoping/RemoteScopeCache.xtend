package org.fuin.dsl.cqrs.scoping

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.inject.Singleton
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.List
import java.util.Map
import org.eclipse.emf.common.CommonPlugin
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtend.lib.annotations.Data

/**
 * Materializes remote <code>.cqrs</code> models declared in the <code>dependencies.json</code>
 * catalog and caches them on disk under a <code>.dependencies-cache</code> directory placed next to
 * the catalog.
 *
 * <p>Each Maven artifact is cached once in its own sub-directory
 * <code>&lt;artifactId&gt;-&lt;version&gt;-&lt;sha1(gav)&gt;</code> holding every <code>.cqrs</code>
 * file unpacked from the artifact's <code>tar.gz</code>. Keying the cache by the Maven coordinate
 * (rather than by namespace) means an artifact that provides several namespaces is downloaded and
 * unpacked only once and shared by all of them. The cache index
 * (<code>.dependencies-cache/index.json</code>) is read into memory the first time a cache directory
 * is seen and held for the session, so subsequent lookups serve files from disk without any network
 * access. {@code maven} artifacts (including SNAPSHOTs) are treated as up to date once cached &mdash;
 * delete the cache directory or bump the version to force a refresh.</p>
 *
 * <p>When an entry declares a <code>local</code> directory the <code>.cqrs</code> files are read
 * straight from that folder (resolved relative to the catalog when not absolute); nothing is
 * downloaded or cached.</p>
 */
@Singleton
class RemoteScopeCache {

	static val String CACHE_DIR_NAME = ".dependencies-cache"
	static val String INDEX_FILE_NAME = "index.json"

	/** In-memory cache index per cache directory: key {@code source} (the Maven GAV) &rarr; entry. */
	val Map<File, Map<String, CacheEntry>> indexByDir = newHashMap

	/**
	 * Returns the local URIs of the <code>.cqrs</code> models that provide the given namespace,
	 * downloading and caching the Maven artifact on a miss (or reading a {@code local} directory
	 * directly), or an empty list when nothing is configured (so the caller falls back to the
	 * standard mechanism).
	 */
	def List<URI> getCachedModelUris(ResourceSet rs, URI modelUri, String namespace, RemoteScopeCatalog catalog) {
		val entry = catalog.lookupEntry(rs, modelUri, namespace)
		if(entry === null) return #[]
		val root = catalog.rootDir(rs, modelUri)
		if(root === null) return #[]

		// A local directory overrides the artifact: read its models directly, no download or cache.
		if(!entry.local.nullOrEmpty) return localModelUris(rs, root, entry.local)

		val cacheDir = toFile(rs, root.appendSegment(CACHE_DIR_NAME))
		if(cacheDir === null) return #[]
		val source = entry.sourceId
		if(source.nullOrEmpty) return #[]

		val dirName = entry.artifactId + "-" + entry.version + "-" + sha1(source)
		val targetDir = new File(cacheDir, dirName)
		val index = indexFor(cacheDir)
		val existing = index.get(source)
		if (existing !== null && existing.source == source && targetDir.directory) {
			val cached = cqrsFiles(targetDir)
			if(!cached.empty) return cached
		}

		materialize(entry, targetDir)
		index.put(source, new CacheEntry(source, dirName))
		persist(cacheDir, index)
		return cqrsFiles(targetDir)
	}

	/** Reads the <code>.cqrs</code> files of a {@code local} directory (relative to the catalog root). */
	private def List<URI> localModelUris(ResourceSet rs, URI root, String local) {
		val candidate = new File(local)
		val dir = if(candidate.absolute) {
				candidate
			} else {
				val rootFile = toFile(rs, root)
				if(rootFile === null) null else new File(rootFile, local)
			}
		return if(dir !== null && dir.directory) cqrsFiles(dir) else #[]
	}

	/** Downloads and unpacks the artifact's model(s) into <code>targetDir</code>, replacing stale content. */
	private def void materialize(RemoteScopeEntry entry, File targetDir) {
		cleanDir(targetDir)
		targetDir.mkdirs
		val stream = new MavenArtifactResolver().openArtifact(entry.groupId, entry.artifactId, entry.version)
		try {
			TarGz.extractCqrsFiles(stream, targetDir)
		} finally {
			stream.close
		}
	}

	/** All <code>.cqrs</code> files in <code>dir</code> as file URIs, sorted by name for stable order. */
	private def List<URI> cqrsFiles(File dir) {
		val files = dir.listFiles
		if(files === null) return #[]
		val result = <URI>newArrayList
		for (f : files.sortBy[name]) {
			if(f.isFile && f.name.endsWith(".cqrs")) result.add(URI.createFileURI(f.absolutePath))
		}
		return result
	}

	private def void cleanDir(File dir) {
		val files = dir.listFiles
		if(files !== null) for (f : files) f.delete
	}

	private def Map<String, CacheEntry> indexFor(File cacheDir) {
		indexByDir.computeIfAbsent(cacheDir)[loadIndex(it)]
	}

	private def Map<String, CacheEntry> loadIndex(File cacheDir) {
		val map = <String, CacheEntry>newLinkedHashMap
		val indexFile = new File(cacheDir, INDEX_FILE_NAME)
		if (indexFile.exists) {
			val reader = new InputStreamReader(new FileInputStream(indexFile), StandardCharsets.UTF_8)
			try {
				val entries = JsonParser.parseReader(reader).asJsonObject.getAsJsonArray("entries")
				if (entries !== null) {
					for (element : entries) {
						val obj = element.asJsonObject
						if (obj.has("source") && obj.has("dir")) {
							val entry = new CacheEntry(obj.get("source").asString, obj.get("dir").asString)
							map.put(entry.source, entry)
						}
					}
				}
			} finally {
				reader.close
			}
		}
		return map
	}

	private def void persist(File cacheDir, Map<String, CacheEntry> index) {
		cacheDir.mkdirs
		val array = new JsonArray
		for (entry : index.values) {
			val obj = new JsonObject
			obj.addProperty("source", entry.source)
			obj.addProperty("dir", entry.dir)
			array.add(obj)
		}
		val root = new JsonObject
		root.add("entries", array)
		val writer = new OutputStreamWriter(new FileOutputStream(new File(cacheDir, INDEX_FILE_NAME)),
			StandardCharsets.UTF_8)
		try {
			new GsonBuilder().setPrettyPrinting.create.toJson(root, writer)
		} finally {
			writer.close
		}
	}

	private def File toFile(ResourceSet rs, URI uri) {
		var resolved = rs.URIConverter.normalize(uri)
		if(resolved.isPlatformResource || resolved.isPlatformPlugin) resolved = CommonPlugin.resolve(resolved)
		return if(resolved.isFile) new File(resolved.toFileString) else null
	}

	private def String sha1(String value) {
		val bytes = MessageDigest.getInstance("SHA-1").digest(value.getBytes(StandardCharsets.UTF_8))
		val sb = new StringBuilder
		for (b : bytes) sb.append(String.format("%02x", b))
		return sb.toString
	}

	@Data
	static class CacheEntry {
		String source
		String dir
	}
}
