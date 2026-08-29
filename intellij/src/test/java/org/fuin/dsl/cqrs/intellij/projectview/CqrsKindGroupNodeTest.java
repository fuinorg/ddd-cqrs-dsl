package org.fuin.dsl.cqrs.intellij.projectview;

import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsModuleDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Verifies that a declaration is filed under its kind rather than listed in one flat run.
 *
 * <p>The largest model this DSL has declares 79 things in a single module, so finding one exception
 * meant reading past every command and event on the way.
 */
public class CqrsKindGroupNodeTest extends BasePlatformTestCase {

    private static final ViewSettings SHOW_MEMBERS = new ViewSettings() {
        @Override
        public boolean isShowMembers() {
            return true;
        }
    };

    private static final String MODEL = """
            context p {
                module m {
                    type String
                    /** Reported when it is taken. */
                    exception TakenException {
                        message "Taken"
                    }
                    /** Reported when it is missing. */
                    exception MissingException {
                        message "Missing"
                    }
                    value-object Name base String {
                        String value
                    }
                    aggregate-id OrderId identifies Order base String {}
                    aggregate Order identifier OrderId {
                        String customer
                        /** Renames it. */
                        method rename fires OrderRenamedEvent {
                            String newName
                            /** It was renamed. */
                            event OrderRenamedEvent {
                                String newName
                            }
                        }
                    }
                }
            }
            """;

    private List<String> rowNames(AbstractTreeNode<?> node) {
        final List<String> names = new ArrayList<>();
        for (AbstractTreeNode<?> child : node.getChildren()) {
            names.add(child.toTestString(null));
        }
        return names;
    }

    /** A module lists the kinds it declares, alphabetically, and each kind lists its own. */
    public void testAModuleListsItsKinds() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsModuleDef module = PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsModuleDef.class);

        final List<AbstractTreeNode<?>> groups =
                CqrsKindGroupNode.groupsOf(getProject(), module, CqrsMembers.childrenOf(module), SHOW_MEMBERS);

        assertEquals(List.of("aggregate-ids", "aggregates", "exceptions", "types", "value-objects"),
                groups.stream().map(g -> g.toTestString(null)).toList());
    }

    /** Within a group the model's own order is kept - only the groups are sorted. */
    public void testAGroupKeepsTheDeclaredOrder() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsModuleDef module = PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsModuleDef.class);

        final AbstractTreeNode<?> exceptions =
                CqrsKindGroupNode.groupsOf(getProject(), module, CqrsMembers.childrenOf(module), SHOW_MEMBERS)
                        .stream().filter(g -> "exceptions".equals(g.toTestString(null))).findFirst().orElseThrow();

        assertEquals(List.of("TakenException", "MissingException"), rowNames(exceptions));
    }

    /**
     * The same inside an aggregate, which is the other place a flat list gets long - and the reason
     * the grouping is not restricted to module level.
     */
    public void testAnAggregateGroupsWhatItHolds() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsAggregateDef aggregate =
                PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsAggregateDef.class);

        final List<AbstractTreeNode<?>> groups =
                CqrsKindGroupNode.groupsOf(getProject(), aggregate, CqrsMembers.childrenOf(aggregate), SHOW_MEMBERS);

        assertEquals(List.of("methods"), groups.stream().map(g -> g.toTestString(null)).toList());
    }

    /** A group is a heading: it says what it is, carries its kind's icon, and goes nowhere. */
    public void testAGroupRowIsAHeading() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsModuleDef module = PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsModuleDef.class);

        final AbstractTreeNode<?> group =
                CqrsKindGroupNode.groupsOf(getProject(), module, CqrsMembers.childrenOf(module), SHOW_MEMBERS).get(0);
        group.update();

        assertEquals("aggregate-ids", group.getPresentation().getPresentableText());
        assertNotNull(group.getPresentation().getIcon(false));
        assertFalse(group.canNavigate());
        assertFalse(group.canNavigateToSource());
    }

    /**
     * A group is told apart from the same kind elsewhere.
     *
     * <p>{@link com.intellij.ide.util.treeView.AbstractTreeNode} compares nodes by value alone, and the
     * value here is the name of the kind - so without an owner every <code>exceptions</code> group in
     * the project would be the same node, and the tree would resolve a selection to whichever it met
     * first.</p>
     */
    public void testAGroupIsNotTheSameKindSomewhereElse() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsModuleDef module = PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsModuleDef.class);
        final CqrsAggregateDef aggregate =
                PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsAggregateDef.class);

        final AbstractTreeNode<?> underModule = CqrsKindGroupNode
                .groupsOf(getProject(), module, CqrsMembers.childrenOf(module), SHOW_MEMBERS).get(0);
        final AbstractTreeNode<?> underAggregate = CqrsKindGroupNode
                .groupsOf(getProject(), aggregate, CqrsMembers.childrenOf(aggregate), SHOW_MEMBERS).get(0);
        final AbstractTreeNode<?> sameAgain = CqrsKindGroupNode
                .groupsOf(getProject(), module, CqrsMembers.childrenOf(module), SHOW_MEMBERS).get(0);

        assertFalse("Groups under different owners are different nodes", underModule.equals(underAggregate));
        assertEquals("The same group rebuilt is the same node", underModule, sameAgain);
    }

    /** Whatever asks which file is selected has to get an answer, or it shows nothing. */
    public void testAGroupKnowsItsFile() {
        myFixture.configureByText("test.cqrs", MODEL);
        final PsiFile file = myFixture.getFile();
        final CqrsModuleDef module = PsiTreeUtil.findChildOfType(file, CqrsModuleDef.class);

        final CqrsKindGroupNode group = (CqrsKindGroupNode) CqrsKindGroupNode
                .groupsOf(getProject(), module, CqrsMembers.childrenOf(module), SHOW_MEMBERS).get(0);

        assertEquals(file.getVirtualFile(), group.getVirtualFile());
    }

    /** Selecting a declaration has to find its way through the new level. */
    public void testAGroupContainsTheFileItsMembersCameFrom() {
        myFixture.configureByText("test.cqrs", MODEL);
        final PsiFile file = myFixture.getFile();
        final CqrsModuleDef module = PsiTreeUtil.findChildOfType(file, CqrsModuleDef.class);

        final CqrsKindGroupNode group = (CqrsKindGroupNode) CqrsKindGroupNode
                .groupsOf(getProject(), module, CqrsMembers.childrenOf(module), SHOW_MEMBERS).get(0);

        assertTrue(group.contains(file.getVirtualFile()));
    }

}
