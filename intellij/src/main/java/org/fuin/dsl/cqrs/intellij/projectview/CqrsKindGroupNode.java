package org.fuin.dsl.cqrs.intellij.projectview;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Queryable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.fuin.dsl.cqrs.intellij.CqrsIcons;
import org.fuin.dsl.cqrs.intellij.CqrsKinds;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * One kind of declaration, holding the declarations of that kind.
 *
 * <p>The Project view has no equivalent of the structure view's {@code Grouper}, so the level is a node
 * of its own. It holds the declarations rather than a PSI element, because a kind is not something the
 * model declares - it is how a reader wants to see what it declares.</p>
 */
public final class CqrsKindGroupNode extends ProjectViewNode<String> {

    private final PsiElement owner;

    private final List<CqrsNamedElement> members;

    public CqrsKindGroupNode(Project project, @NotNull PsiElement owner, @NotNull String kind,
            @NotNull List<CqrsNamedElement> members, ViewSettings settings) {
        super(project, kind, settings);
        this.owner = owner;
        this.members = members;
    }

    /**
     * Groups declarations by kind, in alphabetical order of the group name.
     *
     * <p>Shared by the file node and the member node, which is the whole of the Project view's tree:
     * whatever {@link CqrsMembers} decides is shown under something, this decides how it is arranged.
     * A group is emitted even for a single kind, so a kind sits at the same depth in every file.</p>
     *
     * @param project Project the nodes belong to.
     * @param owner File or declaration the members were found in, which is what tells one group from
     *            another of the same kind elsewhere.
     * @param members Declarations to arrange, in the order the model declares them.
     * @param settings View settings to pass on.
     *
     * @return One node per kind, empty when there is nothing to show.
     */
    public static @NotNull List<AbstractTreeNode<?>> groupsOf(Project project, @NotNull PsiElement owner,
            @NotNull List<CqrsNamedElement> members, ViewSettings settings) {

        final Map<String, List<CqrsNamedElement>> byKind = new TreeMap<>();
        for (final CqrsNamedElement member : members) {
            byKind.computeIfAbsent(CqrsKinds.pluralOf(member), key -> new ArrayList<>()).add(member);
        }

        final List<AbstractTreeNode<?>> groups = new ArrayList<>(byKind.size());
        for (final Map.Entry<String, List<CqrsNamedElement>> entry : byKind.entrySet()) {
            groups.add(new CqrsKindGroupNode(project, owner, entry.getKey(), entry.getValue(), settings));
        }
        return groups;
    }

    @Override
    public @NotNull Collection<? extends AbstractTreeNode<?>> getChildren() {
        final List<AbstractTreeNode<?>> children = new ArrayList<>(members.size());
        for (final CqrsNamedElement member : members) {
            if (member.isValid()) {
                children.add(new CqrsMemberNode(myProject, member, getSettings()));
            }
        }
        return children;
    }

    @Override
    protected void update(@NotNull PresentationData presentation) {
        presentation.setPresentableText(getValue());
        // The kind's own icon rather than a folder: the group says what its members are, so it may as
        // well look like them. Falls back to the file icon for a kind CqrsIcons has no entry for.
        presentation.setIcon(members.isEmpty() ? CqrsIcons.FILE : CqrsIcons.forElement(members.get(0)));
    }

    /**
     * The file this group belongs to, so a selection can be resolved to one.
     *
     * <p>A node whose value is not a {@code PsiElement} answers nothing by default, and everything that
     * asks "which file is selected" - the structure view among them - then has no answer and shows
     * nothing.</p>
     */
    @Override
    public @Nullable VirtualFile getVirtualFile() {
        final PsiFile containing = owner.isValid() ? owner.getContainingFile() : null;
        return containing == null ? null : containing.getVirtualFile();
    }

    /**
     * Equal to the same kind under the same owner, and to nothing else.
     *
     * <p>{@link AbstractTreeNode} compares nodes by their value alone, and this one's value is the name
     * of the kind - so without this every {@code exceptions} group in the project would be equal to
     * every other, and the tree would resolve a selection to whichever it found first.</p>
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof CqrsKindGroupNode other
                && Objects.equals(getValue(), other.getValue())
                && Objects.equals(owner, other.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), owner);
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        // Asked when the tree is looking for the row to select. A group is transparent: it contains
        // whatever its members do, which is the one file they were all declared in.
        for (final CqrsNamedElement member : members) {
            if (member.isValid()) {
                final PsiFile containing = member.getContainingFile();
                if (containing != null && file.equals(containing.getVirtualFile())) {
                    return true;
                }
            }
        }
        return false;
    }

    /** A group is a heading, not a place: opening it is expanding it. */
    @Override
    public boolean canNavigate() {
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        return false;
    }

    @Override
    public @Nullable String toTestString(@Nullable Queryable.PrintInfo printInfo) {
        return getValue();
    }
}
