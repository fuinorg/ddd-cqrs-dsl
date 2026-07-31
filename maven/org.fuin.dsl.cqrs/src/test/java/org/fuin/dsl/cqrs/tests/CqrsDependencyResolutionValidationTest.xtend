package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import com.google.inject.Provider
import java.nio.file.Files
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolvers
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives
import org.fuin.dsl.cqrs.validation.CqrsDslValidator
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * A declared <code>dependency</code> that cannot be resolved is reported on its coordinate. Without
 * it the only symptom is every type the artifact provides failing to resolve, which points at the
 * models rather than at the declaration that is actually wrong.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsDependencyResolutionValidationTest {

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject Provider<XtextResourceSet> resourceSetProvider
	@Inject ValidationTestHelper validationHelper
	@Inject CqrsModelArchives archives

	@BeforeEach
	def void forgetPreviousResolutions() {
		archives.invalidate
	}

	@AfterEach
	def void restoreResolver() {
		CqrsArtifactResolvers.set(null)
	}

	/** A 'local' directory that is not there cannot provide anything. */
	@Test
	def void missingLocalDirectoryIsAnError() {
		val model = parse('''
			context ctx {
				dependency "org.acme:no-such-model:1.0.0" local "no-such-dir"

				module m {
					type String
				}
			}
		''')
		validationHelper.assertError(model, CqrsDslPackage.Literals.DEPENDENCY,
			CqrsDslValidator.DEPENDENCY_UNRESOLVED)
	}

	/** A 'local' directory holding models resolves, so nothing is reported. */
	@Test
	def void resolvableLocalDirectoryIsNotAnError() {
		val root = Files.createTempDirectory("dependency-resolution")
		val provider = Files.createDirectories(root.resolve("provider"))
		Files.writeString(provider.resolve("provided.cqrs"), "context remote { module r { type Money } }")

		val model = parseHelper.parse('''
			context ctx {
				dependency "org.acme:provided-model:1.0.0" local "provider"

				module m {
					import remote.r.*

					value-object Price {
						Money amount
					}
				}
			}
		''', URI.createFileURI(root.resolve("main.cqrs").toString), resourceSetProvider.get)

		validationHelper.assertNoError(model, CqrsDslValidator.DEPENDENCY_UNRESOLVED)
	}

	/** An artifact Maven cannot resolve is reported on the coordinate, not swallowed. */
	@Test
	def void unresolvableArtifactIsAnError() {
		// Stands in for whatever Maven says when the coordinate is not in any repository.
		CqrsArtifactResolvers.set([ groupId, artifactId, version |
			throw new IllegalStateException("Could not find artifact " + artifactId)
		])
		val model = parse('''
			context ctx {
				dependency "org.acme:unresolvable-model:1.0.0"

				module m {
					type String
				}
			}
		''')
		validationHelper.assertError(model, CqrsDslPackage.Literals.DEPENDENCY,
			CqrsDslValidator.DEPENDENCY_UNRESOLVED)
	}

	private def DomainModel parse(CharSequence text) {
		val root = Files.createTempDirectory("dependency-resolution")
		return parseHelper.parse(text, URI.createFileURI(root.resolve("main.cqrs").toString),
			resourceSetProvider.get)
	}
}
