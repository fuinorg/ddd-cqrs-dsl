package org.fuin.dsl.ddd.gen.view

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.Method
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.Utils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

/**
 * Tests for the helpers every shape of a view method is rendered through.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class ViewRestSupportTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testKebabCase() {

        // Single word stays as it is, only lower cased
        assertThat(ViewRestSupport.kebabCase("Receipt")).isEqualTo("receipt")
        assertThat(ViewRestSupport.kebabCase("receipt")).isEqualTo("receipt")

        // Every new word is separated by a dash
        assertThat(ViewRestSupport.kebabCase("listReceipts")).isEqualTo("list-receipts")
        assertThat(ViewRestSupport.kebabCase("countByPartner")).isEqualTo("count-by-partner")
        assertThat(ViewRestSupport.kebabCase("MasterData")).isEqualTo("master-data")
        assertThat(ViewRestSupport.kebabCase("JournalEntry")).isEqualTo("journal-entry")

        // A run of capitals is one word - only the last capital starts the next one
        assertThat(ViewRestSupport.kebabCase("readPDFFile")).isEqualTo("read-pdf-file")
        assertThat(ViewRestSupport.kebabCase("PDF")).isEqualTo("pdf")

        // Digits are kept and do not start a word
        assertThat(ViewRestSupport.kebabCase("mt940Statement")).isEqualTo("mt940-statement")

        assertThat(ViewRestSupport.kebabCase("")).isEqualTo("")
    }

    @Test
    def void testPathVariables() {

        assertThat(ViewRestSupport.pathVariables("/{id}")).containsExactly("id")
        assertThat(ViewRestSupport.pathVariables("/{id}/file/{name}")).containsExactly("id", "name")
        assertThat(ViewRestSupport.pathVariables("/plain")).isEmpty
        // An unterminated placeholder is ignored rather than breaking the generator
        assertThat(ViewRestSupport.pathVariables("/{id")).isEmpty
    }

    @Test
    def void testIsOptional() {

        // Only the RESULT counts - "listPersons" has an optional parameter but a certain result
        assertThat(ViewRestSupport.isOptional(method("listPersons"))).isFalse
        assertThat(ViewRestSupport.isOptional(method("findPerson"))).isTrue
        assertThat(ViewRestSupport.isOptional(method("countPersons"))).isFalse
    }

    @Test
    def void testArgs() {

        // Names only: no types, no annotations, nothing to keep in sync with a signature
        assertThat(ViewRestSupport.args(method("listPersons"))).isEqualTo("search")
        assertThat(ViewRestSupport.args(method("findPerson"))).isEqualTo("id")
        assertThat(ViewRestSupport.args(method("countPersons"))).isEqualTo("")
    }

    private def Method method(String name) {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/view.cqrs")))
        validationTester.assertNoErrors(model)
        val view = model.find(typeof(View), "PersonListView")
        return view.methods.findFirst[it.name == name]
    }

}
