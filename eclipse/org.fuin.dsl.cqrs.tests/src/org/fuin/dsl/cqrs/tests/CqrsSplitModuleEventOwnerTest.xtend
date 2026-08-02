package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import com.google.inject.Provider
import java.nio.file.Files
import java.nio.file.Path
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Event
import org.fuin.dsl.cqrs.extensions.CqrsEventExtensions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * Verifies that the aggregate an event belongs to is still found when the two are declared in
 * <em>different files</em> of the same module.
 *
 * <p>A module may be split across files, and a model that publishes only part of itself has to be
 * split - the aggregate goes into the private part, the events it fires stay public. Whether an
 * event has an owner is what makes it a domain event carrying that aggregate's id rather than a
 * plain one, so resolving it per file would silently change the generated code the moment a model
 * is split.</p>
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class CqrsSplitModuleEventOwnerTest {

	@Inject ParseHelper<DomainModel> parseHelper
	@Inject Provider<XtextResourceSet> resourceSetProvider

	/** The aggregate declares "fires", and it lives in the other file. */
	@Test
	def void findsTheFiringAggregateInAnotherFile() {
		val root = Files.createTempDirectory("split-module-fires")
		val resourceSet = resourceSetProvider.get

		val public_ = parse(root, "public.cqrs", resourceSet, '''
			context shop {
				module ordering {
					type String
					aggregate-id OrderId identifies Order base String {
					}
					value-object OrderName base String {
						String value
					}
					event OrderCreatedEvent {
						OrderName name
					}
				}
			}
		''')

		parse(root, "private.cqrs", resourceSet, '''
			context shop {
				module ordering {
					aggregate Order identifier OrderId {
						constructor create fires OrderCreatedEvent {
							OrderName name
						}
					}
				}
			}
		''')

		EcoreUtil.resolveAll(resourceSet)

		val event = public_.contexts.head.modules.head.elements.filter(Event).head
		val owner = CqrsEventExtensions.getEntity(event)

		Assertions.assertNotNull(owner,
			"the aggregate firing the event must be found although it lives in another file")
		Assertions.assertEquals("Order", owner.name)
	}

	/** An event nothing fires stays a plain event - the wider search must not invent an owner. */
	@Test
	def void keepsAnUnownedEventPlain() {
		val root = Files.createTempDirectory("split-module-unowned")
		val resourceSet = resourceSetProvider.get

		val model = parse(root, "public.cqrs", resourceSet, '''
			context shop {
				module ordering {
					type String
					value-object OrderName base String {
						String value
					}
					event SomethingHappenedEvent {
						OrderName name
					}
				}
			}
		''')

		parse(root, "private.cqrs", resourceSet, '''
			context shop {
				module ordering {
					aggregate-id OrderId identifies Order base String {
					}
					aggregate Order identifier OrderId {
						constructor create {
							OrderName name
						}
					}
				}
			}
		''')

		EcoreUtil.resolveAll(resourceSet)

		val event = model.contexts.head.modules.head.elements.filter(Event).head
		Assertions.assertNull(CqrsEventExtensions.getEntity(event),
			"an event no aggregate fires must stay a plain event")
	}

	private def DomainModel parse(Path root, String name, XtextResourceSet resourceSet, CharSequence text) {
		return parseHelper.parse(text, URI.createFileURI(root.resolve(name).toString), resourceSet)
	}

}
