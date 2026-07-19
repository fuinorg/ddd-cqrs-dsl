package org.fuin.dsl.ddd.gen.view

import java.io.File
import java.nio.^file.Files
import java.util.HashMap
import java.util.List
import java.util.Map
import jakarta.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.fuin.dsl.cqrs.cqrsDsl.View
import org.fuin.dsl.ddd.gen.base.GenerateOptions
import org.fuin.dsl.ddd.gen.base.TestExtensions
import org.fuin.dsl.ddd.gen.base.Utils
import org.fuin.dsl.cqrs.tests.CqrsDslInjectorProvider
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig
import org.fuin.srcgen4j.commons.DefaultContext
import org.fuin.srcgen4j.commons.GeneratedArtifact
import org.fuin.srcgen4j.commons.Variable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

import static org.assertj.core.api.Assertions.*

import static extension org.fuin.dsl.cqrs.extensions.CqrsDomainModelExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.MapExtensions.*

/**
 * Verifies that {@link JpaViewTableArtifactFactory} renders a full JPA entity from a "JpaHint" declared
 * inside a {@code view}, into the view's own package. The generated file is compared against the golden
 * at {@code src/test/expected-java/tst/x/jpa/Customer.java}.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class JpaViewTableArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testCreateEntityFromViewJpaHint() {

        // PREPARE
        val view = model.find(typeof(View), "PersonListView")
        val Map<String, Object> context = new HashMap<String, Object>()
        context.codeReferenceRegistry

        val factory = new JpaViewTableArtifactFactory()
        val config = new ArtifactFactoryConfig("artifact", JpaViewTableArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)

        // TEST
        factory.create(view, context, true) // preparation pass registers references
        val List<GeneratedArtifact> artifacts = factory.create(view, context, false)

        // VERIFY
        assertThat(artifacts).hasSize(1)
        val a = artifacts.get(0)
        assertThat(a.pathAndName).isEqualTo("p/query/core/view/x/m/Customer.java")
        val actual = new String(a.data, "UTF-8")
        val file = new File("src/test/expected-java/" + TestExtensions.EXAMPLES_ABSTRACT + "/x/jpa/Customer.java")
        assertThat(actual).isEqualTo(new String(Files.readAllBytes(file.toPath), "UTF-8"))
    }

    @Test
    def void testCreateEntitiesWithRelationsFromViewJpaHint() {

        // PREPARE
        val view = model.find(typeof(View), "OrderListView")
        val Map<String, Object> context = new HashMap<String, Object>()
        context.codeReferenceRegistry

        val factory = new JpaViewTableArtifactFactory()
        val config = new ArtifactFactoryConfig("artifact", JpaViewTableArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)

        // TEST
        factory.create(view, context, true) // preparation pass registers references
        val List<GeneratedArtifact> artifacts = factory.create(view, context, false)

        // VERIFY - one @OneToMany parent and one @ManyToOne child, each matching its golden
        assertThat(artifacts).hasSize(2)
        assertThat(artifacts.map[pathAndName]).containsExactlyInAnyOrder(
            "p/query/core/view/x/m/Order.java", "p/query/core/view/x/m/OrderLine.java")
        assertGolden(artifacts, "p/query/core/view/x/m/Order.java", "Order.java")
        assertGolden(artifacts, "p/query/core/view/x/m/OrderLine.java", "OrderLine.java")
    }

    private def void assertGolden(List<GeneratedArtifact> artifacts, String pathAndName, String goldenSimpleName) {
        val a = artifacts.findFirst[it.pathAndName == pathAndName]
        val actual = new String(a.data, "UTF-8")
        val file = new File("src/test/expected-java/" + TestExtensions.EXAMPLES_ABSTRACT + "/x/jpa/" + goldenSimpleName)
        assertThat(actual).isEqualTo(new String(Files.readAllBytes(file.toPath), "UTF-8"))
    }

    private def model() {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource("/jpa-view.cqrs")))
        validationTester.assertNoErrors(model)
        return model
    }

}
