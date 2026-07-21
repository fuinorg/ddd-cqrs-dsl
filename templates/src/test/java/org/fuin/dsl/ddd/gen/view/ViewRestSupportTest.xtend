package org.fuin.dsl.ddd.gen.view

import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.*

/**
 * Tests for the REST path helpers of a view.
 */
class ViewRestSupportTest {

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

}
