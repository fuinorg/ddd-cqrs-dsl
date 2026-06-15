package org.fuin.dsl.cqrs.intellij;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsValueObject;

import java.util.Collection;

/**
 * Verifies the caret ('^') keyword escape on the IntelliJ side: an escaped keyword is a valid
 * identifier, its logical name has the caret stripped, and a reference written with the escape
 * resolves to the declaration.
 */
public class CqrsKeywordEscapeTest extends BasePlatformTestCase {

    private static final String MODEL = """
            context c {
              namespace n {
                type String
                value-object ^event {
                  String value
                }
                value-object Foo {
                  ^ev<caret>ent data
                }
              }
            }
            """;

    public void testDeclarationNameHasCaretStripped() {
        myFixture.configureByText("test.cqrs", MODEL.replace("<caret>", ""));
        CqrsValueObject event = findValueObject("event");
        assertNotNull("value-object written as '^event' must be named 'event'", event);
    }

    public void testReferenceToEscapedKeywordResolves() {
        myFixture.configureByText("test.cqrs", MODEL);
        PsiReference reference = myFixture.getReferenceAtCaretPosition();
        assertNotNull("expected a reference at the '^event' attribute type", reference);
        PsiElement target = reference.resolve();
        assertTrue("reference '^event' should resolve to a named declaration",
                target instanceof CqrsNamedElement);
        assertEquals("event", ((CqrsNamedElement) target).getName());
    }

    private CqrsValueObject findValueObject(String name) {
        Collection<CqrsValueObject> vos =
                PsiTreeUtil.findChildrenOfType(myFixture.getFile(), CqrsValueObject.class);
        for (CqrsValueObject vo : vos) {
            if (name.equals(vo.getName())) {
                return vo;
            }
        }
        return null;
    }
}
