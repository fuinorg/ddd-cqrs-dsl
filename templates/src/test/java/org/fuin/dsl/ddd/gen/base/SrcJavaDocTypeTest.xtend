package org.fuin.dsl.ddd.gen.base

import org.fuin.dsl.cqrs.cqrsDsl.CqrsDslFactory
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.*

class SrcJavaDocTypeTest {

    @Test
    def void test() {

        // PREPARE
        val vo = CqrsDslFactory.eINSTANCE.createValueObject
        vo.setDoc(
            '''
                /**
                 * Bla.
                 */
            '''
        )
        val testee = new SrcJavaDocType(vo)

        // TEST
        val result = testee.toString

        // VERIFY
        assertThat(result).isEqualTo(
            '''
                /**
                 * Bla.
                 */
            '''.toString)

    }

    @Test
    def void testCollapsesMultipleSpaces() {

        // PREPARE - a wrapped doc whose continuation lines still carry the source indentation, which
        // becomes a run of spaces once the lines are joined into one sentence.
        val vo = CqrsDslFactory.eINSTANCE.createValueObject
        vo.setDoc(
            "/** An email address is a unique identifier that specifies where electronic mail\n" +
            "         messages should be delivered. It acts like a digital mailbox, allowing users\n" +
            "         to send and receive messages over the internet. */"
        )
        val testee = new SrcJavaDocType(vo)

        // TEST
        val result = testee.toString

        // VERIFY every run of spaces is collapsed to a single space.
        assertThat(result).isEqualTo(
            "/**\n" +
            " * An email address is a unique identifier that specifies where electronic mail messages" +
            " should be delivered. It acts like a digital mailbox, allowing users to send and receive" +
            " messages over the internet.\n" +
            " */\n")

    }

}
