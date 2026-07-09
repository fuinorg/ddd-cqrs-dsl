package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class SrcGen4JHintTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Test
    def void testParse() {

        // PREPARE
        val model = parser.parse('''
            project shop {
                hint SrcGen4J {
                    "package": "a.b.c",
                    "types": [
                        {
                            "name": "ValueObject",
                            "module": "shared",
                            "group": "domain",
                            "artifacts": [
                                { "artifactFactory": "AbstractValueObject", "folder": "genJava" },
                                { "artifactFactory": "FinalValueObject", "folder": "mainJava" }
                            ]
                        }
                    ]
                }
            }
        ''')
        val hint = model.projects.get(0).hints.get(0)

        // TEST
        val result = SrcGen4JHint.parse(hint)

        // VERIFY (string values are returned unquoted)
        assertThat(result.packagePattern).isEqualTo("a.b.c")
        assertThat(result.types).hasSize(1)

        val type = result.types.get(0)
        assertThat(type.name).isEqualTo("ValueObject")
        assertThat(type.module).isEqualTo("shared")
        assertThat(type.group).isEqualTo("domain")
        assertThat(type.artifacts).hasSize(2)

        val first = type.artifacts.get(0)
        assertThat(first.artifactFactory).isEqualTo("AbstractValueObject")
        assertThat(first.folder).isEqualTo("genJava")

        val second = type.artifacts.get(1)
        assertThat(second.artifactFactory).isEqualTo("FinalValueObject")
        assertThat(second.folder).isEqualTo("mainJava")
    }

}
