package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import com.google.inject.Provider
import java.nio.file.Files
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Module
import org.fuin.dsl.cqrs.scoping.CqrsImportProposals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/** Verifies what content assist offers after an {@code import}. */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsImportProposalsTest {

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject Provider<XtextResourceSet> resourceSetProvider
	@Inject CqrsImportProposals testee

	/** Contexts and modules as wildcards, single types by name - the own module left out. */
	@Test
	def void offersReachableContextsModulesAndTypes() {
		val model = parse('''
			context ctx {
				module types {
					type Money
				}
				module use {
					type Own
				}
			}
		''')
		val candidates = testee.candidates(model.moduleNamed("use"))

		Assertions.assertTrue(candidates.contains("ctx.*"), "context wildcard: " + candidates)
		Assertions.assertTrue(candidates.contains("ctx.types.*"), "module wildcard: " + candidates)
		Assertions.assertTrue(candidates.contains("ctx.types.Money"), "single type: " + candidates)
		Assertions.assertFalse(candidates.contains("ctx.use.*"), "own module: " + candidates)
		Assertions.assertFalse(candidates.contains("ctx.use.Own"), "own type: " + candidates)
	}

	/** Another file - a dependency model lands in the index the same way - is reachable too. */
	@Test
	def void offersNamesOfAnotherFile() {
		val root = Files.createTempDirectory("import-proposals")
		val rs = resourceSetProvider.get
		parseHelper.parse('''
			context other {
				module far {
					type FarAway
				}
			}
		''', URI.createFileURI(root.resolve("other.cqrs").toString), rs)
		val model = parseHelper.parse('''
			context ctx {
				module use {
					type Own
				}
			}
		''', URI.createFileURI(root.resolve("main.cqrs").toString), rs)
		EcoreUtil.resolveAll(model.eResource)

		val candidates = testee.candidates(model.moduleNamed("use"))
		Assertions.assertTrue(candidates.contains("other.*"), candidates.toString)
		Assertions.assertTrue(candidates.contains("other.far.*"), candidates.toString)
		Assertions.assertTrue(candidates.contains("other.far.FarAway"), candidates.toString)
	}

	/** What is already imported is not offered again. */
	@Test
	def void doesNotRepeatWhatIsAlreadyImported() {
		val model = parse('''
			context ctx {
				module types {
					type Money
				}
				module use {
					import ctx.types.*

					type Own
				}
			}
		''')
		val candidates = testee.candidates(model.moduleNamed("use"))

		Assertions.assertFalse(candidates.contains("ctx.types.*"), "already imported: " + candidates)
		Assertions.assertTrue(candidates.contains("ctx.types.Money"),
			"a single type of it may still be narrowed to: " + candidates)
	}

	/** A module named 'a.b' is not the owner of a module named 'a.b.c'. */
	@Test
	def void keepsDottedModuleNamesApart() {
		val model = parse('''
			context ctx {
				module a.b {
					type InB
				}
				module a.b.c {
					type InC
				}
			}
		''')
		val candidates = testee.candidates(model.moduleNamed("a.b"))

		Assertions.assertFalse(candidates.contains("ctx.a.b.InB"), "own type: " + candidates)
		Assertions.assertTrue(candidates.contains("ctx.a.b.c.InC"),
			"a type of the deeper module is not its own: " + candidates)
	}

	private def moduleNamed(DomainModel model, String name) {
		model.eAllContents.filter(Module).findFirst[it.name == name]
	}

	private def DomainModel parse(CharSequence text) {
		val root = Files.createTempDirectory("import-proposals")
		val model = parseHelper.parse(text, URI.createFileURI(root.resolve("main.cqrs").toString),
			resourceSetProvider.get)
		EcoreUtil.resolveAll(model.eResource)
		return model
	}
}
