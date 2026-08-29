package org.fuin.dsl.cqrs.intellij;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.psi.CqrsBusinessRules;
import org.fuin.dsl.cqrs.intellij.psi.CqrsKeyDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypeRef;

import java.util.List;

/**
 * Verifies that the editor lets an operation name a business key, and refuses what the build refuses.
 *
 * <p>A key derives a uniqueness rule, so it is named where a rule is named. Until this, the resolver
 * accepted only a {@code business_rule} declaration there, which is the same restriction the Xtext
 * metamodel had: the key resolved to nothing and the editor marked a correct model as broken.</p>
 */
public class CqrsKeyUsageTest extends BasePlatformTestCase {

    /** The errors reported for a model, by the part of the message that identifies the check. */
    private List<HighlightInfo> errors(String body, String fragment) {
        myFixture.configureByText("test.cqrs", body);
        return myFixture.doHighlighting().stream()
                .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                .filter(info -> info.getDescription() != null && info.getDescription().contains(fragment))
                .toList();
    }

    /** The usual case: the operation names the key, and the reference finds it. */
    public void testAnOperationNamesAKey() {
        myFixture.configureByText("test.cqrs", model("""
                    /** No two things of the same kind share a name. */
                    key NamePerKind exception DuplicateNameException {
                        attributes name, kind
                        on-collision refuse
                        consistency strong
                    }
                """, "business-rules NamePerKind"));
        CqrsBusinessRules usage = PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsBusinessRules.class);
        assertNotNull("the operation declares a usage", usage);
        CqrsTypeRef named = PsiTreeUtil.findChildOfType(usage, CqrsTypeRef.class);
        assertNotNull("the usage names something", named);
        PsiReference reference = named.getReference();
        assertNotNull("the name is a reference", reference);
        PsiElement resolved = reference.resolve();
        assertTrue("a 'business-rules' clause resolves a key", resolved instanceof CqrsKeyDef);
        assertEquals("NamePerKind", ((CqrsKeyDef) resolved).getName());
    }

    /** A key that refuses needs the exception it throws. */
    public void testARefusingKeyNeedsItsException() {
        assertEquals(1, errors(model("""
                    /** No two things of the same kind share a name. */
                    key NamePerKind {
                        attributes name, kind
                        on-collision refuse
                        consistency strong
                    }
                """, null), "needs the exception that says so").size());
    }

    /** One that overwrites refuses nobody, so an exception beside it would never be thrown. */
    public void testACollisionThatDoesNotRefuseHasNothingToThrow() {
        assertEquals(1, errors(model("""
                    /** A later import replaces the earlier one. */
                    key NamePerKind exception DuplicateNameException {
                        attributes name, kind
                        on-collision overwrite
                        consistency strong
                    }
                """, null), "has nothing to throw").size());
    }

    /**
     * 'display-as' may name an attribute the key is not made of: what a person recognises a thing by
     * is not always what makes it unique.
     */
    public void testDisplayAsReachesTheWholeType() {
        assertEmpty(errors(model("""
                    /** No two things share a kind. */
                    key KindOnly exception DuplicateNameException {
                        attributes kind
                        on-collision refuse
                        consistency strong
                        display-as "${name} (${kind})"
                    }
                """, null), "is not an attribute of this type"));
    }

    /** But not something the type does not have. */
    public void testDisplayAsCannotNameWhatIsNotThere() {
        assertEquals(1, errors(model("""
                    /** No two things share a kind. */
                    key KindOnly exception DuplicateNameException {
                        attributes kind
                        on-collision refuse
                        consistency strong
                        display-as "${nickname}"
                    }
                """, null), "is not an attribute of this type").size());
    }

    /** Two formats are two answers to "what does a picker show", with nothing to choose between them. */
    public void testATypeIsDisplayedByOneKey() {
        assertEquals(1, errors(model("""
                    /** No two things of the same kind share a name. */
                    key NamePerKind exception DuplicateNameException {
                        attributes name, kind
                        on-collision refuse
                        consistency strong
                        display-as "${name}"
                    }

                    /** No two things share a kind. */
                    key KindOnly exception DuplicateNameException {
                        attributes kind
                        on-collision refuse
                        consistency strong
                        display-as "${kind}"
                    }
                """, null), "already says how this type is displayed").size());
    }

    /** An aggregate carrying the given keys, and a constructor optionally checking one. */
    private static String model(String keys, String usage) {
        return """
                context foo {
                    module bar {

                        type String

                        /** Reported when the name is taken. */
                        exception DuplicateNameException {
                            message "Name is already taken"
                        }

                        aggregate-id ThingId identifies Thing {}

                        aggregate Thing identifier ThingId {

                            /** What it is called. */
                            String name

                            /** What kind it is. */
                            String kind

                """ + keys + """

                            /** Creates one. */
                            constructor create """ + (usage == null ? " " : " " + usage + " ") + """
                {
                                /** What it is called. */
                                String name
                            }

                        }

                    }
                }
                """;
    }

}
