package org.fuin.dsl.ddd.flutter.resourceset

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.flutter.base.AbstractDartSource
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.ddd.gen.base.TestExtensions.*

/** Tests the one const value navigation is built from. */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class DartModuleCatalogueArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testCatalogue() {
        val artifact = generate()
        assertThat(artifact.pathAndName).isEqualTo("module_catalogue.dart")
        assertThat(artifact.module).isEqualTo("flutter.contract")
        assertThat(artifact.folder).isEqualTo("genMainDart")
        assertThat(new String(artifact.data, "UTF-8")).isEqualTo("module_catalogue.dart".loadDartExample)
    }

    @Test
    def void testTheUserFacingUnitIsTheGroupNotTheModelModule() {
        // The model splits a bounded context in two - the aggregate side and the read side - and a user
        // has no use for that distinction. Enablement stays per model module, so both names are carried.
        assertThat(source).contains("group: 'categories'")
        assertThat(source).contains("modules: <String>['categories', 'categories.categoryview']")
    }

    @Test
    def void testADependencyOnAnotherContextIsNotADependencyOfThisCatalogue() {
        // categories reaches into the common basics of another context for VersionedEntityIdPath. That
        // is not something an installation can switch on or off here, and recording it would invite a
        // client to try.
        assertThat(source).contains("dependsOn: <String>[]")
    }

    @Test
    def void testItNamesTheViewsAndTheCommandsOfEachGroup() {
        assertThat(source).contains("views: <ViewDescriptor>[categoryView]")
        assertThat(source).contains("CreateCategoryCommand.descriptor")
        assertThat(source).contains("RemoveCategoryCommand.descriptor")
    }

    @Test
    def void testNothingIsGeneratedDuringPreparation() {
        assertThat(createTestee.create(resourceSet, new HashMap<String, Object>(), true)).isNull
    }

    private def String source() {
        new String(generate().data, "UTF-8")
    }

    private def generate() {
        createTestee.create(resourceSet, new HashMap<String, Object>(), false).iterator.next
    }

    private def createTestee() {
        val factory = new DartModuleCatalogueArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("dartModules",
            DartModuleCatalogueArtifactFactory.name, "flutter.contract", "genMainDart")
        config.addVariable(new Variable(AbstractDartSource.KEY_DART_PACKAGE, "melkheftken_contract"))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def ResourceSet resourceSet() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/dart-categories.cqrs")))
        validationTester.assertNoErrors(model)
        return model.eResource.resourceSet
    }

}
