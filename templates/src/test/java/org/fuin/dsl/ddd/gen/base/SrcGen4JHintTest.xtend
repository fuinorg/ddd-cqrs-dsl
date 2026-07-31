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
            context shop {
                hint SrcGen4J {
                    "package": "a.b.c",
                    "types": [
                        {
                            "name": "ValueObject",
                            "module": "shared",
                            "group": "domain",
                            "artifacts": [
                                { "artifactFactory": "AbstractValueObject", "folder": "genJava" },
                                { "artifactFactory": "FinalValueObject", "folder": "mainJava", "module": "api", "group": "dto" }
                            ]
                        }
                    ]
                }
            }
        ''')
        val hint = model.contexts.get(0).hints.get(0)

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
        // No per-artifact override: module/group fall back to the type's values.
        assertThat(first.module).isNull
        assertThat(first.group).isNull

        val second = type.artifacts.get(1)
        assertThat(second.artifactFactory).isEqualTo("FinalValueObject")
        assertThat(second.folder).isEqualTo("mainJava")
        // Per-artifact override of module/group.
        assertThat(second.module).isEqualTo("api")
        assertThat(second.group).isEqualTo("dto")
    }

}
