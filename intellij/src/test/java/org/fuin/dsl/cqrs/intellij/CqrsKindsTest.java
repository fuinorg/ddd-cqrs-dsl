package org.fuin.dsl.cqrs.intellij;

import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;

import java.util.List;
import java.util.TreeSet;
import java.util.Set;

/**
 * Verifies that every kind of declaration has a name to be filed under.
 *
 * <p>Swept over the example models rather than asserted type by type: a kind added to the grammar later
 * arrives in one of these files and fails here, rather than showing up in a tree under "other" where
 * nobody would think to look for it.
 */
public class CqrsKindsTest extends BasePlatformTestCase {

    private static final List<String> EXAMPLES = List.of(
            "examples/aggregate.cqrs", "examples/entity.cqrs", "examples/enumobject.cqrs",
            "examples/event.cqrs", "examples/exception.cqrs", "examples/valueobject.cqrs",
            "examples/service.cqrs", "examples/constraint.cqrs", "examples/annotation.cqrs",
            "examples/projection_and_view.cqrs", "examples/process_manager.cqrs",
            "examples/grammar_additions.cqrs", "examples/shared_business_rules.cqrs",
            "examples/common_types.cqrs");

    @Override
    protected String getTestDataPath() {
        return "src/test/resources";
    }

    public void testEveryDeclarationHasAKind() {
        final Set<String> unnamed = new TreeSet<>();
        for (final String example : EXAMPLES) {
            myFixture.configureByFile(example);
            for (final CqrsNamedElement named
                    : PsiTreeUtil.findChildrenOfType(myFixture.getFile(), CqrsNamedElement.class)) {
                if (CqrsKinds.OTHER.equals(CqrsKinds.pluralOf(named))) {
                    unnamed.add(named.getClass().getInterfaces()[0].getSimpleName());
                }
            }
        }
        assertEquals("These kinds would be filed under '" + CqrsKinds.OTHER + "'", Set.of(), unnamed);
    }

    public void testTheNameIsTheKeywordPluralised() {
        myFixture.configureByFile("examples/aggregate.cqrs");
        final CqrsNamedElement aggregate = PsiTreeUtil
                .findChildrenOfType(myFixture.getFile(), CqrsNamedElement.class).stream()
                .filter(e -> "aggregates".equals(CqrsKinds.pluralOf(e))).findFirst().orElseThrow();

        // The words the model is written in, so a reader never has to translate a heading back into
        // the keyword they would type to add one.
        assertEquals("aggregates", CqrsKinds.pluralOf(aggregate));
    }

}
