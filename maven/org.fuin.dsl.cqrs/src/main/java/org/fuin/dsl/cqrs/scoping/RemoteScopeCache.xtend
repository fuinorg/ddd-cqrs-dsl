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
import java.util.Map
import org.eclipse.emf.common.CommonPlugin
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtend.lib.annotations.Data

/**
 * Downloads remote <code>.cqrs</code> models and caches them on disk under a
 * <code>.remote-scope-cache</code> directory placed next to the <code>.remote-scope.json</code>
 * catalog.
 *
 * <p>The cache index (<code>.remote-scope-cache/index.json</code>) is read into memory the first
 * time a cache directory is seen and is held for the session, so subsequent lookups find the cached
 * file by its (context, namespace) key without any network access. A cache miss triggers a single
 * HTTP download, writes the file, appends an index entry, persists the index and returns the local
 * file URI.</p>
 */
@Singleton
class RemoteScopeCache {

	static val String CACHE_DIR_NAME = ".remote-scope-cache"
	static val String INDEX_FILE_NAME = "index.json"

	/** In-memory cache index per cache directory: key {@code "context namespace"} &rarr; entry. */
	val Map<File, Map<String, CacheEntry>> indexByDir = newHashMap

	/**
	 * Returns the local cache URI of the remote model that provides the given namespace, downloading
	 * and caching it on a miss, or <code>null</code> when nothing is configured (so the caller falls
	 * back to the standard mechanism).
	 */
	def URI getCachedModelUri(ResourceSet rs, URI modelUri, String namespace, RemoteScopeCatalog catalog) {
		val root = catalog.rootDir(rs, modelUri)
		if(root === null) return null
		val cacheDir = toFile(rs, root.appendSegment(CACHE_DIR_NAME))
		if(cacheDir === null) return null

		val ns = RemoteScopeCatalog.stripWildcard(namespace)
		val url = catalog.lookupUrl(rs, modelUri, namespace)
		if(url.nullOrEmpty) return null

		val index = indexFor(cacheDir)
		val existing = index.get(ns)
		if (existing !== null && existing.url == url) {
			val cached = new File(cacheDir, existing.file)
			if(cached.exists && upToDate(url, cached)) return URI.createFileURI(cached.absolutePath)
		}

		val fileName = ns + "-" + sha1(url) + ".cqrs"
		val target = new File(cacheDir, fileName)
		download(url, target)
		index.put(ns, new CacheEntry(ns, url, fileName))
		persist(cacheDir, index)
		return URI.createFileURI(target.absolutePath)
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
						val entry = new CacheEntry(obj.get("namespace").asString, obj.get("url").asString,
							obj.get("file").asString)
						map.put(entry.namespace, entry)
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
			obj.addProperty("url", entry.url)
			obj.addProperty("file", entry.file)
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
	 * A cached file is current unless its source is a local <code>file:</code> that has been modified
	 * more recently. Non-file sources (e.g. HTTP) are always treated as up to date.
	 */
	private def boolean upToDate(String url, File cached) {
		val source = sourceFile(url)
		return source === null || source.lastModified <= cached.lastModified
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
		String url
		String file
	}
}
