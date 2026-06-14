package org.fuin.dsl.cqrs.scoping

import com.google.common.io.CharStreams
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.inject.Singleton
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.List
import java.util.Map
import org.apache.log4j.Logger
import org.eclipse.emf.common.CommonPlugin
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet

/**
 * Reads the local <code>dependencies.json</code> catalog that declares <em>where a namespace
 * lives</em>, mapping it to the Maven artifact that provides the remote <code>.cqrs</code> model(s).
 *
 * <p>The catalog file is discovered by walking up the directory hierarchy starting at the model
 * resource's URI. Its name defaults to <code>dependencies.json</code> and can be overridden with
 * the system property <code>cqrs.dependencies.file</code>. The JSON structure is an array of typed
 * objects, each declaring the fully qualified namespaces it <strong>provides</strong> (the
 * <code>context.namespace</code> values, not the importer) as a <code>namespaces</code> array, a
 * <code>type</code> discriminator (always <code>maven</code>) and a <code>data</code> block with the
 * artifact's <code>groupId</code>/<code>artifactId</code>/<code>version</code>. Listing several
 * namespaces in one entry is handy because a single Maven artifact often holds more than one context
 * and namespace. An optional <code>local</code> directory overrides the artifact and is read
 * directly &mdash; useful while developing a model that is not published yet:</p>
 *
 * <pre>
 * [
 *   { "type": "maven", "namespaces": ["common.types", "common.refs"],
 *     "data": { "groupId": "org.fuin.dsl.cqrs.contexts",
 *               "artifactId": "cqrs-common-model", "version": "0.1.0-SNAPSHOT" } },
 *   { "type": "maven", "namespaces": ["dev.workinprogress"],
 *     "data": { "groupId": "org.acme", "artifactId": "wip-model", "version": "0.0.1-SNAPSHOT",
 *               "local": "../wip-model/src/main/cqrs" } }
 * ]
 * </pre>
 *
 * <p>So <em>any</em> model that contains <code>import common.types.*</code> resolves to that source,
 * regardless of the importing context. A namespace that is not present in the catalog yields
 * <code>null</code>, which lets the caller fall back to the standard file-based scoping mechanism.</p>
 */
@Singleton
class RemoteScopeCatalog {

	static val Logger LOG = Logger.getLogger(RemoteScopeCatalog)

	static val String DEFAULT_FILE_NAME = "dependencies.json"

	/** Sentinel stored in the discovery cache to mark "no catalog found" (maps can't cache null). */
	static val URI NONE = URI.createURI("cqrs:no-remote-scope-catalog")

	static val Map<Object, Object> NO_OPTIONS = newHashMap

	/** Caches the discovered catalog URI per containing directory (value may be {@link #NONE}). */
	val Map<URI, URI> configUriByDir = newHashMap

	/**
	 * Caches the parsed catalog per discovered catalog URI, together with the file's last-modified
	 * time so the catalog is re-read when it is edited: {@code config -> (timestamp -> namespace -> entry)}.
	 */
	val Map<URI, Pair<Long, Map<String, RemoteScopeEntry>>> catalogByConfig = newHashMap

	def String fileName() {
		System.getProperty("cqrs.dependencies.file", DEFAULT_FILE_NAME)
	}

	/** Directory that contains the catalog file for the given model, or <code>null</code> if none. */
	def URI rootDir(ResourceSet rs, URI modelUri) {
		val config = configUri(rs, modelUri)
		return if(config === NONE) null else config.trimSegments(1)
	}

	/**
	 * Resolves the catalog entry that provides the given namespace, or <code>null</code> when no
	 * catalog entry exists for it. A trailing <code>.*</code> wildcard is ignored, so
	 * <code>import a.b</code> and <code>import a.b.*</code> resolve to the same entry.
	 */
	def RemoteScopeEntry lookupEntry(ResourceSet rs, URI modelUri, String namespace) {
		val catalog = catalog(rs, modelUri)
		return catalog?.get(stripWildcard(namespace))
	}

	def static String stripWildcard(String namespace) {
		if(namespace !== null && namespace.endsWith(".*")) namespace.substring(0, namespace.length - 2) else namespace
	}

	private def Map<String, RemoteScopeEntry> catalog(ResourceSet rs, URI modelUri) {
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
	 * Parses the catalog into a map of fully qualified namespace (<code>context.namespace</code>) to
	 * its typed {@link RemoteScopeEntry}. Entries with an unknown <code>type</code> are logged and
	 * skipped; a structurally invalid entry aborts the whole parse.
	 */
	private def Map<String, RemoteScopeEntry> parse(ResourceSet rs, URI configUri) {
		val result = <String, RemoteScopeEntry>newLinkedHashMap
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
				val obj = element.asJsonObject
				val entry = toEntry(obj)
				if(entry !== null) {
					for (ns : namespaces(obj)) result.put(ns, entry)
				}
			}
		} catch (Exception ex) {
			throw new IllegalStateException(
				"Failed to parse remote scope catalog '" + absolutePath(rs, configUri) + "':\n" + content, ex)
		}
		return result
	}

	/**
	 * Builds a typed source entry from a single catalog object <code>{ type, namespaces, data }</code>.
	 * Returns <code>null</code> for an unknown type (logged and skipped); throws for a missing
	 * <code>type</code> or <code>data</code>, or a missing type-specific field. The
	 * <code>namespaces</code> array is read separately by {@link #namespaces(JsonObject)}.
	 */
	private def RemoteScopeEntry toEntry(JsonObject obj) {
		val type = string(obj, "type")
		val data = if(obj.has("data") && obj.get("data").isJsonObject) obj.getAsJsonObject("data") else null
		if(type === null || data === null) {
			throw new IllegalStateException("Each catalog entry needs 'type', 'namespaces' and 'data': " + obj)
		}
		switch type {
			case RemoteScopeEntry.TYPE_MAVEN:
				RemoteScopeEntry.maven(required(data, "groupId"), required(data, "artifactId"),
					required(data, "version"), string(data, "local"))
			default: {
				LOG.warn("Ignoring remote scope entry with unknown type '" + type + "': " + obj)
				null
			}
		}
	}

	/** Reads the required non-empty <code>namespaces</code> string array of a catalog object. */
	private def static List<String> namespaces(JsonObject obj) {
		val element = if(obj.has("namespaces")) obj.get("namespaces") else null
		if(element === null || !element.isJsonArray || element.asJsonArray.empty) {
			throw new IllegalStateException("Each catalog entry needs a non-empty 'namespaces' array: " + obj)
		}
		val result = <String>newArrayList
		for (ns : element.asJsonArray) {
			if(!ns.isJsonPrimitive) {
				throw new IllegalStateException("'namespaces' must contain only strings: " + obj)
			}
			result.add(ns.asString)
		}
		return result
	}

	private def static String string(JsonObject obj, String name) {
		if(obj.has(name) && obj.get(name).isJsonPrimitive) obj.get(name).asString else null
	}

	private def static String required(JsonObject data, String name) {
		val value = string(data, name)
		if(value === null) throw new IllegalStateException("Missing '" + name + "' in 'data': " + data)
		return value
	}
}
