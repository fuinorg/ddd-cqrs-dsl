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
import java.nio.file.Files
import java.nio.file.StandardCopyOption
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
 * <p>Each resolved namespace is cached in its own sub-directory that holds one or more
 * <code>.cqrs</code> files: a {@code simple} source contributes the single downloaded file under
 * <code>&lt;namespace&gt;-&lt;sha1(source)&gt;</code>, a {@code maven} source contributes every
 * <code>.cqrs</code> unpacked from the artifact's <code>tar.gz</code> under
 * <code>&lt;namespace&gt;-&lt;version&gt;-&lt;sha1(source)&gt;</code> (the version is part of the name
 * so different versions of the same artifact stay distinct on disk). The cache index
 * (<code>.dependencies-cache/index.json</code>) is
 * read into memory the first time a cache directory is seen and held for the session, so subsequent
 * lookups serve files from disk without any network access.</p>
 *
 * <p>A <code>file:</code> based {@code simple} source is refreshed when its source file changes;
 * <code>http(s):</code> sources and {@code maven} artifacts (including SNAPSHOTs) are treated as up to
 * date once cached &mdash; delete the cache directory or bump the version to force a refresh.</p>
 */
@Singleton
class RemoteScopeCache {

	static val String CACHE_DIR_NAME = ".dependencies-cache"
	static val String INDEX_FILE_NAME = "index.json"

	/** File name of the single model cached for a {@code simple} source. */
	static val String SIMPLE_FILE_NAME = "model.cqrs"

	/** In-memory cache index per cache directory: key {@code namespace} &rarr; entry. */
	val Map<File, Map<String, CacheEntry>> indexByDir = newHashMap

	/**
	 * Returns the local cache URIs of the <code>.cqrs</code> models that provide the given namespace,
	 * downloading and caching them on a miss, or an empty list when nothing is configured (so the
	 * caller falls back to the standard mechanism).
	 */
	def List<URI> getCachedModelUris(ResourceSet rs, URI modelUri, String namespace, RemoteScopeCatalog catalog) {
		val root = catalog.rootDir(rs, modelUri)
		if(root === null) return #[]
		val cacheDir = toFile(rs, root.appendSegment(CACHE_DIR_NAME))
		if(cacheDir === null) return #[]

		val ns = RemoteScopeCatalog.stripWildcard(namespace)
		val entry = catalog.lookupEntry(rs, modelUri, namespace)
		if(entry === null) return #[]
		val source = entry.sourceId
		if(source.nullOrEmpty) return #[]

		val dirName = if(entry.type == RemoteScopeEntry.TYPE_MAVEN)
				ns + "-" + entry.version + "-" + sha1(source)
			else
				ns + "-" + sha1(source)
		val targetDir = new File(cacheDir, dirName)
		val index = indexFor(cacheDir)
		val existing = index.get(ns)
		if (existing !== null && existing.source == source && targetDir.directory && upToDate(entry, targetDir)) {
			val cached = cqrsFiles(targetDir)
			if(!cached.empty) return cached
		}

		materialize(entry, targetDir)
		index.put(ns, new CacheEntry(ns, source, dirName))
		persist(cacheDir, index)
		return cqrsFiles(targetDir)
	}

	/** Downloads / unpacks the entry's model(s) into <code>targetDir</code>, replacing any stale content. */
	private def void materialize(RemoteScopeEntry entry, File targetDir) {
		cleanDir(targetDir)
		targetDir.mkdirs
		switch entry.type {
			case RemoteScopeEntry.TYPE_SIMPLE:
				download(entry.url, new File(targetDir, SIMPLE_FILE_NAME))
			case RemoteScopeEntry.TYPE_MAVEN: {
				val stream = new MavenArtifactResolver().openArtifact(entry.groupId, entry.artifactId, entry.version)
				try {
					TarGz.extractCqrsFiles(stream, targetDir)
				} finally {
					stream.close
				}
			}
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
						if (obj.has("namespace") && obj.has("source") && obj.has("dir")) {
							val entry = new CacheEntry(obj.get("namespace").asString, obj.get("source").asString,
								obj.get("dir").asString)
							map.put(entry.namespace, entry)
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
			obj.addProperty("namespace", entry.namespace)
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

	/**
	 * A cached {@code simple} entry whose source is a local <code>file:</code> is stale once that file
	 * has been modified more recently than the cached copy. All other sources (<code>http(s):</code>
	 * and {@code maven}) are treated as up to date.
	 */
	private def boolean upToDate(RemoteScopeEntry entry, File targetDir) {
		if(entry.type != RemoteScopeEntry.TYPE_SIMPLE) return true
		val source = sourceFile(entry.url)
		if(source === null) return true
		val cached = new File(targetDir, SIMPLE_FILE_NAME)
		return cached.exists && source.lastModified <= cached.lastModified
	}

	/** Returns the local source file for a <code>file:</code> URL, or <code>null</code> for other schemes. */
	private def File sourceFile(String url) {
		try {
			val uri = new java.net.URI(url)
			return if("file".equals(uri.scheme)) new File(uri) else null
		} catch (Exception ex) {
			return null
		}
	}

	private def void download(String url, File target) {
		target.parentFile.mkdirs
		val input = new java.net.URI(url).toURL.openStream
		try {
			Files.copy(input, target.toPath, StandardCopyOption.REPLACE_EXISTING)
		} finally {
			input.close
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
		String namespace
		String source
		String dir
	}
}
