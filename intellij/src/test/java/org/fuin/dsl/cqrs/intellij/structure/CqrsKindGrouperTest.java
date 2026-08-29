package org.fuin.dsl.cqrs.intellij.structure;

import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.ide.util.treeView.smartTree.Group;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.projectview.CqrsMemberNode;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsModuleDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Verifies the structure view's grouping.
 *
 * <p>The grouper is exercised directly rather than through the tree: the platform applies it between
 * the model and what is drawn, so a test that went through {@code CqrsStructureViewModel} would be
 * testing the platform's wrapper rather than the rule.
 */
public class CqrsKindGrouperTest extends BasePlatformTestCase {

    private static final String MODEL = """
            context p {
                module m {
                    type String
                    /** Reported when it is taken. */
                    exception TakenException {
                        message "Taken"
                    }
                    value-object Name base String {
                        String value
                    }
                    aggregate-id OrderId identifies Order base String {}
                    aggregate Order identifier OrderId {
                        /** Who ordered. */
                        String customer
                        /** What it is called. */
                        String title
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

    /** The grouper ignores the parent; a real node is passed because the platform forbids null. */
    private AbstractTreeNode<?> anyParent(CqrsNamedElement element) {
        return new CqrsMemberNode(getProject(), element, ViewSettings.DEFAULT);
    }

    private List<String> group(CqrsNamedElement owner) {
        final List<TreeElement> children = new ArrayList<>();
        for (TreeElement child : new CqrsStructureViewElement(owner).getChildren()) {
            children.add(child);
        }
        final Collection<Group> groups = new CqrsKindGrouper().group(anyParent(owner), children);
        return groups.stream().map(g -> g.getPresentation().getPresentableText()).toList();
    }

    /** A module shows the kinds it declares, alphabetically. */
    public void testAModuleShowsItsKinds() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsModuleDef module = PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsModuleDef.class);

        assertEquals(List.of("aggregate-ids", "aggregates", "exceptions", "types", "value-objects"),
                group(module));
    }

    /**
     * And so does an aggregate. This is the level the project view never reaches - attributes and
     * methods in one run is the other long list, and the reason grouping is not module-only.
     */
    public void testAnAggregateShowsAttributesApartFromMethods() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsAggregateDef aggregate =
                PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsAggregateDef.class);

        assertEquals(List.of("attributes", "methods"), group(aggregate));
    }

    /**
     * A group is the same group in the next build as in this one.
     *
     * <p>The tree matches one build's nodes against the next to know what stays open and what stays
     * selected. A group wraps no PSI, so it is matched by itself - and when it compared its members
     * too, which are new objects every time, nothing ever matched and the view emptied itself the
     * moment it was rebuilt.</p>
     */
    public void testAGroupSurvivesARebuild() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsModuleDef module = PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsModuleDef.class);

        final Collection<Group> first = new CqrsKindGrouper().group(anyParent(module),
                Arrays.asList(new CqrsStructureViewElement(module).getChildren()));
        final Collection<Group> second = new CqrsKindGrouper().group(anyParent(module),
                Arrays.asList(new CqrsStructureViewElement(module).getChildren()));

        assertEquals(new ArrayList<>(first), new ArrayList<>(second));
    }

    /**
     * A group is not grouped again inside itself.
     *
     * <p>The platform groups a node's children and then repeats that for each group it just made. Those
     * children are all of one kind, so a grouper that always groups produces a group of the same name
     * holding the same things, for ever.</p>
     */
    public void testAGroupIsNotGroupedAgainInsideItself() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsModuleDef module = PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsModuleDef.class);
        final CqrsKindGrouper grouper = new CqrsKindGrouper();

        final Group group = grouper.group(anyParent(module),
                Arrays.asList(new CqrsStructureViewElement(module).getChildren())).iterator().next();

        // What the platform asks next: the group's own node, holding the group as its value.
        assertTrue(grouper.group(new TestGroupNode(getProject(), group),
                new ArrayList<>(group.getChildren())).isEmpty());
    }

    /** Stands in for the platform's GroupWrapper: a node whose value is the group. */
    private static final class TestGroupNode extends AbstractTreeNode<Group> {
        private TestGroupNode(com.intellij.openapi.project.Project project, Group value) {
            super(project, value);
        }

        @Override
        public @org.jetbrains.annotations.NotNull Collection<? extends AbstractTreeNode<?>> getChildren() {
            return List.of();
        }

        @Override
        protected void update(@org.jetbrains.annotations.NotNull com.intellij.ide.projectView.PresentationData presentation) {
        }
    }

    /** Members stay in the order the model declares them; only the groups are sorted. */
    public void testMembersKeepTheirDeclaredOrder() {
        myFixture.configureByText("test.cqrs", MODEL);
        final CqrsAggregateDef aggregate =
                PsiTreeUtil.findChildOfType(myFixture.getFile(), CqrsAggregateDef.class);

        final List<TreeElement> children =
                Arrays.asList(new CqrsStructureViewElement(aggregate).getChildren());
        final Group attributes = new CqrsKindGrouper().group(anyParent(aggregate), children).stream()
                .filter(g -> "attributes".equals(g.getPresentation().getPresentableText()))
                .findFirst().orElseThrow();

        assertEquals(List.of("customer", "title"), attributes.getChildren().stream()
                .map(c -> ((StructureViewTreeElement) c).getPresentation().getPresentableText()).toList());
    }

}
