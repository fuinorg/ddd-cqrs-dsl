package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import com.google.inject.Provider
import java.nio.file.Files
import java.util.List
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.scoping.IScopeProvider
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.Attribute
import org.fuin.dsl.cqrs.cqrsDsl.Command
import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslPackage
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.scoping.CqrsVisibleNames
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * Verifies what content assist may propose. The scope a reference resolves against is wider than
 * what a module reaches directly - it also carries container relative names and, through the global
 * scope, every element of the workspace by its fully qualified name - so proposing all of it would
 * dump the whole workspace into the completion list.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsVisibleNamesTest {

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject Provider<XtextResourceSet> resourceSetProvider
	@Inject IScopeProvider scopeProvider

	/** Only the module's own types - a sibling module and another file stay out. */
	@Test
	def void offersOnlyOwnModuleWithoutImports() {
		val model = parseWithNeighbour('''
			context ctx {
				module vo.m {
					type String
					type Integer

					value-object Money {
						Integer amount
					}
				}
				module vo.sibling {
					type SiblingType
				}
			}
		''')
		Assertions.assertEquals(#["Integer", "Money", "String"],
			proposals(model.firstAttribute, CqrsDslPackage.Literals.VARIABLE__TYPE))
	}

	/** An imported module's types join the list, each exactly once. */
	@Test
	def void offersImportedTypesOnce() {
		val model = parseWithNeighbour('''
			context ctx {
				module vo.m {
					import ctx.vo.sibling.*

					type Integer

					value-object Money {
						Integer amount
					}
				}
				module vo.sibling {
					type SiblingType
				}
			}
		''')
		Assertions.assertEquals(#["Integer", "Money", "SiblingType"],
			proposals(model.firstAttribute, CqrsDslPackage.Literals.VARIABLE__TYPE))
	}

	/** A context wide wildcard reaches every module below it. */
	@Test
	def void contextWildcardOffersEveryModule() {
		val model = parseWithNeighbour('''
			context ctx {
				module vo.m {
					import ctx.*

					type Integer

					value-object Money {
						Integer amount
					}
				}
				module vo.sibling {
					type SiblingType
				}
			}
		''')
		Assertions.assertEquals(#["Integer", "Money", "SiblingType"],
			proposals(model.firstAttribute, CqrsDslPackage.Literals.VARIABLE__TYPE))
	}

	/** A name nested deeper than the module stays proposable - it is still relative to it. */
	@Test
	def void offersNamesNestedInsideTheModule() {
		val model = parseWithNeighbour('''
			context ctx {
				module vo.m {
					type String
					type UUID

					aggregate-id OrderId identifies Order base UUID {
						examples "6bc75dd5-be5b-4c57-977e-8ee404b21c74"
					}

					aggregate Order identifier OrderId {
						method rename {
							String newName
						}
						command RenameCommand target Order.rename {
							String newName
						}
					}
				}
			}
		''')
		val command = model.eAllContents.filter(Command).head
		Assertions.assertTrue(proposals(command, CqrsDslPackage.Literals.COMMAND__TARGET).contains("Order.rename"),
			"a method nested in an aggregate of the same module must be proposable")
	}

	private def firstAttribute(DomainModel model) {
		model.eAllContents.filter(Attribute).head as EObject
	}

	/** The names {@link CqrsVisibleNames} lets through, sorted. */
	private def List<String> proposals(EObject context, EReference reference) {
		scopeProvider.getScope(context, reference).allElements.filter [
			CqrsVisibleNames.isAddressable(context, it)
		].map[name.toString].sort.toList
	}

	/** Parses the model with an unrelated second file in the same resource set. */
	private def DomainModel parseWithNeighbour(CharSequence text) {
		val root = Files.createTempDirectory("visible-names")
		val rs = resourceSetProvider.get
		parseHelper.parse('''
			context other_ctx {
				module far.away {
					type ShouldNotBeOffered
				}
			}
		''', URI.createFileURI(root.resolve("other.cqrs").toString), rs)
		val model = parseHelper.parse(text, URI.createFileURI(root.resolve("main.cqrs").toString), rs)
		EcoreUtil.resolveAll(model.eResource)
		return model
	}
}
