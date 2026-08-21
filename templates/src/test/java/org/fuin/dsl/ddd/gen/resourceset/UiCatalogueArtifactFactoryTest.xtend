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
 * Tests the generated UI catalogue - what a client renders navigation from.
 *
 * <p>The properties under test are that wording stated in the model arrives verbatim, that an element
 * stating none is <b>absent</b> rather than present-and-empty, and that the grouping a client needs to
 * render a module's tabs is emitted whether or not the module was captioned.
 */
@InjectWith(typeof(CqrsDslInjectorProvider))
@ExtendWith(InjectionExtension)
class UiCatalogueArtifactFactoryTest {

    @Inject
    ParseHelper<DomainModel> parser

    @Inject
    ValidationTestHelper validationTester

    @Test
    def void testModuleViewAndMethodWordingIsGenerated() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/uicatalogue.cqrs"), new HashMap<String, Object>(), false)

        // VERIFY
        val java = artifact(result, "p/shared/domain/UiCatalogue.java")
        assertThat(java.module).isEqualTo("shared")
        assertThat(java.folder).isEqualTo("genMainJava")

        val src = new String(java.data, "UTF-8")
        assertThat(src).contains("package p.shared.domain;")

        // A module is keyed by its name - the thing no client can turn into a caption by itself. The
        // resource key is that same name, and the bundle is the module's, so a translator reads the
        // identifier the client looks up.
        assertThat(src).contains(
            'Map.entry("x.receipts", new Text("Receipts", "x.receipts", "Receipts", "Receipts and invoices", "Documents issued to customers and received from suppliers"))')

        // A view is keyed by its name, as in PermissionIds.VIEW_METHODS, and shares the bundle of the
        // module that declares it.
        assertThat(src).contains(
            'Map.entry("ReceiptView", new Text("Receipts", "ReceiptView", "List", "Receipt list", "Every receipt, newest first"))')

        // A method is keyed by its permission id, so one lookup serves both questions.
        assertThat(src).contains(
            'Map.entry("ReceiptView.listReceipts", new Text("Receipts", "ReceiptView.listReceipts", "All", "All receipts", "Every receipt this installation holds"))')
    }

    @Test
    def void testElementsWithoutWordingAreAbsent() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/uicatalogue.cqrs"), new HashMap<String, Object>(), false)

        // VERIFY
        val src = new String(artifact(result, "p/shared/domain/UiCatalogue.java").data, "UTF-8")

        // Absent, not an entry holding empty strings: a caller has to be able to tell "the model says
        // nothing" from "the model says this" and pick its own fallback.
        assertThat(src).doesNotContain('Map.entry("ReceiptView.countReceipts"')
        assertThat(src).doesNotContain('Map.entry("x.unnamed", new Text')
        assertThat(src).doesNotContain('Map.entry("OtherView", new Text')
    }

    @Test
    def void testGroupingCoversEveryModuleThatHasAView() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/uicatalogue.cqrs"), new HashMap<String, Object>(), false)

        // VERIFY
        val src = new String(artifact(result, "p/shared/domain/UiCatalogue.java").data, "UTF-8")

        // A client renders a module's tabs from this, so an uncaptioned module still belongs in it.
        assertThat(src).contains('Map.entry("x.receipts", List.of("ReceiptView"))')
        assertThat(src).contains('Map.entry("x.unnamed", List.of("OtherView"))')
    }

    @Test
    def void testLocaleAwareLookupIsGenerated() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/uicatalogue.cqrs"), new HashMap<String, Object>(), false)

        // VERIFY
        val src = new String(artifact(result, "p/shared/domain/UiCatalogue.java").data, "UTF-8")

        // Navigation localizes through the mechanism that already localizes field labels: the module's
        // resource bundle, the catalogue key, and the suffixes the field annotations use.
        assertThat(src).contains("public String getShortLabel(final Locale locale) {")
        assertThat(src).contains("public String getLabel(final Locale locale) {")
        assertThat(src).contains("public String getTooltip(final Locale locale) {")
        assertThat(src).contains('return resolve(locale, ".slabel", shortLabel);')
        assertThat(src).contains('return resolve(locale, ".label", label);')
        assertThat(src).contains('return resolve(locale, ".tooltip", tooltip);')
        assertThat(src).contains("return ResourceBundle.getBundle(bundle, locale).getString(key + suffix);")

        // A missing bundle and a missing key are the same non-event, and a text the model never stated
        // stays absent in every language - a bundle translates, it does not add.
        assertThat(src).contains("catch (final MissingResourceException ex) {")
        assertThat(src).contains("if (fallback == null) {")

        // Plain java.util does all of it, so the generated class drags in no library of its own.
        assertThat(src).contains("import java.util.Locale;")
        assertThat(src).contains("import java.util.MissingResourceException;")
        assertThat(src).contains("import java.util.ResourceBundle;")
        assertThat(src).doesNotContain("org.fuin.objects4j")
    }

    @Test
    def void testModuleDeclaredInTwoFilesBecomesOneEntry() {

        // PREPARE
        val testee = createTestee()

        // TEST
        val result = testee.create(model("/uicatalogue-split-a.cqrs", "/uicatalogue-split-b.cqrs"),
            new HashMap<String, Object>(), false)

        // VERIFY
        val src = new String(artifact(result, "p/shared/domain/UiCatalogue.java").data, "UTF-8")

        // Every map here is keyed by module name, and Map.ofEntries rejects a duplicate key at class
        // initialization - so a module split over two files would compile and then fail on first touch.
        assertThat(src.split('Map.entry\\("x.split", List.of').length - 1).isEqualTo(1)
        assertThat(src.split('Map.entry\\("x.split", new Text').length - 1).isEqualTo(1)

        // The halves add up rather than one of them winning.
        assertThat(src).contains('Map.entry("x.split", List.of("FirstView", "SecondView"))')

        // The wording is taken from the half that states it.
        assertThat(src).contains(
            'Map.entry("x.split", new Text("Split", "x.split", "Split", "Split module", "Declared in two files, and one module to everything that reads the catalogue"))')
    }

    @Test
    def void testNothingIsGeneratedDuringPreparation() {
        assertThat(new UiCatalogueArtifactFactory().create(model("/uicatalogue.cqrs"),
            new HashMap<String, Object>(), true)).isNull
    }

    @Test
    def void testModelWithoutAnyWordingGetsNoCatalogue() {
        // An empty catalogue would name a target module the project may have no reason to own, and
        // there would be nothing in it to caption anything with.
        assertThat(createTestee().create(model("/view.cqrs"), new HashMap<String, Object>(), false)).isNull
    }

    private def artifact(Iterable<org.fuin.srcgen4j.commons.GeneratedArtifact> result, String pathAndName) {
        val found = result.findFirst[it.pathAndName == pathAndName]
        assertThat(found).describedAs("Artifact '" + pathAndName + "' in " + result.map[it.pathAndName].toList).
            isNotNull
        return found
    }

    private def createTestee() {
        val factory = new UiCatalogueArtifactFactory()
        val ArtifactFactoryConfig config = new ArtifactFactoryConfig("uiCatalogue",
            UiCatalogueArtifactFactory.name, "module", "folder")
        config.addVariable(new Variable(GenerateOptions.KEY_COPYRIGHT_HEADER, Utils.readAsString("required-header.txt")))
        config.init(new DefaultContext(), null)
        factory.init(config)
        return factory
    }

    private def ResourceSet model(String resource) {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource(resource)))
        validationTester.assertNoErrors(model)
        return model.eResource.resourceSet
    }

    /** Parses several files into one resource set, the way a model split over several files is read. */
    private def ResourceSet model(String first, String... more) {
        val DomainModel model = parser.parse(Utils.readAsString(class.getResource(first)))
        validationTester.assertNoErrors(model)
        val resourceSet = model.eResource.resourceSet
        for (resource : more) {
            validationTester.assertNoErrors(parser.parse(Utils.readAsString(class.getResource(resource)), resourceSet))
        }
        return resourceSet
    }

}
