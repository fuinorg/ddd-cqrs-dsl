package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import com.google.inject.Provider
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.Map
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ExternalType
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolvers
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * Verifies that cross-references resolve against the models of a declared {@code dependency}: an
 * artifact resolved by Maven and read <em>inside</em> its jar in the local repository, and a
 * {@code local} directory read directly.
 *
 * <p>The artifact is a jar written by the test and handed over by a stub resolver, so this covers the
 * reading half only - that {@code MimaArtifactResolverTest} really resolves through Maven is verified
 * separately.</p>
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class RemoteScopeResolutionTest {

	static val COORDINATE = "org.fuin.test:cqrs-model:1.0.0"

	static val REMOTE_BILLING = '''
		context remote {
			module com.acme.billing {
				type Money
			}
		}
	'''

	static val REMOTE_CATALOG = '''
		context remote {
			module com.acme.catalog {
				type Sku
			}
		}
	'''

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject Provider<XtextResourceSet> resourceSetProvider
	@Inject CqrsModelArchives archives

	/**
	 * What an artifact resolved to is remembered for the session, and every test here publishes the
	 * same coordinate into a temp repository of its own - so the memory has to be dropped in between,
	 * or the second test would see the first one's jar.
	 */
	@BeforeEach
	def void forgetPreviousResolutions() {
		archives.invalidate
	}

	@AfterEach
	def void restoreResolver() {
		CqrsArtifactResolvers.set(null)
	}

	/**
	 * The artifact is resolved by Maven and its models are read straight out of the jar - the URIs of
	 * the resolved types point <em>inside</em> the archive, nothing is unpacked.
	 */
	@Test
	def void resolvesFromInsideTheArtifactJar() {
		val root = Files.createTempDirectory("remote-scope-maven")
		installResolver(root, #{
			"model/money.cqrs" -> REMOTE_BILLING.toString,
			"model/sub/sku.cqrs" -> REMOTE_CATALOG.toString
		})

		val model = parse(root, '''
			context consumer {
				dependency "«COORDINATE»"

				module com.acme.sales {
					import remote.com.acme.billing.*
					import remote.com.acme.catalog.*

					value-object Price {
						Money amount
						Sku item
					}
				}
			}
		''')
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertTrue(model.eResource.errors.empty,
			'''Unexpected errors: «model.eResource.errors.join(", ")»''')

		val attributeTypes = model.contexts.head.modules.head.elements.filter(ValueObject).head.attributes.map[type]
		Assertions.assertTrue(attributeTypes.forall[it instanceof ExternalType && !eIsProxy],
			"both types of the artifact must be resolved")
		Assertions.assertEquals(#["Money", "Sku"], attributeTypes.map[name].sort)

		// The decisive assertion: read in place, out of the jar in the local repository.
		for (type : attributeTypes) {
			val uri = type.eResource.URI.toString
			Assertions.assertTrue(uri.startsWith("archive:"),
				"a model of a dependency must be read from inside the jar, but was: " + uri)
			Assertions.assertTrue(uri.contains("!/model/"),
				"only models below 'model/' are read, but was: " + uri)
		}

		Assertions.assertFalse(Files.exists(root.resolve(".dependencies-cache")),
			"nothing may be unpacked next to the model any more")
	}

	/** A model in a sub folder of the jar is found too - entries are read recursively. */
	@Test
	def void readsModelsFromSubFoldersOfTheJar() {
		val root = Files.createTempDirectory("remote-scope-nested")
		installResolver(root, #{"model/sub/sku.cqrs" -> REMOTE_CATALOG.toString})

		assertResolves(parse(root, '''
			context consumer {
				dependency "«COORDINATE»"

				module com.acme.sales {
					import remote.com.acme.catalog.*

					value-object Price {
						Sku item
					}
				}
			}
		'''), "Sku")
	}

	/** Entries outside 'model/' are not models and must be ignored. */
	@Test
	def void ignoresEntriesOutsideTheModelFolder() {
		val root = Files.createTempDirectory("remote-scope-outside")
		installResolver(root, #{"other/money.cqrs" -> REMOTE_BILLING.toString})

		val model = parse(root, '''
			context consumer {
				dependency "«COORDINATE»"

				module com.acme.sales {
					value-object Price {
						Money amount
					}
				}
			}
		''')
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertFalse(model.eResource.errors.empty,
			"a '.cqrs' outside 'model/' must not be picked up")
	}

	/** A {@code local} directory is read directly, with no resolution at all. */
	@Test
	def void readsLocalDirectoryDirectly() {
		val root = Files.createTempDirectory("remote-scope-local")
		val localDir = Files.createDirectories(root.resolve("provider"))
		Files.writeString(localDir.resolve("billing.cqrs"), REMOTE_BILLING.toString)
		// No resolver at all: a 'local' clause must never reach Maven.
		CqrsArtifactResolvers.set([ groupId, artifactId, version |
			throw new IllegalStateException("must not resolve when 'local' is declared")
		])

		assertResolves(parse(root, '''
			context consumer {
				dependency "«COORDINATE»" local "provider"

				module com.acme.sales {
					import remote.com.acme.billing.*

					value-object Price {
						Money amount
					}
				}
			}
		'''), "Money")
	}

	/** Without a dependency the artifact's types stay unresolved. */
	@Test
	def void fallsBackWithoutDependency() {
		val root = Files.createTempDirectory("remote-scope-none")
		val model = parse(root, '''
			context consumer {
				module com.acme.sales {
					value-object Price {
						Money amount
					}
				}
			}
		''')
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertFalse(model.eResource.errors.empty,
			"reference to a type of another project must stay unresolved without a dependency")
	}

	// ---- helpers ---------------------------------------------------------

	/**
	 * Writes a jar with the given entries and installs a resolver that answers with it.
	 *
	 * <p>Which Maven does the resolving is beside the point here - that is
	 * {@code MimaArtifactResolverTest} - so a stub keeps this test free of any environment and lets the
	 * Eclipse tests bundle, which resolves through m2e, run exactly the same code.</p>
	 */
	private def void installResolver(Path root, Map<String, String> entries) {
		val jar = root.resolve("cqrs-model-1.0.0.jar")
		Files.write(jar, jar(entries))
		CqrsArtifactResolvers.set([ groupId, artifactId, version | jar ])
	}

	/** A jar holding the given entries (path inside the archive to content). */
	private def byte[] jar(Map<String, String> entries) {
		val bytes = new ByteArrayOutputStream
		val zip = new ZipOutputStream(bytes)
		for (entry : entries.entrySet) {
			zip.putNextEntry(new ZipEntry(entry.key))
			zip.write(entry.value.getBytes(StandardCharsets.UTF_8))
			zip.closeEntry
		}
		zip.close
		return bytes.toByteArray
	}

	private def DomainModel parse(Path root, CharSequence text) {
		return parseHelper.parse(text, URI.createFileURI(root.resolve("main.cqrs").toString),
			resourceSetProvider.get)
	}

	private def void assertResolves(DomainModel model, String typeName) {
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertTrue(model.eResource.errors.empty,
			'''Unexpected errors: «model.eResource.errors.join(", ")»''')
		val type = model.contexts.head.modules.head.elements.filter(ValueObject).head.attributes.head.type
		Assertions.assertFalse(type.eIsProxy, typeName + " must resolve")
		Assertions.assertEquals(typeName, type.name)
	}
}
