package org.fuin.dsl.cqrs.scoping

import com.google.common.io.CharStreams
import com.google.gson.JsonParser
import com.google.inject.Singleton
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Map
import org.apache.log4j.Logger
import org.eclipse.emf.common.CommonPlugin
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet

/**
 * Reads the local <code>.remote-scope.json</code> catalog that declares <em>where a namespace
 * lives</em>, mapping it to the URL of the remote <code>.cqrs</code> model that provides it.
 *
 * <p>The catalog file is discovered by walking up the directory hierarchy starting at the model
 * resource's URI. Its name defaults to <code>.remote-scope.json</code> and can be overridden with
 * the system property <code>cqrs.remote.scope.file</code>. The JSON structure is an array of
 * single-entry objects that map a fully qualified namespace (the <strong>provided</strong>
 * <code>context.namespace</code>, not the importer) to the URL of the model that provides it:</p>
 *
 * <pre>
 * [
 *   { "common.basics": "http://models.acme.com/common/basics.cqrs" },
 *   { "common.types":  "http://models.acme.com/common/types.cqrs" }
 * ]
 * </pre>
 *
 * <p>So <em>any</em> model that contains <code>import common.basics.*</code> resolves to that URL,
 * regardless of the importing context. A namespace that is not present in the catalog yields
 * <code>null</code>, which lets the caller fall back to the standard file-based scoping mechanism.</p>
 */
@Singleton
class RemoteScopeCatalog {

	static val Logger LOG = Logger.getLogger(RemoteScopeCatalog)

	static val String DEFAULT_FILE_NAME = ".remote-scope.json"

	/** Sentinel stored in the discovery cache to mark "no catalog found" (maps can't cache null). */
	static val URI NONE = URI.createURI("cqrs:no-remote-scope-catalog")

	static val Map<Object, Object> NO_OPTIONS = newHashMap

	/** Caches the discovered catalog URI per containing directory (value may be {@link #NONE}). */
	val Map<URI, URI> configUriByDir = newHashMap

	/**
	 * Caches the parsed catalog per discovered catalog URI, together with the file's last-modified
	 * time so the catalog is re-read when it is edited: {@code config -> (timestamp -> namespace -> url)}.
	 */
	val Map<URI, Pair<Long, Map<String, String>>> catalogByConfig = newHashMap

	def String fileName() {
		System.getProperty("cqrs.remote.scope.file", DEFAULT_FILE_NAME)
	}

	/** Directory that contains the catalog file for the given model, or <code>null</code> if none. */
	def URI rootDir(ResourceSet rs, URI modelUri) {
		val config = configUri(rs, modelUri)
		return if(config === NONE) null else config.trimSegments(1)
	}

	/**
	 * Resolves the URL of the remote model that provides the given namespace, or <code>null</code>
	 * when no catalog entry exists for it. A trailing <code>.*</code> wildcard is ignored, so
	 * <code>import a.b</code> and <code>import a.b.*</code> resolve to the same entry.
	 */
	def String lookupUrl(ResourceSet rs, URI modelUri, String namespace) {
		val catalog = catalog(rs, modelUri)
		return catalog?.get(stripWildcard(namespace))
	}

	def static String stripWildcard(String namespace) {
		if(namespace !== null && namespace.endsWith(".*")) namespace.substring(0, namespace.length - 2) else namespace
	}

	private def Map<String, String> catalog(ResourceSet rs, URI modelUri) {
		val config = configUri(rs, modelUri)
		if(config === NONE) return null
		val stamp = timeStamp(rs, config)
		val cached = catalogByConfig.get(config)
		if(cached !== null && cached.key == stamp) return cached.value
		val parsed = parse(rs, config)
		catalogByConfig.put(config, stamp -> parsed)
		return parsed
	}

	/**
	 * Discovers the catalog URI for the model's directory. A previously discovered URI is re-validated
	 * so a deleted or moved catalog triggers a fresh search; a previously empty result ({@link #NONE})
	 * stays cached, so adding a brand new catalog where none existed still requires a restart.
	 */
	private def URI configUri(ResourceSet rs, URI modelUri) {
		if(modelUri === null) return NONE
		val dir = modelUri.trimSegments(1)
		val cached = configUriByDir.get(dir)
		if(cached !== null && (cached === NONE || locate(rs, cached) !== null)) return cached
		val found = findConfigUri(rs, dir)
		configUriByDir.put(dir, found)
		return found
	}

	/** Last-modified time of the (file based) catalog, or 0 when it cannot be determined. */
	private def long timeStamp(ResourceSet rs, URI uri) {
		val file = toFile(rs, uri)
		return if(file !== null) file.lastModified else 0L
	}

	private def URI findConfigUri(ResourceSet rs, URI startDir) {
		val name = fileName
		var dir = startDir
		while(dir !== null && dir.segmentCount > 0) {
			val candidate = dir.appendSegment(name)
			val found = locate(rs, candidate)
			LOG.info("Looking for remote scope catalog at '" + absolutePath(rs, candidate) + "' -> " +
				(if(found !== null) "found" else "not found"))
			if(found !== null) return found
			dir = dir.trimSegments(1)
		}
		LOG.info("No remote scope catalog '" + name + "' found above '" + absolutePath(rs, startDir) + "'")
		return NONE
	}

	/**
	 * Returns a readable URI for the candidate when it exists, or <code>null</code> otherwise.
	 * The file system is checked first (so a catalog created outside Eclipse is found even when the
	 * workspace has not been refreshed), falling back to the {@link org.eclipse.emf.ecore.resource.URIConverter}
	 * for non-file URIs (e.g. archive or HTTP based resource sets).
	 */
	private def URI locate(ResourceSet rs, URI candidate) {
		val file = toFile(rs, candidate)
		if(file !== null && file.exists) return URI.createFileURI(file.absolutePath)
		return if(rs.URIConverter.exists(candidate, NO_OPTIONS)) candidate else null
	}

	/** Resolves a URI to a {@link File}, or <code>null</code> when it is not a file system URI. */
	private def File toFile(ResourceSet rs, URI uri) {
		var resolved = rs.URIConverter.normalize(uri)
		if(resolved.isPlatformResource || resolved.isPlatformPlugin) resolved = CommonPlugin.resolve(resolved)
		return if(resolved.isFile) new File(resolved.toFileString) else null
	}

	/** Resolves a URI to an absolute file system path for logging, falling back to the URI string. */
	private def String absolutePath(ResourceSet rs, URI uri) {
		val file = toFile(rs, uri)
		return if(file !== null) file.absolutePath else rs.URIConverter.normalize(uri).toString
	}

	/**
	 * Parses the catalog into a flat map of fully qualified namespace (<code>context.namespace</code>)
	 * to provider URL.
	 */
	private def Map<String, String> parse(ResourceSet rs, URI configUri) {
		val result = <String, String>newLinkedHashMap
		val reader = new InputStreamReader(rs.URIConverter.createInputStream(configUri, NO_OPTIONS),
			StandardCharsets.UTF_8)
		val content = try {
			CharStreams.toString(reader)
		} finally {
			reader.close
		}
		try {
			val rootElement = JsonParser.parseString(content)
			if(!rootElement.isJsonArray) {
				throw new IllegalStateException("Expected a JSON array at the root")
			}
			for (element : rootElement.asJsonArray) {
				if(!element.isJsonObject) {
					throw new IllegalStateException("Expected a JSON object entry, but got '" + element + "'")
				}
				for (entry : element.asJsonObject.entrySet) {
					result.put(entry.key, entry.value.asString)
				}
			}
		} catch (Exception ex) {
			throw new IllegalStateException(
				"Failed to parse remote scope catalog '" + absolutePath(rs, configUri) + "':\n" + content, ex)
		}
		return result
	}
}
