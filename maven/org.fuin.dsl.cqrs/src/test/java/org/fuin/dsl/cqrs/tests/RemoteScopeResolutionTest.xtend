package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import com.google.inject.Provider
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.Map
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
 * Verifies that cross-references are resolved against remote {@code .cqrs} models declared in the
 * {@code dependencies.json} catalog: a {@code maven} artifact (tar.gz) cached once per GAV under the
 * {@code .dependencies-cache} directory, and a {@code local} directory that is read directly.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class RemoteScopeResolutionTest {

	static val REMOTE_BILLING = '''
		project remote {
			context com.acme {
				namespace billing {
					type Money
				}
			}
		}
	'''

	static val REMOTE_CATALOG = '''
		project remote {
			context com.acme {
				namespace catalog {
					type Sku
				}
			}
		}
	'''

	static val LOCAL_SALES = '''
		project local {
			context com.acme.sales {
				namespace sales {
					import com.acme.billing.*
					value-object Price {
						Money amount
					}
				}
			}
		}
	'''

	/** Imports two namespaces that a single catalog entry provides from one source. */
	static val LOCAL_SALES_BOTH = '''
		project local {
			context com.acme.sales {
				namespace sales {
					import com.acme.billing.*
					import com.acme.catalog.*
					value-object Price {
						Money amount
						Sku item
					}
				}
			}
		}
	'''

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject Provider<XtextResourceSet> resourceSetProvider

	/**
	 * Fetches a {@code maven} artifact (tar.gz) over HTTP, unpacks all models and resolves types from
	 * <em>two</em> namespaces declared by a single catalog entry. The artifact is cached once per GAV,
	 * so both namespaces share a single <code>&lt;artifactId&gt;-&lt;version&gt;-&lt;sha1&gt;</code> dir.
	 */
	@Test
	def void resolvesMavenArtifactOverHttpAndCaches() {
		val root = Files.createTempDirectory("remote-scope-maven")
		val tarGz = TarGzTestSupport.tarGz(#{
			"money.cqrs" -> REMOTE_BILLING.toString,
			"sku.cqrs" -> REMOTE_CATALOG.toString
		})
		val dir = "/org/fuin/test/cqrs-model/0.1.0-SNAPSHOT/"
		val server = serve(#{
			dir + "maven-metadata.xml" -> mavenMetadata.getBytes(StandardCharsets.UTF_8),
			dir + "cqrs-model-0.1.0-20240101.000000-1-cqrs.tar.gz" -> tarGz
		})
		server.start
		val emptyLocalRepo = Files.createTempDirectory("m2-empty")
		System.setProperty("cqrs.maven.repo.snapshots", "http://127.0.0.1:" + server.address.port + "/")
		System.setProperty("maven.repo.local", emptyLocalRepo.toString)
		try {
			Files.writeString(root.resolve("dependencies.json"), '''
				[ { "type": "maven", "namespaces": ["com.acme.billing", "com.acme.catalog"], "data": {
					"groupId": "org.fuin.test", "artifactId": "cqrs-model", "version": "0.1.0-SNAPSHOT" } } ]
			''')

			val model = parse(root, LOCAL_SALES_BOTH)
			EcoreUtil.resolveAll(model.eResource)
			Assertions.assertTrue(model.eResource.errors.empty,
				'''Unexpected errors: «model.eResource.errors.join(", ")»''')
			val attributeTypes = model.projects.head.contexts.head.namespaces.head.elements.filter(ValueObject).head.attributes.map[type]
			Assertions.assertTrue(attributeTypes.forall[it instanceof ExternalType && !eIsProxy],
				"both remote types must be resolved")
			Assertions.assertEquals(#["Money", "Sku"], attributeTypes.map[name].sort)

			val cacheDirs = Files.list(root.resolve(".dependencies-cache")).filter[Files.isDirectory(it)].collect(
				java.util.stream.Collectors.toList)
			Assertions.assertEquals(1, cacheDirs.size,
				"the artifact must be cached once per GAV, shared by both namespaces (no duplication)")
			Assertions.assertTrue(cacheDirs.head.fileName.toString.startsWith("cqrs-model-0.1.0-SNAPSHOT-"),
				"cache dir name must be <artifactId>-<version>-<sha1>")
			Assertions.assertEquals(2, Files.list(cacheDirs.head).filter[toString.endsWith(".cqrs")].count,
				"both .cqrs files from the tar.gz must be unpacked once")
		} finally {
			System.clearProperty("cqrs.maven.repo.snapshots")
			System.clearProperty("maven.repo.local")
			server.stop(0)
		}
	}

	/** A {@code maven} artifact already present in the local repository is used without any network. */
	@Test
	def void resolvesMavenArtifactFromLocalRepository() {
		val root = Files.createTempDirectory("remote-scope-maven-local")
		val localRepo = Files.createTempDirectory("m2-local")
		val artifactDir = Files.createDirectories(
			localRepo.resolve("org/fuin/test/cqrs-model/0.1.0-SNAPSHOT"))
		Files.write(artifactDir.resolve("cqrs-model-0.1.0-SNAPSHOT-cqrs.tar.gz"),
			TarGzTestSupport.tarGz(#{"money.cqrs" -> REMOTE_BILLING.toString}))
		// Unreachable remote base: the local repository must be used instead.
		System.setProperty("cqrs.maven.repo.snapshots", "http://127.0.0.1:1/")
		System.setProperty("maven.repo.local", localRepo.toString)
		try {
			Files.writeString(root.resolve("dependencies.json"), '''
				[ { "type": "maven", "namespaces": ["com.acme.billing"], "data": {
					"groupId": "org.fuin.test", "artifactId": "cqrs-model", "version": "0.1.0-SNAPSHOT" } } ]
			''')

			assertResolvesToMoney(parse(root, LOCAL_SALES))
		} finally {
			System.clearProperty("cqrs.maven.repo.snapshots")
			System.clearProperty("maven.repo.local")
		}
	}

	/** A {@code local} directory is read directly: models resolve and no cache directory is created. */
	@Test
	def void resolvesFromLocalDirectory() {
		val root = Files.createTempDirectory("remote-scope-local")
		val localDir = Files.createTempDirectory("local-models")
		Files.writeString(localDir.resolve("billing.cqrs"), REMOTE_BILLING.toString)
		// Unreachable remote base: it must never be contacted because a local directory is configured.
		System.setProperty("cqrs.maven.repo.snapshots", "http://127.0.0.1:1/")
		try {
			Files.writeString(root.resolve("dependencies.json"), '''
				[ { "type": "maven", "namespaces": ["com.acme.billing"], "data": {
					"groupId": "org.fuin.test", "artifactId": "cqrs-model", "version": "0.1.0-SNAPSHOT",
					"local": "«localDir.toString»" } } ]
			''')

			assertResolvesToMoney(parse(root, LOCAL_SALES))
			Assertions.assertFalse(Files.exists(root.resolve(".dependencies-cache")),
				"a local directory must be read directly without creating a cache")
		} finally {
			System.clearProperty("cqrs.maven.repo.snapshots")
		}
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
		Files.writeString(root.resolve("dependencies.json"), "")
		val model = parse(root, LOCAL_SALES)
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertFalse(model.eResource.errors.empty,
			"reference to remote type must stay unresolved when the catalog cannot be parsed")
	}

	private def static String mavenMetadata() '''
		<metadata>
			<groupId>org.fuin.test</groupId>
			<artifactId>cqrs-model</artifactId>
			<version>0.1.0-SNAPSHOT</version>
			<versioning>
				<snapshotVersions>
					<snapshotVersion>
						<classifier>cqrs</classifier>
						<extension>tar.gz</extension>
						<value>0.1.0-20240101.000000-1</value>
					</snapshotVersion>
				</snapshotVersions>
			</versioning>
		</metadata>
	'''

	/** Starts (but does not yet {@code start()}) a local HTTP server answering the given byte routes. */
	private def static HttpServer serve(Map<String, byte[]> routes) {
		val server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0)
		for (route : routes.entrySet) {
			val body = route.value
			server.createContext(route.key) [ exchange |
				exchange.sendResponseHeaders(200, body.length)
				exchange.responseBody.write(body)
				exchange.close
			]
		}
		return server
	}

	private def DomainModel parse(Path root, CharSequence text) {
		val uri = URI.createFileURI(root.resolve("model.cqrs").toString)
		parseHelper.parse(text, uri, resourceSetProvider.get)
	}

	private def void assertResolvesToMoney(DomainModel model) {
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertTrue(model.eResource.errors.empty,
			'''Unexpected errors: «model.eResource.errors.join(", ")»''')
		val valueObject = model.projects.head.contexts.head.namespaces.head.elements.filter(ValueObject).head
		val type = valueObject.attributes.head.type
		Assertions.assertFalse(type.eIsProxy, "remote type reference must be resolved")
		Assertions.assertTrue(type instanceof ExternalType)
		Assertions.assertEquals("Money", type.name)
	}
}
