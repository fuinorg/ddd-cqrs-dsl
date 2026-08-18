package org.fuin.dsl.ddd.gen.resourceset

import java.util.HashMap
import jakarta.inject.Inject
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

/**
 * Tests the read side of the generated Spring configuration. A view contributes two beans that are
 * only ever injected by type - the generated controller and the hand-written service implementation it
 * forwards to - and both have to be registered, because nothing component-scans the generated packages.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class SpringBeansArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testQueryConfigRegistersControllerAndServiceImpl() {

        // PREPARE
        val context = new HashMap<String, Object>()
        val SpringBeansArtifactFactory testee = createTestee()
        val ResourceSet resourceSet = model()

        // TEST
        val result = testee.create(resourceSet, context, false)

        // VERIFY
        val query = result.findFirst[module == "query.starter"]
        assertThat(query).isNotNull
        assertThat(query.folder).isEqualTo("genMainJava")
        assertThat(query.pathAndName).isEqualTo("p/query/starter/QueryBeansConfiguration.java")

        val src = new String(query.data, "UTF-8")

        // Both classes are imported by type ...
        assertThat(src).contains("import p.query.core.view.x.m.PersonListController;")
        assertThat(src).contains("import p.query.core.view.x.m.PersonListServiceImpl;")
        // ... and both are registered, the controller first.
        assertThat(src).contains("@Import({PersonListController.class, PersonListServiceImpl.class})")

        // The view itself is looked up by name, so it stays an explicit prototype scoped bean.
        assertThat(src).contains("@Bean(PersonListView.BEAN_NAME)")

        // The read model's entity manager is asked for by name, not by type: an application may hold the
        // read model in a different database from the rest of its persistence, and a bare EntityManager
        // would not say which one is meant. The query starter declares the bean, so a single-datasource
        // application is unaffected.
        assertThat(src).contains("import org.springframework.beans.factory.annotation.Qualifier;")
        assertThat(src).contains("@Qualifier(\"readModelEntityManager\") final EntityManager em")
    }

    @Test
    def void testNothingIsGeneratedDuringPreparation() {
        assertThat(new SpringBeansArtifactFactory().create(model(), new HashMap<String, Object>(), true)).isNull
    }

    private def createTestee() {
        val factory = new SpringBeansArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("springBeans",
            SpringBeansArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/view.cqrs")))
        validationTester.assertNoErrors(model)
        return model.eResource.resourceSet
    }

}
