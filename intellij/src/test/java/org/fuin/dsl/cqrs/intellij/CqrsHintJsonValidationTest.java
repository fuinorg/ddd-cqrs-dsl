package org.fuin.dsl.cqrs.intellij;

import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.psi.CqrsHintDef;

import java.util.List;

/**
 * Verifies the hint-JSON validation on the IntelliJ side: a hint's JSON is validated against the schema
 * that matches its name (via {@link CqrsHintJson}), and a 'JpaHint' outside a view is flagged with a
 * placement warning by {@link CqrsValidationAnnotator}.
 */
public class CqrsHintJsonValidationTest extends BasePlatformTestCase {

    /** Parses the text, finds the (first) hint, and validates its JSON against its schema. */
    private List<String> validateHint(String body) {
        myFixture.configureByText("test.cqrs", body);
        CqrsHintDef hint = PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsHintDef.class);
        assertNotNull("no hint found in fixture", hint);
        String schema = CqrsHintJson.schemaForHintName(hint.getName());
        assertNotNull("no schema for hint " + hint.getName(), schema);
        return CqrsHintJson.validate(hint.getJson(), schema);
    }

    public void testValidJpaHintValidatesClean() {
        List<String> messages = validateHint("""
                project p {
                context c {
                  namespace n {
                    projection Pj
                    view V uses Pj {
                      hint JpaHint {
                        "tables": [
                          { "className": "Customer", "columns": [ { "fieldName": "id", "javaType": "java.util.UUID" } ] }
                        ]
                      }
                    }
                  }
                }
                }
                """);
        assertEmpty(messages);
    }

    public void testInvalidJpaHintReportsSchemaViolation() {
        // "length" must be an integer, not a string.
        List<String> messages = validateHint("""
                project p {
                context c {
                  namespace n {
                    projection Pj
                    view V uses Pj {
                      hint JpaHint {
                        "tables": [
                          { "className": "Customer", "columns": [
                            { "fieldName": "id", "javaType": "String", "length": "big" } ] }
                        ]
                      }
                    }
                  }
                }
                }
                """);
        assertFalse("expected a schema violation, got none", messages.isEmpty());
    }

    public void testInvalidSrcGen4JReportsSchemaViolation() {
        // A "types" entry must have a "name".
        List<String> messages = validateHint("""
                project p {
                  hint SrcGen4J { "types": [ { "module": "x" } ] }
                  context c { }
                }
                """);
        assertFalse("expected a schema violation, got none", messages.isEmpty());
    }

    public void testJpaHintInsideViewHasNoWarning() {
        myFixture.configureByText("test.cqrs", """
                project p {
                context c {
                  namespace n {
                    projection Pj
                    view V uses Pj {
                      hint JpaHint { "tables": [] }
                    }
                  }
                }
                }
                """);
        myFixture.checkHighlighting(true, false, false);
    }

    public void testJpaHintOutsideViewWarns() {
        myFixture.configureByText("test.cqrs", """
                project p {
                  hint <warning descr="JpaHint only generates code inside a view">JpaHint</warning> { "tables": [] }
                  context c { }
                }
                """);
        myFixture.checkHighlighting(true, false, false);
    }

}
