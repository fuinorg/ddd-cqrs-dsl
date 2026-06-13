package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import com.google.inject.Provider
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * Verifies that cross-references are resolved against remote (HTTP-only) {@code .cqrs} models
 * through the {@code .remote-scope.json} catalog and the {@code .remote-scope-cache} directory.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class RemoteScopeResolutionTest {

	static val REMOTE_BILLING = '''
		context com.acme {
			namespace billing {
				type Money
			}
		}
	'''

	static val LOCAL_SALES = '''
		context com.acme.sales {
			namespace sales {
				import com.acme.billing.*
				value-object Price {
					Money amount
				}
			}
		}
	'''

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject Provider<XtextResourceSet> resourceSetProvider

	/** Fetches the remote model over HTTP, caches it on disk and resolves the cross-reference. */
	@Test
	def void resolvesRemoteTypeOverHttpAndCaches() {
		val root = Files.createTempDirectory("remote-scope-http")
		val server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0)
		server.createContext("/billing.cqrs") [ exchange |
			val bytes = REMOTE_BILLING.toString.getBytes(StandardCharsets.UTF_8)
			exchange.sendResponseHeaders(200, bytes.length)
			exchange.responseBody.write(bytes)
			exchange.close
		]
		server.start
		try {
			val port = server.address.port
			Files.writeString(root.resolve(".remote-scope.json"), '''
				[ { "com.acme.billing": "http://127.0.0.1:«port»/billing.cqrs" } ]
			''')

			val model = parse(root, LOCAL_SALES)
			assertResolvesToMoney(model)

			val cacheDir = root.resolve(".remote-scope-cache")
			Assertions.assertTrue(Files.exists(cacheDir.resolve("index.json")), "cache index must be written")
			Assertions.assertTrue(Files.list(cacheDir).anyMatch[toString.endsWith(".cqrs")],
				"downloaded .cqrs must be cached")
		} finally {
			server.stop(0)
		}
	}

	/** With a pre-populated cache and no reachable server, resolution is served from disk (offline). */
	@Test
	def void resolvesFromCacheWhenOffline() {
		val root = Files.createTempDirectory("remote-scope-offline")
		// Unreachable URL: it must never be contacted because the cache already has the model.
		Files.writeString(root.resolve(".remote-scope.json"), '''
			[ { "com.acme.billing": "http://127.0.0.1:1/billing.cqrs" } ]
		''')
		val cacheDir = Files.createDirectory(root.resolve(".remote-scope-cache"))
		Files.writeString(cacheDir.resolve("billing-cached.cqrs"), REMOTE_BILLING.toString)
		Files.writeString(cacheDir.resolve("index.json"), '''
			{ "entries": [
				{ "namespace": "com.acme.billing",
				  "url": "http://127.0.0.1:1/billing.cqrs", "file": "billing-cached.cqrs" }
			] }
		''')

		assertResolvesToMoney(parse(root, LOCAL_SALES))
	}

	/** Without a catalog the standard mechanism applies and the remote reference stays unresolved. */
	@Test
	def void fallsBackWithoutCatalog() {
		val root = Files.createTempDirectory("remote-scope-none")
		val model = parse(root, LOCAL_SALES)
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertFalse(model.eResource.errors.empty,
			"reference to remote type must stay unresolved without a catalog")
	}

	/** An empty (or otherwise unparseable) catalog must degrade gracefully instead of breaking editing. */
	@Test
	def void fallsBackWithEmptyCatalog() {
		val root = Files.createTempDirectory("remote-scope-empty")
		Files.writeString(root.resolve(".remote-scope.json"), "")
		val model = parse(root, LOCAL_SALES)
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertFalse(model.eResource.errors.empty,
			"reference to remote type must stay unresolved when the catalog cannot be parsed")
	}

	private def DomainModel parse(Path root, CharSequence text) {
		val uri = URI.createFileURI(root.resolve("model.cqrs").toString)
		parseHelper.parse(text, uri, resourceSetProvider.get)
	}

	private def void assertResolvesToMoney(DomainModel model) {
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertTrue(model.eResource.errors.empty,
			'''Unexpected errors: «model.eResource.errors.join(", ")»''')
		val valueObject = model.contexts.head.namespaces.head.elements.filter(ValueObject).head
		val type = valueObject.attributes.head.type
		Assertions.assertFalse(type.eIsProxy, "remote type reference must be resolved")
		Assertions.assertTrue(type instanceof ExternalType)
		Assertions.assertEquals("Money", type.name)
	}
}
