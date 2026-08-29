package org.fuin.dsl.cqrs.intellij.structure;

import com.intellij.icons.AllIcons;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.intellij.ide.util.treeView.smartTree.Group;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.fuin.dsl.cqrs.intellij.CqrsKinds;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Puts a level for the kind between a declaration and whatever holds it.
 *
 * <p>A module of any size lists everything it declares in one run - 79 of them in the largest model
 * this DSL has - so finding one exception means reading past every command and event on the way. With
 * this, a module shows its kinds and each kind its own declarations.
 *
 * <p>It applies at every level rather than only under a module, because the same problem appears again
 * inside an aggregate: its attributes, business rules, constructors, keys and methods are one list too.
 * A group is emitted even where a node holds only one kind, so that a kind is always found at the same
 * depth and the tree does not change shape with its contents.
 *
 * <p>Being a {@link Grouper} rather than something built into the children means the platform supplies
 * the toolbar toggle, and the structure view's alphabetical sorter goes on sorting <em>within</em> each
 * group.
 */
public final class CqrsKindGrouper implements Grouper {

    /** Identifies the toggle, and is what its state is remembered under. */
    public static final String ID = "CQRS_GROUP_BY_KIND";

    @Override
    public @NotNull Collection<Group> group(@NotNull AbstractTreeNode<?> parent,
                                            @NotNull Collection<TreeElement> children) {

        if (parent.getValue() instanceof KindGroup) {
            // Never inside our own group. The platform groups a node's children, then does the same for
            // each group it just made - and those are all of one kind, so grouping them again yields a
            // group of the same name holding the same things, without end.
            return List.of();
        }

        // Sorted by name, which is the order the groups are shown in: no curated list to keep in step
        // with the grammar, and a kind added later appears where its name says it should.
        final Map<String, List<TreeElement>> byKind = new TreeMap<>();
        for (final TreeElement child : children) {
            byKind.computeIfAbsent(kindOf(child), key -> new ArrayList<>()).add(child);
        }

        final Object owner = ownerOf(parent);
        final List<Group> groups = new ArrayList<>(byKind.size());
        for (final Map.Entry<String, List<TreeElement>> entry : byKind.entrySet()) {
            groups.add(new KindGroup(owner, entry.getKey(), entry.getValue()));
        }
        return groups;
    }

    @Override
    public @NotNull ActionPresentation getPresentation() {
        return new ActionPresentationData("Group by Kind", "Group declarations by what kind they are",
                AllIcons.Actions.GroupBy);
    }

    @Override
    public @NotNull String getName() {
        return ID;
    }

    /**
     * What the groups of this call belong to, which is what tells them from the same kinds elsewhere.
     *
     * <p>The declaration itself rather than the node wrapping it: a node is built afresh on every
     * rebuild, so a group keyed by one would never be the group it was a moment ago.</p>
     */
    private static Object ownerOf(AbstractTreeNode<?> parent) {
        final Object value = parent.getValue();
        if (value instanceof StructureViewTreeElement element) {
            return element.getValue();
        }
        return value;
    }

    /** The kind a tree element belongs to, or {@link CqrsKinds#OTHER} for anything not from this DSL. */
    private static String kindOf(TreeElement child) {
        if (child instanceof StructureViewTreeElement element
                && element.getValue() instanceof PsiElement psi) {
            return CqrsKinds.pluralOf(psi);
        }
        return CqrsKinds.OTHER;
    }

    /**
     * One kind, and the declarations of it.
     *
     * <p><b>Equal by name alone.</b> The tree matches the nodes of one build against the next to know
     * what to keep open and what is still selected, and a group is matched by the group object itself -
     * it wraps no PSI to be matched by. Comparing the members too would make every group differ from
     * its own previous self, because the elements are built afresh each time, and the view empties
     * itself as soon as it is rebuilt. Within one parent a name identifies a group, which is exactly
     * what this has to say.</p>
     */
    private static final class KindGroup implements Group {

        private final Object owner;

        private final String name;

        private final List<TreeElement> children;

        private KindGroup(Object owner, String name, List<TreeElement> children) {
            this.owner = owner;
            this.name = name;
            this.children = children;
        }

        @Override
        public @NotNull ItemPresentation getPresentation() {
            return new PresentationData(name, null, AllIcons.Nodes.Folder, null);
        }

        @Override
        public @NotNull Collection<TreeElement> getChildren() {
            return children;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof KindGroup other && name.equals(other.name)
                    && java.util.Objects.equals(owner, other.owner);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(owner, name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
