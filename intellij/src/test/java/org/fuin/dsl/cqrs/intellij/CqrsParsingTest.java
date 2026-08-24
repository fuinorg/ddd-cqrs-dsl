package org.fuin.dsl.cqrs.intellij;

import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.ParsingTestCase;

import java.util.Collection;

/**
 * Parses every example {@code .cqrs} fixture and asserts the parser produces no error elements.
 * This guards the BNF grammar against regressions.
 */
public class CqrsParsingTest extends ParsingTestCase {

    public CqrsParsingTest() {
        super("examples", "cqrs", new CqrsParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

    public void testAggregate() {
        assertNoErrors("aggregate");
    }

    /** Business keys, soft delete, the row's declared identity, rule predicates and the widened hint slot. */
    public void testGrammarAdditions() {
        assertNoErrors("grammar_additions");
    }

    public void testDependency() {
        assertNoErrors("dependency");
    }

    public void testValueObject() {
        assertNoErrors("valueobject");
    }

    public void testEvent() {
        assertNoErrors("event");
    }

    public void testEnumObject() {
        assertNoErrors("enumobject");
    }

    public void testConstraint() {
        assertNoErrors("constraint");
    }

    public void testEntity() {
        assertNoErrors("entity");
    }

    public void testService() {
        assertNoErrors("service");
    }

    /** A projection with and without input, and a view with an empty and a filled body. */
    public void testProjectionAndView() {
        assertNoErrors("projection_and_view");
    }

    /** A process manager with states, guarded reactions, issued commands and timeouts. */
    public void testProcessManager() {
        assertNoErrors("process_manager");
    }

    public void testException() {
        assertNoErrors("exception");
    }

    /** Annotation declarations and annotation instances prefixing a value-object and an event. */
    public void testAnnotation() {
        assertNoErrors("annotation");
    }

    public void testCommonBasics() {
        assertNoErrors("common_basics");
    }

    public void testCommonConstr() {
        assertNoErrors("common_constr");
    }

    public void testCommonExceptions() {
        assertNoErrors("common_exceptions");
    }

    public void testCommonTypes() {
        assertNoErrors("common_types");
    }

    /**
     * A string may span several lines. The lexer takes the longest match, so a closed string still
     * wins over the rule that ends an unterminated one at the line break.
     */
    public void testMultilineString() {
        assertNoErrors("multiline_string");
    }

    /** Keywords used as identifiers via the '^' escape (e.g. {@code ^event}). */
    public void testEscapedKeywords() {
        assertNoErrors("escaped_keywords");
    }

    /** The module is optional: a context may hold imports and elements directly. */
    /** All three import forms, on the context and on a module. */
    public void testImport() {
        assertNoErrors("import");
    }

    public void testContextWithoutModule() {
        assertNoErrors("context_without_module");
    }

    /** A context may mix modules and type/elements as siblings. */
    public void testContextMixedModulesAndElements() {
        assertNoErrors("context_mixed_modules_and_elements");
    }

    /**
     * A 'business-rule' may sit at module level, so a rule that applies across contexts is stated
     * once and imported - and an aggregate may still declare its own alongside the imported one.
     */
    public void testSharedBusinessRules() {
        assertNoErrors("shared_business_rules");
    }

    private void assertNoErrors(String name) {
        String text;
        try {
            text = loadFile(name + "." + myFileExt);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        PsiFile file = createPsiFile(name, text);
        ensureParsed(file);
        Collection<PsiErrorElement> errors = PsiTreeUtil.collectElementsOfType(file, PsiErrorElement.class);
        assertEmpty("Unexpected parse errors in " + name + ".cqrs", errors);
    }
}
