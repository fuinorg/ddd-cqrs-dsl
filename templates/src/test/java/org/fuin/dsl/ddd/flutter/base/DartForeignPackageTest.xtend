package org.fuin.dsl.ddd.flutter.base

import com.google.inject.Provider
import jakarta.inject.Inject
import java.nio.file.Files
import java.nio.file.Path
import java.util.HashMap
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.ValueObject
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.flutter.valueobject.DartRowArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactory
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.fuin.srcgen4j.core.emf.PrimaryResources
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

/**
 * What a model generates when it does not own every type it uses.
 *
 * <p>The other Dart fixtures come from a model that declares everything it needs, so they pin the
 * self-contained case and say nothing about this one - which is the ordinary case for an application:
 * the shared context is published as its own Dart package, and the application imports it the way the
 * JVM side takes it from a jar. The two configurations differ only in which package the imports name,
 * which is exactly the kind of difference that goes wrong quietly.
 *
 * <p>"Foreign" is not a property of the model text. It is decided by which resources the parser marked
 * primary, so these tests build a resource set of two files and mark only one - the same thing
 * <code>AbstractEMFParser</code> does for a real build.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartForeignPackageTest {

    static val SHARED = '''
        context org.fuin.test.shared {
            module common.basics {

                type String

                /** An amount of money. */
                value-object Money base String {
                    String value
                }

            }
        }
    '''

    static val LOCAL = '''
        context com.example.shop {
            module sales {

                import org.fuin.test.shared.common.basics.*

                /** What a product is called. */
                value-object ProductName base String {
                    String value
                }

                /** A row using one type it owns and one it does not. */
                value-object OrderLine {
                    ProductName name
                    Money price
                }

            }
        }
    '''

    @Inject ParseHelper<DomainModel> parser
    @Inject Provider<XtextResourceSet> resourceSets

    @Test
    def void testAForeignTypeIsImportedThroughTheOtherPackagesPublicLibrary() {
        // Only the type mapping is configured here, so the runtime stays local: the one import naming
        // the other package can have come from nothing but the foreign type.
        val imports = importsOf(publishedTypesOnly)
        assertThat(imports).contains("import 'package:shared_contract/shared_contract.dart';")
        assertThat(imports).contains("import 'package:shop_contract/src/json/json.dart';")
    }

    @Test
    def void testAForeignTypeIsNotReachedForDirectly() {
        // Its generated tree is already offered by the library above, so naming a file inside it says
        // the same thing twice - which the analyzer reports as an unnecessary import.
        assertThat(importsOf(publishedShared)).doesNotContain("package:shared_contract/src-gen/")
    }

    @Test
    def void testALocalTypeIsImportedFromThisPackage() {
        assertThat(importsOf(publishedShared))
            .contains("import 'package:shop_contract/src-gen/sales/product_name.dart';")
    }

    @Test
    def void testAForeignRuntimeIsNotReachedIntoEither() {
        // `lib/src` is private to the package that owns it. Reaching in is something Dart tells you not
        // to do rather than something it stops, so nothing here may do it.
        val imports = importsOf(publishedShared)
        assertThat(imports).doesNotContain("package:shared_contract/src/")
        assertThat(imports).doesNotContain("package:shop_contract/src/")
        assertThat(imports).contains("import 'package:shared_contract/shared_contract.dart';")
    }

    @Test
    def void testWithNothingPublishedEverythingIsThisPackagesOwn() {
        // The fallback, and what every model did before a shared package existed: a context that
        // publishes no Dart package is generated locally, so the imports name this package throughout.
        val imports = importsOf(new HashMap<String, String>())
        assertThat(imports).contains("import 'package:shop_contract/src/json/json.dart';")
        assertThat(imports).doesNotContain("shared_contract")
    }

    def private static publishedTypesOnly() {
        val vars = new HashMap<String, String>()
        vars.put(AbstractDartSource.KEY_DART_PACKAGES, "org.fuin.test.shared=shared_contract")
        return vars
    }

    def private static publishedShared() {
        val vars = new HashMap<String, String>()
        vars.put(AbstractDartSource.KEY_DART_RUNTIME_PACKAGE, "shared_contract")
        vars.put(AbstractDartSource.KEY_DART_PACKAGES, "org.fuin.test.shared=shared_contract")
        return vars
    }

    /** The import block of the generated row, as one string. */
    def private String importsOf(HashMap<String, String> vars) {
        val root = Files.createTempDirectory("dart-foreign")
        val resourceSet = resourceSets.get
        val shared = parse(root, "shared.cqrs", resourceSet, SHARED)
        val local = parse(root, "local.cqrs", resourceSet, LOCAL)

        // Only the local file is this build's own; the other arrived as a dependency.
        PrimaryResources.install(resourceSet, #{local.eResource.URI})
        assertThat(PrimaryResources.isPrimary(shared.eResource)).isFalse

        val source = new String(generate(vars, local), "UTF-8")
        return source.substring(0, source.indexOf("///"))
    }

    def private static byte[] generate(HashMap<String, String> vars, DomainModel model) {
        val row = model.eAllContents.filter(typeof(ValueObject)).findFirst[name == "OrderLine"]
        val factory = new DartRowArtifactFactory()
        val config = new ArtifactFactoryConfig("dartRow", factory.class.name, "contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "shop_contract"))
        for (entry : vars.entrySet) {
            config.addVariable(new Variable(entry.key, entry.value))
        }
        config.init(new DefaultContext(), null)
        factory.init(config)
        return (factory as ArtifactFactory<ValueObject>)
            .create(row, new HashMap<String, Object>(), false).iterator.next.data
    }

    def private DomainModel parse(Path root, String fileName, XtextResourceSet resourceSet,
            CharSequence text) {
        return parser.parse(text, URI.createFileURI(root.resolve(fileName).toString), resourceSet)
    }

}
