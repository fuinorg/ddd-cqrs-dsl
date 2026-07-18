package org.fuin.dsl.ddd.gen.base

import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.Aggregate
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.aggregate.ESRepositoryArtifactFactory
import org.fuin.dsl.ddd.gen.valueobject.AbstractValueObjectArtifactFactory
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
                                { "artifactFactory": "org.fuin.dsl.ddd.gen.valueobject.AbstractValueObjectArtifactFactory", "folder": "genJava" }
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

        val factory = new AbstractValueObjectArtifactFactory()
        val config = new ArtifactFactoryConfig("vo", AbstractValueObjectArtifactFactory.name, "module", "folder")
        config.init(new DefaultContext(), null)
        factory.init(config)

        // TEST
        val pkg = factory.asPackage(vo.namespace)
        val artifact = factory.newArtifact("myproj/shared/domain/ctx/ns/Money.java",
            "data".getBytes("UTF-8"), vo.namespace)

        // VERIFY the package (project/context/namespace from the model, module/group from the hint)
        assertThat(pkg).isEqualTo("myproj.shared.domain.ctx.ns")

        // VERIFY newArtifact takes the target module from the hint "module" and the folder from the
        // matching artifact's "folder".
        assertThat(artifact.module).isEqualTo("shared")
        assertThat(artifact.folder).isEqualTo("genJava")
    }

    @Test
    def void testHintDrivenPackageOmitsOptionalNamespace() {

        // PREPARE - the same hint as above (optional "[.${namespace}]" segment), but the value-object
        // is declared directly in the context, without a namespace.
        val model = parser.parse('''
            project myproj {
                hint SrcGen4J {
                    "package": "${project}.${module}.${group}.${context}[.${namespace}]",
                    "types": [
                        {
                            "name": "org.fuin.dsl.cqrs.cqrsDsl.ValueObject",
                            "module": "shared",
                            "group": "domain",
                            "artifacts": [
                                { "artifactFactory": "org.fuin.dsl.ddd.gen.valueobject.AbstractValueObjectArtifactFactory", "folder": "genJava" }
                            ]
                        }
                    ]
                }
                context ctx {
                    type String
                    value-object Money {
                        String amount
                    }
                }
            }
        ''')
        val vo = model.find(typeof(ValueObject), "Money")

        val factory = new AbstractValueObjectArtifactFactory()
        val config = new ArtifactFactoryConfig("vo", AbstractValueObjectArtifactFactory.name, "module", "folder")
        config.init(new DefaultContext(), null)
        factory.init(config)

        // TEST - derive the package from the element itself (its namespace is null).
        val pkg = factory.asPackage(vo)

        // VERIFY the optional namespace segment is dropped: the package ends at the context, with no
        // trailing dot.
        assertThat(vo.namespace).isNull
        assertThat(pkg).isEqualTo("myproj.shared.domain.ctx")
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

        val factory = new AbstractValueObjectArtifactFactory()
        val config = new ArtifactFactoryConfig("vo", AbstractValueObjectArtifactFactory.name, "module", "folder")
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

        val factory = new AbstractValueObjectArtifactFactory()
        val config = new ArtifactFactoryConfig("vo", AbstractValueObjectArtifactFactory.name, "module", "folder")
        config.init(new DefaultContext(), null)
        factory.init(config)

        // TEST + VERIFY the hint from the sibling file is picked up (same logical project), overriding
        // the preset package.
        assertThat(factory.asPackage(vo.namespace)).isEqualTo("myproj.ctx.ns")
    }

    @Test
    def void testTypeOverrideWithoutArtifactsAppliesToPresetArtifacts() {

        // PREPARE - the hint overrides only "module"/"group" of a type (no "artifacts"), so the preset's
        // artifacts (here the aggregate's ESRepository -> folder "mainJava") must keep applying, but with
        // the overridden module/group. This mirrors the cqrs-keycloak-example hint.
        val model = parser.parse('''
            project myproj {
                hint SrcGen4J {
                    "types": [
                        { "name": "org.fuin.dsl.cqrs.cqrsDsl.Aggregate", "module": "command", "group": "core.domain" }
                    ]
                }
                context ctx {
                    namespace ns {
                        type String
                        aggregate-id OrderId identifies Order {}
                        aggregate Order identifier OrderId {}
                    }
                }
            }
        ''')
        val aggregate = model.find(typeof(Aggregate), "Order")

        val factory = new ESRepositoryArtifactFactory()
        val config = new ArtifactFactoryConfig("esRepository", ESRepositoryArtifactFactory.name, "module", "folder")
        config.init(new DefaultContext(), null)
        factory.init(config)

        // TEST - route the generated artifact through the hint.
        val artifact = factory.newArtifact("Order.java", "data".getBytes("UTF-8"), aggregate.namespace)

        // VERIFY the overridden module becomes the target module, while the folder still comes from the
        // preset's ESRepository artifact ("mainJava"); the package uses the overridden module/group.
        assertThat(artifact.module).isEqualTo("command")
        assertThat(artifact.folder).isEqualTo("mainJava")
        assertThat(factory.asPackage(aggregate.namespace)).isEqualTo("myproj.command.core.domain.ctx.ns")
    }

    @Test
    def void testArtifactLevelModuleGroupOverridesType() {

        // PREPARE - the type sets module "shared"/group "domain", but the AbstractValueObject artifact
        // overrides them with "api"/"dto". The per-artifact override must win for both the target module
        // and the package (a sibling artifact without an override would keep inheriting the type's values).
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
                                { "artifactFactory": "org.fuin.dsl.ddd.gen.valueobject.AbstractValueObjectArtifactFactory", "folder": "genJava", "module": "api", "group": "dto" }
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

        val factory = new AbstractValueObjectArtifactFactory()
        val config = new ArtifactFactoryConfig("vo", AbstractValueObjectArtifactFactory.name, "module", "folder")
        config.init(new DefaultContext(), null)
        factory.init(config)

        // TEST
        val pkg = factory.asPackage(vo.namespace)
        val artifact = factory.newArtifact("Money.java", "data".getBytes("UTF-8"), vo.namespace)

        // VERIFY the artifact-level module/group win over the type's, for both the package and the module.
        assertThat(pkg).isEqualTo("myproj.api.dto.ctx.ns")
        assertThat(artifact.module).isEqualTo("api")
        assertThat(artifact.folder).isEqualTo("genJava")
    }
}
