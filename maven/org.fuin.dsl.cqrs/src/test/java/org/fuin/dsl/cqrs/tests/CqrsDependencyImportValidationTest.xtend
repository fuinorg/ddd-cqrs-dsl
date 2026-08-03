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
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolvers
import org.fuin.dsl.cqrs.scoping.CqrsImportProposals
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives
import org.fuin.dsl.cqrs.validation.CqrsDslValidator
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * What a <code>dependency</code> provides must be visible to everything that answers "does this name
 * exist" - not only to the scope that resolves the reference.
 *
 * <p>These tests run on an index that hides the models read out of an archive, which is what an IDE's
 * index does (see {@link WorkspaceOnlyIndexInjectorProvider}). Without that the default headless setup
 * has the dependency's models in the index as well, the two pools agree by accident, and the bug this
 * covers - an <code>import</code> marked as unresolvable while the very types it provides resolve -
 * cannot happen at all.</p>
 *
 * <p>The artifact is a zip written by the test and handed over by a stub resolver, the same way
 * {@link RemoteScopeResolutionTest} does it.</p>
 */
@ExtendWith(InjectionExtension)
@InjectWith(WorkspaceOnlyIndexInjectorProvider)
class CqrsDependencyImportValidationTest {

	static val COORDINATE = "org.fuin.test:cqrs-model:1.0.0"

	static val REMOTE_BILLING = '''
		context remote {
			module com.acme.billing {
				type Money
			}
		}
	'''

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject Provider<XtextResourceSet> resourceSetProvider
	@Inject ValidationTestHelper validationHelper
	@Inject CqrsModelArchives archives
	@Inject CqrsImportProposals importProposals

	@BeforeEach
	def void forgetPreviousResolutions() {
		archives.invalidate
	}

	@AfterEach
	def void restoreResolver() {
		CqrsArtifactResolvers.set(null)
	}

	/**
	 * The coordinate is declared on the context in one file, the import written in another - the way a
	 * model that publishes only part of itself is laid out. Neither the import nor the type it provides
	 * may be reported.
	 */
	@Test
	def void moduleImportOfADependencyIsNotReported() {
		val using = twoFileModel('''
			context consumer {
				module com.acme.sales {
					import remote.com.acme.billing.*

					value-object Price {
						Money amount
					}
				}
			}
		''')

		assertTypeResolves(using, "Money")
		assertNoIssues(using)
	}

	/**
	 * A context level import reaches the modules below it. Those have to be expanded into one resolver
	 * each, and for a dependency they can only come from the artifact - so this fails where the module
	 * level import above still works.
	 */
	@Test
	def void contextImportOfADependencyResolvesAndIsNotReported() {
		val using = twoFileModel('''
			context consumer {
				module com.acme.sales {
					import remote.*

					value-object Price {
						Money amount
					}
				}
			}
		''')

		assertTypeResolves(using, "Money")
		assertNoIssues(using)
	}

	/** An import that really matches nothing is still reported - the fallback must not swallow that. */
	@Test
	def void importMatchingNothingIsStillReported() {
		val using = twoFileModel('''
			context consumer {
				module com.acme.sales {
					import remote.com.acme.shipping.*

					value-object Price {
						String amount
					}
				}
			}
		''')

		val issues = validationHelper.validate(using)
		Assertions.assertTrue(issues.exists[code == CqrsDslValidator.IMPORT_UNRESOLVED],
			'''Expected an «CqrsDslValidator.IMPORT_UNRESOLVED» issue, but got: «issues.join(", ")»''')
	}

	/** What may be written after 'import' includes what the dependency provides. */
	@Test
	def void contentAssistProposesWhatTheDependencyProvides() {
		val using = twoFileModel('''
			context consumer {
				module com.acme.sales {
					type String
				}
			}
		''')
		EcoreUtil.resolveAll(using.eResource.resourceSet)

		val candidates = importProposals.candidates(using.contexts.head.modules.head)
		Assertions.assertTrue(candidates.contains("remote.*"),
			"the dependency's context must be proposable, but was: " + candidates)
		Assertions.assertTrue(candidates.contains("remote.com.acme.billing.*"),
			"the dependency's module must be proposable, but was: " + candidates)
		Assertions.assertTrue(candidates.contains("remote.com.acme.billing.Money"),
			"the dependency's type must be proposable, but was: " + candidates)
	}

	/**
	 * A model read out of an archive is not ours to report on: it belongs to another project, it is
	 * opened read-only, and a marker on it would name a problem the reader cannot fix. The model used
	 * here carries an import that matches nothing, so without the guard it would be reported.
	 */
	@Test
	def void aModelReadOutOfAnArchiveIsNotReportedOn() {
		val root = Files.createTempDirectory("dependency-import-archive")
		installResolver(root, #{
			"model/public/money.cqrs" -> '''
				context remote {
					module com.acme.billing {
						import nothing.of.the.sort.*

						type Money
					}
				}
			'''.toString
		})

		val using = parse(root, "main.cqrs", resourceSetProvider.get, '''
			context consumer {
				dependency "«COORDINATE»"

				module com.acme.sales {
					import remote.com.acme.billing.*

					value-object Price {
						Money amount
					}
				}
			}
		''')
		EcoreUtil.resolveAll(using.eResource.resourceSet)

		val archived = using.eResource.resourceSet.resources.findFirst[CqrsModelArchives.isArchived(it.URI)]
		Assertions.assertNotNull(archived, "the dependency's model must have been read out of the archive")
		assertNoIssues(archived.contents.head)
	}

	// ---- helpers ---------------------------------------------------------

	/**
	 * The declaration of the dependency and the model using it, in two files of one context - the
	 * split a model that publishes only part of itself has to make.
	 *
	 * @param using Content of the second file.
	 *
	 * @return Model of the second file.
	 */
	private def DomainModel twoFileModel(CharSequence using) {
		val root = Files.createTempDirectory("dependency-import")
		installResolver(root, #{"model/public/money.cqrs" -> REMOTE_BILLING.toString})
		val resourceSet = resourceSetProvider.get

		parse(root, "declaration.cqrs", resourceSet, '''
			context consumer {
				dependency "«COORDINATE»"
			}
		''')
		return parse(root, "usage.cqrs", resourceSet, using)
	}

	/**
	 * Nothing at all is reported for the given model. {@code ValidationTestHelper} would do this too,
	 * but it fails through JUnit 4, which the headless test run does not put on the class path.
	 */
	private def void assertNoIssues(EObject model) {
		val issues = validationHelper.validate(model)
		Assertions.assertTrue(issues.empty, '''Unexpected issues: «issues.join(", ")»''')
	}

	private def void assertTypeResolves(DomainModel model, String typeName) {
		EcoreUtil.resolveAll(model.eResource.resourceSet)
		val type = model.contexts.head.modules.head.elements.filter(ValueObject).head.attributes.head.type
		Assertions.assertFalse(type.eIsProxy, typeName + " must resolve against the dependency")
		Assertions.assertEquals(typeName, type.name)
	}

	/** Writes a zip with the given entries and installs a resolver that answers with it. */
	private def void installResolver(Path root, Map<String, String> entries) {
		val archive = root.resolve("cqrs-model-1.0.0.zip")
		Files.write(archive, zip(entries))
		CqrsArtifactResolvers.set([ groupId, artifactId, version | archive ])
	}

	private def byte[] zip(Map<String, String> entries) {
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

	private def DomainModel parse(Path root, String fileName, XtextResourceSet resourceSet, CharSequence text) {
		return parseHelper.parse(text, URI.createFileURI(root.resolve(fileName).toString), resourceSet)
	}
}
