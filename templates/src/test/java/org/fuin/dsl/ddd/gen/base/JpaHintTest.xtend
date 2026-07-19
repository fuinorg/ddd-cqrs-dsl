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

    @Test
    def void testParseRelations() {

        // PREPARE
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/jpa-view.cqrs")))
        validationTester.assertNoErrors(model)
        val view = model.find(typeof(View), "OrderListView")
        val hint = view.hints.findFirst[name == "JpaHint"]

        // TEST
        val jpa = JpaHint.parse(hint)

        // VERIFY
        assertThat(jpa.tables).hasSize(2)

        val order = jpa.tables.get(0)
        assertThat(order.className).isEqualTo("Order")
        assertThat(order.manyToOnes).isEmpty
        assertThat(order.oneToManys).hasSize(1)
        val lines = order.oneToManys.get(0)
        assertThat(lines.fieldName).isEqualTo("lines")
        assertThat(lines.targetClassName).isEqualTo("OrderLine")
        assertThat(lines.mappedBy).isEqualTo("order")
        assertThat(lines.fetch).isEqualTo("LAZY")
        assertThat(lines.orphanRemoval).isTrue
        assertThat(lines.cascade).containsExactly("ALL")

        val line = jpa.tables.get(1)
        assertThat(line.className).isEqualTo("OrderLine")
        assertThat(line.oneToManys).isEmpty
        assertThat(line.manyToOnes).hasSize(1)
        val order2 = line.manyToOnes.get(0)
        assertThat(order2.fieldName).isEqualTo("order")
        assertThat(order2.targetClassName).isEqualTo("Order")
        assertThat(order2.fetch).isEqualTo("LAZY")
        assertThat(order2.optional).isFalse
        assertThat(order2.joinColumn).isNotNull
        assertThat(order2.joinColumn.name).isEqualTo("ORDER_ID")
        assertThat(order2.joinColumn.referencedColumnName).isEqualTo("ID")
        assertThat(order2.joinColumn.nullable).isFalse
        assertThat(order2.joinColumn.foreignKey).isEqualTo("NO_CONSTRAINT")
    }

}
