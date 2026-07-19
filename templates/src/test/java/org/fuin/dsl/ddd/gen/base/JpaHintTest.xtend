package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

/**
 * Tests the {@link JpaHint} parser: it must map a "JpaHint" hint's JSON to the typed table/column model.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class JpaHintTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testParse() {

        // PREPARE
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/jpa-view.cqrs")))
        validationTester.assertNoErrors(model)
        val view = model.find(typeof(View), "PersonListView")
        val hint = view.hints.findFirst[name == "JpaHint"]

        // TEST
        val jpa = JpaHint.parse(hint)

        // VERIFY
        assertThat(jpa.tables).hasSize(1)
        val table = jpa.tables.get(0)
        assertThat(table.className).isEqualTo("Customer")
        assertThat(table.name).isEqualTo("CUSTOMER")
        assertThat(table.schema).isEqualTo("SALES")
        assertThat(table.catalog).isNull
        assertThat(table.uniqueConstraints).hasSize(1)
        assertThat(table.uniqueConstraints.get(0).name).isEqualTo("UQ_EMAIL")
        assertThat(table.uniqueConstraints.get(0).columnNames).containsExactly("EMAIL")
        assertThat(table.indexes).hasSize(1)
        assertThat(table.indexes.get(0).columnList).isEqualTo("LAST_NAME")
        assertThat(table.indexes.get(0).unique).isFalse

        assertThat(table.columns).hasSize(3)

        val id = table.columns.get(0)
        assertThat(id.fieldName).isEqualTo("id")
        assertThat(id.javaType).isEqualTo("java.util.UUID")
        assertThat(id.id).isTrue
        assertThat(id.nullable).isFalse

        val amount = table.columns.get(1)
        assertThat(amount.fieldName).isEqualTo("amount")
        assertThat(amount.precision).isEqualTo(12)
        assertThat(amount.scale).isEqualTo(2)
        assertThat(amount.digits.integer).isEqualTo(10)
        assertThat(amount.digits.fraction).isEqualTo(2)
        assertThat(amount.decimalMin.value).isEqualTo("0.0")
        assertThat(amount.decimalMin.inclusive).isTrue

        val name = table.columns.get(2)
        assertThat(name.javaType).isEqualTo("String")
        assertThat(name.length).isEqualTo(100)
        assertThat(name.digits).isNull
        assertThat(name.decimalMin).isNull
    }

}
