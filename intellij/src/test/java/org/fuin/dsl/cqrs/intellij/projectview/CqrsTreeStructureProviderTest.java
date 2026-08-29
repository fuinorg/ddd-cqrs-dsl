package org.fuin.dsl.cqrs.intellij.projectview;

import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAttribute;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsParameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Verifies the Project view wiring: when the file node is swapped, and what the swapped node yields. */
public class CqrsTreeStructureProviderTest extends BasePlatformTestCase {

    /**
     * "Show members" is declared on ViewSettings itself. Stubbing the interface directly is what keeps
     * this test honest about that - narrowing the parameter to ProjectViewSettings in the provider
     * would fail here rather than in a pane nobody tries.
     */
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
                    aggregate Order identifier OrderId {
                        String customer
                        method rename fires OrderRenamedEvent {
                            String newName
                        }
                    }
                }
            }
            """;

    private Collection<AbstractTreeNode<?>> modify(String fileName, String source, ViewSettings settings) {
        myFixture.configureByText(fileName, source);
        final PsiFile file = myFixture.getFile();
        final PsiFileNode fileNode = new PsiFileNode(getProject(), file, settings);
        return new CqrsTreeStructureProvider()
                .modify(fileNode, List.<AbstractTreeNode<?>>of(fileNode), settings);
    }

    private List<String> childNames(AbstractTreeNode<?> node) {
        final List<String> names = new ArrayList<>();
        for (AbstractTreeNode<?> child : node.getChildren()) {
            names.add(child.getValue() instanceof CqrsNamedElement named ? named.getName() : "?");
        }
        return names;
    }

    /** The rows under a node, whether they are groups or declarations. */
    private List<String> rowNames(AbstractTreeNode<?> node) {
        final List<String> names = new ArrayList<>();
        for (AbstractTreeNode<?> child : node.getChildren()) {
            names.add(child.toTestString(null));
        }
        return names;
    }

    /** Steps through the group a declaration is filed under. */
    private AbstractTreeNode<?> onlyMemberOf(AbstractTreeNode<?> node) {
        final AbstractTreeNode<?> group = node.getChildren().iterator().next();
        return group.getChildren().iterator().next();
    }

    public void testShowMembersOffLeavesTheFileNodeAlone() {
        // ViewSettings.DEFAULT answers false, which is the state the tree starts in.
        final Collection<AbstractTreeNode<?>> result = modify("test.cqrs", MODEL, ViewSettings.DEFAULT);

        assertEquals(1, result.size());
        assertFalse(result.iterator().next() instanceof CqrsFileNode);
    }

    public void testShowMembersOnReplacesTheFileNodeAndListsTheContexts() {
        final Collection<AbstractTreeNode<?>> result = modify("test.cqrs", MODEL, SHOW_MEMBERS);

        final AbstractTreeNode<?> node = result.iterator().next();
        assertTrue("Expected the file node to be swapped", node instanceof CqrsFileNode);

        // Declarations are filed under their kind, so the file lists kinds and the kind lists what it
        // holds - one context here, but the level is there whether or not it saves anything.
        assertEquals(List.of("contexts"), rowNames(node));
        assertEquals(List.of("p"), rowNames(node.getChildren().iterator().next()));

        // The handle is promised without parsing, so a directory of models renders without being read.
        assertTrue(((CqrsFileNode) node).isAlwaysShowPlus());
        assertFalse(((CqrsFileNode) node).expandOnDoubleClick());
    }

    public void testANonCqrsFileIsUntouched() {
        final Collection<AbstractTreeNode<?>> result = modify("test.txt", "nothing to see", SHOW_MEMBERS);

        assertFalse(result.iterator().next() instanceof CqrsFileNode);
    }

    public void testMemberRowShowsTheBareNameAndNoLocationString() {
        final AbstractTreeNode<?> file = modify("test.cqrs", MODEL, SHOW_MEMBERS).iterator().next();
        final AbstractTreeNode<?> context = onlyMemberOf(file);

        // The presentation is a template until update() has run.
        context.update();

        assertEquals("p", context.getPresentation().getPresentableText());
        assertNull("The file name belongs in go-to-symbol, not directly beneath that same file",
                context.getPresentation().getLocationString());
        assertNotNull(context.getPresentation().getIcon(false));
    }

    public void testMemberRowNavigatesToItsDeclaration() {
        final AbstractTreeNode<?> file = modify("test.cqrs", MODEL, SHOW_MEMBERS).iterator().next();
        final AbstractTreeNode<?> context = onlyMemberOf(file);

        assertTrue(context.canNavigateToSource());
    }

    /** Autoscroll-from-source: the caret inside an operation selects that operation, not the file. */
    public void testSelectionWalksOutToTheNearestRow() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsTreeStructureProvider testee = new CqrsTreeStructureProvider();

        final CqrsParameter parameter =
                PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsParameter.class);
        assertNotNull(parameter);
        assertTrue(testee.getTopLevelElement(parameter) instanceof CqrsMethodDef);

        // An attribute has no row, so the selection stops at the declaration holding it.
        final CqrsAttribute attribute =
                PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsAttribute.class);
        assertNotNull(attribute);
        final PsiElement selected = testee.getTopLevelElement(attribute);
        assertTrue(selected instanceof CqrsNamedElement);
        assertEquals("Order", ((CqrsNamedElement) selected).getName());
    }

    public void testAFileOfAnotherLanguageHasNoSelection() {
        myFixture.configureByText("test.txt", "plain");
        assertNull(new CqrsTreeStructureProvider().getTopLevelElement(myFixture.getFile()));
    }
}
