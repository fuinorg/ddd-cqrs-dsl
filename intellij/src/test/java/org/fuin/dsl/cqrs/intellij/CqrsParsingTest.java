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
