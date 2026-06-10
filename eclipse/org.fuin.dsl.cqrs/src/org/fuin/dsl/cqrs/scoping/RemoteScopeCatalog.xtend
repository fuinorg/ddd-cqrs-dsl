package org.fuin.dsl.cqrs.scoping

import com.google.gson.JsonParser
import com.google.inject.Singleton
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet

/**
 * Reads the local <code>.remote-scope.json</code> catalog that maps a (context, namespace)
 * coordinate to the URL of a remote <code>.cqrs</code> model.
 *
 * <p>The catalog file is discovered by walking up the directory hierarchy starting at the model
 * resource's URI. Its name defaults to <code>.remote-scope.json</code> and can be overridden with
 * the system property <code>cqrs.remote.scope.file</code>. The JSON structure is a nested object
 * <code>context &rarr; namespace &rarr; url</code>:</p>
 *
 * <pre>
 * {
 *   "com.acme.sales": {
 *     "com.acme.billing": "http://models.acme.com/billing.cqrs"
 *   }
 * }
 * </pre>
 *
 * <p>A coordinate that is not present in the catalog yields <code>null</code>, which lets the
 * caller fall back to the standard file-based scoping mechanism.</p>
 */
@Singleton
class RemoteScopeCatalog {

	static val String DEFAULT_FILE_NAME = ".remote-scope.json"

	/** Sentinel stored in the discovery cache to mark "no catalog found" (maps can't cache null). */
	static val URI NONE = URI.createURI("cqrs:no-remote-scope-catalog")

	static val Map<Object, Object> NO_OPTIONS = newHashMap

	/** Caches the discovered catalog URI per containing directory (value may be {@link #NONE}). */
	val Map<URI, URI> configUriByDir = newHashMap

	/** Caches the parsed catalog per discovered catalog URI. */
	val Map<URI, Map<String, Map<String, String>>> catalogByConfig = newHashMap

	def String fileName() {
		System.getProperty("cqrs.remote.scope.file", DEFAULT_FILE_NAME)
	}

	/** Directory that contains the catalog file for the given model, or <code>null</code> if none. */
	def URI rootDir(ResourceSet rs, URI modelUri) {
		val config = configUri(rs, modelUri)
		return if(config === NONE) null else config.trimSegments(1)
	}

	/**
	 * Resolves the remote model URL configured for the (context, namespace) pair, or
	 * <code>null</code> when not configured. A trailing <code>.*</code> wildcard on the namespace
	 * is ignored, so <code>import a.b</code> and <code>import a.b.*</code> resolve to the same entry.
	 */
	def String lookupUrl(ResourceSet rs, URI modelUri, String context, String namespace) {
		val catalog = catalog(rs, modelUri)
		return catalog?.get(context)?.get(stripWildcard(namespace))
	}

	def static String stripWildcard(String namespace) {
		if(namespace !== null && namespace.endsWith(".*")) namespace.substring(0, namespace.length - 2) else namespace
	}

	private def Map<String, Map<String, String>> catalog(ResourceSet rs, URI modelUri) {
		val config = configUri(rs, modelUri)
		if(config === NONE) return null
		return catalogByConfig.computeIfAbsent(config)[parse(rs, it)]
	}

	private def URI configUri(ResourceSet rs, URI modelUri) {
		if(modelUri === null) return NONE
		configUriByDir.computeIfAbsent(modelUri.trimSegments(1))[findConfigUri(rs, it)]
	}

	private def URI findConfigUri(ResourceSet rs, URI startDir) {
		val converter = rs.URIConverter
		val name = fileName
		var dir = startDir
		while(dir !== null && dir.segmentCount > 0) {
			val candidate = dir.appendSegment(name)
			if(converter.exists(candidate, NO_OPTIONS)) return candidate
			dir = dir.trimSegments(1)
		}
		return NONE
	}

	private def Map<String, Map<String, String>> parse(ResourceSet rs, URI configUri) {
		val result = <String, Map<String, String>>newLinkedHashMap
		val reader = new InputStreamReader(rs.URIConverter.createInputStream(configUri, NO_OPTIONS),
			StandardCharsets.UTF_8)
		try {
			val root = JsonParser.parseReader(reader).asJsonObject
			for (contextEntry : root.entrySet) {
				val namespaces = <String, String>newLinkedHashMap
				for (namespaceEntry : contextEntry.value.asJsonObject.entrySet) {
					namespaces.put(namespaceEntry.key, namespaceEntry.value.asString)
				}
				result.put(contextEntry.key, namespaces)
			}
		} finally {
			reader.close
		}
		return result
	}
}
