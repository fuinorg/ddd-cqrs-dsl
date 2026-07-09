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

    @Test
    def void testPackageOnlyHintOverridesDefaultPackage() {

        // PREPARE - a hint that overrides only "package" (no "types"). The default preset
        // ("srcgen4j-default.json") still supplies the ValueObject type entry (module "shared", group
        // "domain"), but the model's "package" pattern must win over the preset's.
        val model = parser.parse('''
            project myproj {
                hint SrcGen4J {
                    "package": "${project}.${context}.${namespace}"
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

        // VERIFY the model's "package" overwrites the preset's: no ${module}/${group} segment, so the
        // result is project.context.namespace and NOT the default "myproj.shared.domain.ctx.ns".
        assertThat(pkg).isEqualTo("myproj.ctx.ns")
    }

    @Test
    def void testHintFromAnotherFileOfTheSameProject() {

        // PREPARE - a project split across two ".cqrs" files (two resources in one resource set): the
        // "SrcGen4J" hint is declared in one file's "project myproj" block...
        val hintModel = parser.parse('''
            project myproj {
                hint SrcGen4J {
                    "package": "${project}.${context}.${namespace}"
                }
            }
        ''')
        val resourceSet = hintModel.eResource.resourceSet
        // ...while the value-object lives in the OTHER file's block for the same project.
        val elementModel = parser.parse('''
            project myproj {
                context ctx {
                    namespace ns {
                        type String
                        value-object Money {
                            String amount
                        }
                    }
                }
            }
        ''', resourceSet)
        val vo = elementModel.find(typeof(ValueObject), "Money")

        val factory = new ValueObjectArtifactFactory()
        val config = new ArtifactFactoryConfig("vo", ValueObjectArtifactFactory.name, "project", "folder")
        config.init(new DefaultContext(), null)
        factory.init(config)

        // TEST + VERIFY the hint from the sibling file is picked up (same logical project), overriding
        // the preset package.
        assertThat(factory.asPackage(vo.namespace)).isEqualTo("myproj.ctx.ns")
    }
}
