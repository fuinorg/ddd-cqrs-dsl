package org.fuin.dsl.ddd.gen.base

import com.google.inject.Provider
import jakarta.inject.Inject
import java.nio.file.Files
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.script.CqrsScripts
import org.fuin.srcgen4j.core.emf.PrimaryResources
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*

/**
 * Verifies that the Java package of a generated type comes from the "model2JavaPackage" script: from
 * the preset shipped with these templates when a model declares none, and from the model's own script
 * when it does.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class ScriptedPackageTest {

    @Inject ParseHelper<DomainModel> parser

    @Inject Provider<XtextResourceSet> resourceSetProvider

    /** Scripts are compiled once per source, and every test here publishes its own. */
    @BeforeEach
    def void forgetCompiledScripts() {
        CqrsScripts.invalidate
    }

    /** Without a hint the preset decides, and it reproduces the layout of the old "package" pattern. */
    @Test
    def void testPresetPackage() {

        val model = parser.parse('''
            context p {
                module x.a {
                    value-object MyValueObject {
                        String value
                    }
                }
            }
        ''')

        val vo = model.contexts.head.modules.head.elements.filter(ValueObject).head
        assertThat(CqrsScripts.model2JavaPackage(vo, TypeKeys.JAVA_VALUE_OBJECT)).
            isEqualTo("p.shared.domain.x.a")
    }

    /** The preset places each kind of artifact of one element in its own module. */
    @Test
    def void testPresetPackagePerTypeKey() {

        val model = parser.parse('''
            context p {
                module x.a {
                    value-object MyValueObject {
                        String value
                    }
                }
            }
        ''')

        val vo = model.contexts.head.modules.head.elements.filter(ValueObject).head
        assertThat(CqrsScripts.model2JavaPackage(vo, TypeKeys.JAVA_COMMAND)).isEqualTo("p.command.api.x.a")
        assertThat(CqrsScripts.model2JavaPackage(vo, TypeKeys.JAVA_AGGREGATE)).
            isEqualTo("p.command.core.domain.x.a")
        assertThat(CqrsScripts.model2JavaPackage(vo, TypeKeys.JAVA_VIEW_REST_API_SPRING)).
            isEqualTo("p.query.api.view.x.a")
    }

    /** A model that declares its own script decides for itself; the path is relative to the ".cqrs". */
    @Test
    def void testModelScriptOverridesThePreset() {

        val dir = Files.createTempDirectory("scripted-package")
        Files.createDirectories(dir.resolve("scripts"))
        Files.writeString(dir.resolve("scripts/model2JavaPackage.js"), '''
            function model2JavaPackage(element, typeKey) {
                return 'com.acme.' + typeKey.replace(/-/g, '_');
            }
        ''')

        val model = parser.parse('''
            context p {
                hint SrcGen4J {
                    "model2JavaPackage": "scripts/model2JavaPackage.js"
                }
                module x.a {
                    value-object MyValueObject {
                        String value
                    }
                }
            }
        ''', URI.createFileURI(dir.resolve("main.cqrs").toString), resourceSetProvider.get)

        val vo = model.contexts.head.modules.head.elements.filter(ValueObject).head
        assertThat(CqrsScripts.model2JavaPackage(vo, TypeKeys.JAVA_VALUE_OBJECT)).
            isEqualTo("com.acme.java_value_object")
    }

    /**
     * The scripts shipped with these templates are the default: a model that declares no hint at all,
     * or only one of the two fields, gets the preset for whatever it left out.
     */
    @Test
    def void testPresetIsTheDefaultPerField() {

        val dir = Files.createTempDirectory("scripted-default")
        Files.createDirectories(dir.resolve("scripts"))
        Files.writeString(dir.resolve("scripts/model2JavaPackage.js"), '''
            function model2JavaPackage(element, typeKey) {
                return 'com.acme';
            }
        ''')

        val model = parser.parse('''
            context p {
                hint SrcGen4J {
                    "model2JavaPackage": "scripts/model2JavaPackage.js"
                }
                module x.a {
                    value-object MyValueObject {
                        String value
                    }
                }
            }
        ''', URI.createFileURI(dir.resolve("main.cqrs").toString), resourceSetProvider.get)

        val vo = model.contexts.head.modules.head.elements.filter(ValueObject).head

        // The declared one wins ...
        assertThat(CqrsScripts.model2JavaPackage(vo, TypeKeys.JAVA_VALUE_OBJECT)).isEqualTo("com.acme")

        // ... and the one that was not declared comes from the preset.
        val target = CqrsScripts.artifact2Target(vo, TypeKeys.JAVA_VALUE_OBJECT,
            "org.fuin.dsl.ddd.gen.valueobject.FinalValueObjectArtifactFactory")
        assertThat(target.module).isEqualTo("shared")
        assertThat(target.folder).isEqualTo("mainJava")
    }

    /**
     * The target is taken from this build's own models, never from a dependency: a module name only
     * means something to the generator configuration of the project doing the generating.
     */
    @Test
    def void testTargetIgnoresANonPrimaryModel() {

        val dir = Files.createTempDirectory("scripted-target-local")
        Files.createDirectories(dir.resolve("scripts"))
        Files.writeString(dir.resolve("scripts/artifact2Target.js"), '''
            function artifact2Target(element, typeKey, artifactFactory) {
                return { module: 'not-a-module-of-this-build', folder: 'genMainJava' };
            }
        ''')

        val resourceSet = resourceSetProvider.get
        val dependency = parser.parse('''
            context dep {
                hint SrcGen4J {
                    "artifact2Target": "scripts/artifact2Target.js"
                }
                module d.a {
                    value-object DepValueObject {
                        String value
                    }
                }
            }
        ''', URI.createFileURI(dir.resolve("dependency.cqrs").toString), resourceSet)

        val local = parser.parse('''
            context p {
                module x.a {
                    value-object MyValueObject {
                        String value
                    }
                }
            }
        ''', URI.createFileURI(dir.resolve("local.cqrs").toString), resourceSet)

        // Only the local model is parsed by this build; the dependency was pulled in for its types.
        PrimaryResources.install(resourceSet, #{local.eResource.URI})

        val depVo = dependency.contexts.head.modules.head.elements.filter(ValueObject).head
        val target = CqrsScripts.artifact2Target(depVo, TypeKeys.JAVA_VALUE_OBJECT,
            "org.fuin.dsl.ddd.gen.valueobject.FinalValueObjectArtifactFactory")
        assertThat(target.module).isEqualTo("shared")
    }

    /**
     * A dependency that declares no "model2JavaPackage" is an error, not a silent fall back to this
     * project's preset: that would put an imported type in a package it was never generated into.
     * A model published before the mapping became a script looks exactly like this.
     */
    @Test
    def void testDependencyWithoutItsOwnPackageMappingFails() {

        val dir = Files.createTempDirectory("scripted-unmigrated-dependency")
        val resourceSet = resourceSetProvider.get

        val dependency = parser.parse('''
            context dep {
                module d.a {
                    value-object DepValueObject {
                        String value
                    }
                }
            }
        ''', URI.createFileURI(dir.resolve("dependency.cqrs").toString), resourceSet)

        val local = parser.parse('''
            context p {
                module x.a {
                    value-object MyValueObject {
                        String value
                    }
                }
            }
        ''', URI.createFileURI(dir.resolve("local.cqrs").toString), resourceSet)

        PrimaryResources.install(resourceSet, #{local.eResource.URI})

        // The local model still gets the preset ...
        val localVo = local.contexts.head.modules.head.elements.filter(ValueObject).head
        assertThat(CqrsScripts.model2JavaPackage(localVo, TypeKeys.JAVA_VALUE_OBJECT)).
            isEqualTo("p.shared.domain.x.a")

        // ... the dependency's type does not.
        val depVo = dependency.contexts.head.modules.head.elements.filter(ValueObject).head
        assertThatThrownBy[CqrsScripts.model2JavaPackage(depVo, TypeKeys.JAVA_VALUE_OBJECT)].
            hasMessageContaining("declares no 'model2JavaPackage'").
            hasMessageContaining("dep")
    }

    /** A type key the script does not know is an error - there is no declarative mapping left. */
    @Test
    def void testUnknownTypeKeyFails() {

        val model = parser.parse('''
            context p {
                module x.a {
                    value-object MyValueObject {
                        String value
                    }
                }
            }
        ''')

        val vo = model.contexts.head.modules.head.elements.filter(ValueObject).head
        assertThatThrownBy[CqrsScripts.model2JavaPackage(vo, "java-does-not-exist")].
            hasMessageContaining("java-does-not-exist")
    }

    /** A declared script that is not there fails the build, naming the path as it was written. */
    @Test
    def void testMissingScriptFails() {

        val dir = Files.createTempDirectory("scripted-package-missing")

        val model = parser.parse('''
            context p {
                hint SrcGen4J {
                    "model2JavaPackage": "scripts/not-there.js"
                }
                module x.a {
                    value-object MyValueObject {
                        String value
                    }
                }
            }
        ''', URI.createFileURI(dir.resolve("main.cqrs").toString), resourceSetProvider.get)

        val vo = model.contexts.head.modules.head.elements.filter(ValueObject).head
        assertThatThrownBy[CqrsScripts.model2JavaPackage(vo, TypeKeys.JAVA_VALUE_OBJECT)].
            hasMessageContaining("not-there.js")
    }

}
