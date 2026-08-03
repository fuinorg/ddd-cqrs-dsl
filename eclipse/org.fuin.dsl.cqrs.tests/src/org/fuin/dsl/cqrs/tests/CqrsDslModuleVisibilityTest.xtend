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
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * Verifies that a {@code module} is the unit of visibility: its own elements are reachable by their
 * simple name, everything else needs an {@code import} - even a sibling module of the same context.
 * A fully qualified reference always works, with or without an import.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsDslModuleVisibilityTest {

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject Provider<XtextResourceSet> resourceSetProvider

	/** A module always sees its own elements. */
	@Test
	def void ownModuleIsVisible() {
		assertNoErrors(parse('''
			context shop {
				module catalog {
					type String
					value-object ProductName base String {
						String value
					}
				}
			}
		'''))
	}

	/** Without an import a sibling module of the very same context stays out of reach. */
	@Test
	def void siblingModuleIsNotVisibleWithoutImport() {
		assertErrors(parse('''
			context shop {
				module types {
					type String
				}
				module catalog {
					value-object ProductName base String {
						String value
					}
				}
			}
		'''))
	}

	/** 'context.module.*' makes every type of one module visible. */
	@Test
	def void moduleWildcardImportMakesSiblingVisible() {
		assertNoErrors(parse('''
			context shop {
				module types {
					type String
				}
				module catalog {
					import shop.types.*

					value-object ProductName base String {
						String value
					}
				}
			}
		'''))
	}

	/** 'context.*' reaches every module of a context, including one with a dotted name. */
	@Test
	def void contextWildcardImportReachesDottedModule() {
		assertNoErrors(parse('''
			context shop {
				module core.types {
					type String
				}
				module core.catalog {
					import shop.*

					value-object ProductName base String {
						String value
					}
				}
			}
		'''))
	}

	/** 'context.module.Type' imports a single type. */
	@Test
	def void singleTypeImportMakesOnlyThatTypeVisible() {
		assertNoErrors(parse('''
			context shop {
				module types {
					type String
				}
				module catalog {
					import shop.types.String

					value-object ProductName base String {
						String value
					}
				}
			}
		'''))
	}

	/** An import declared on the context applies to every module below it. */
	@Test
	def void contextImportIsInheritedByModules() {
		assertNoErrors(parse('''
			context shop {
				import shop.types.*

				module types {
					type String
				}
				module catalog {
					value-object ProductName base String {
						String value
					}
				}
			}
		'''))
	}

	/** A fully qualified reference resolves through the global scope, so it needs no import. */
	@Test
	def void fullyQualifiedReferenceNeedsNoImport() {
		assertNoErrors(parse('''
			context shop {
				module types {
					type String
				}
				module catalog {
					value-object ProductName base shop.types.String {
						shop.types.String value
					}
				}
			}
		'''))
	}

	/**
	 * The same simple name in two modules is not ambiguous: a module's own declaration wins over an
	 * imported one. This is what models that reuse names such as {@code TaxRate} rely on.
	 */
	@Test
	def void ownDeclarationShadowsImportedOne() {
		val model = parse('''
			context p {
				module journal {
					type String
					value-object TaxRate base String {
						String value
					}
				}
				module receipts {
					import p.journal.*

					type String
					value-object TaxRate base String {
						String value
					}
					value-object Uses {
						TaxRate rate
					}
				}
			}
		''')
		assertNoErrors(model)

		val receipts = model.contexts.head.modules.findFirst[name == "receipts"]
		val uses = receipts.elements.filter(ValueObject).findFirst[name == "Uses"]
		val resolved = uses.attributes.head.type
		Assertions.assertFalse(resolved.eIsProxy, "TaxRate must resolve")
		Assertions.assertSame(receipts, resolved.eContainer,
			"an unqualified name must resolve to the declaration of its own module, not the imported one")
	}

	/**
	 * The same, with the module spread over two files: its own declaration must still win over the
	 * imported one of the same name.
	 *
	 * <p>A module may be split across files - a model that publishes only part of itself has to be -
	 * and the half that uses a name is not necessarily the half that declares it. Resolving the two
	 * halves against each other only works if the module's own elements shadow its imports no matter
	 * which file either of them sits in.</p>
	 */
	@Test
	def void ownDeclarationShadowsImportedOneAcrossFiles() {
		val root = Files.createTempDirectory("module-visibility-split")
		val resourceSet = resourceSetProvider.get

		// Declares "receipts.TaxRate" - and the "journal" module declaring one of the same name
		parseHelper.parse('''
			context p {
				module journal {
					type String
					value-object TaxRate base String {
						String value
					}
				}
				module receipts {
					type String
					value-object TaxRate base String {
						String value
					}
				}
			}
		''', URI.createFileURI(root.resolve("public.cqrs").toString), resourceSet)

		// Uses it, from the other half of the very same module
		val other = parseHelper.parse('''
			context p {
				module receipts {
					import p.journal.*

					value-object Uses {
						TaxRate rate
					}
				}
			}
		''', URI.createFileURI(root.resolve("private.cqrs").toString), resourceSet)

		EcoreUtil.resolveAll(resourceSet)

		val uses = other.contexts.head.modules.head.elements.filter(ValueObject).findFirst[name == "Uses"]
		val resolved = uses.attributes.head.type
		Assertions.assertFalse(resolved.eIsProxy,
			"TaxRate must resolve although the module is split over two files")
		Assertions.assertEquals("receipts", (resolved.eContainer as Module).name,
			"an unqualified name must resolve to the declaration of its own module, not the imported one")
	}

	private def DomainModel parse(CharSequence text) {
		val root = Files.createTempDirectory("module-visibility")
		val uri = URI.createFileURI(root.resolve("model.cqrs").toString)
		return parseHelper.parse(text, uri, resourceSetProvider.get)
	}

	private def void assertNoErrors(DomainModel model) {
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertTrue(model.eResource.errors.empty,
			'''Unexpected errors: «model.eResource.errors.join(", ")»''')
	}

	private def void assertErrors(DomainModel model) {
		EcoreUtil.resolveAll(model.eResource)
		Assertions.assertFalse(model.eResource.errors.empty,
			"Expected the reference to fail because nothing imports it")
	}
}
