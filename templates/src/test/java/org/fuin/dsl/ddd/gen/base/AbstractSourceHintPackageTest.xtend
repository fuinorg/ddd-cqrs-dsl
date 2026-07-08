package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.valueobject.ValueObjectArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.cqrs.extensions.CqrsEObjectExtensions.*

/**
 * Verifies that {@link AbstractSource#asPackage} derives the package from the project's "SrcGen4J"
 * generator hint (see "dsl-examples/22-hint.cqrs") when a matching type entry exists.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class AbstractSourceHintPackageTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Test
    def void testHintDrivenPackage() {

        // PREPARE
        val model = parser.parse('''
            project myproj {
                hint SrcGen4J {
                    "package": "${project}.${module}.${group}.${context}.${namespace}",
                    "types": [
                        {
                            "name": "org.fuin.dsl.cqrs.cqrsDsl.ValueObject",
                            "module": "shared",
                            "group": "domain",
                            "artifacts": [
                                { "artifactFactory": "org.fuin.dsl.ddd.gen.valueobject.ValueObjectArtifactFactory", "folder": "genJava" }
                            ]
                        }
                    ]
                }
                context ctx {
                    namespace ns {
                        type String
                        value-object Money {
                            String amount
                        }
                    }
                }
            }
        ''')
        val vo = model.find(typeof(ValueObject), "Money")

        val factory = new ValueObjectArtifactFactory()
        val config = new ArtifactFactoryConfig("vo", ValueObjectArtifactFactory.name, "project", "folder")
        config.init(new DefaultContext(), null)
        factory.init(config)

        // TEST
        val pkg = factory.asPackage(vo.namespace)
        val artifact = factory.newArtifact("myproj/shared/domain/ctx/ns/Money.java",
            "data".getBytes("UTF-8"), vo.namespace)

        // VERIFY the package (project/context/namespace from the model, module/group from the hint)
        assertThat(pkg).isEqualTo("myproj.shared.domain.ctx.ns")

        // VERIFY newArtifact takes the target project from the hint "module" and the folder from the
        // matching artifact's "folder".
        assertThat(artifact.project).isEqualTo("shared")
        assertThat(artifact.folder).isEqualTo("genJava")
    }
}
