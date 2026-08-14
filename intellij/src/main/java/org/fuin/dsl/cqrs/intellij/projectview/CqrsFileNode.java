package org.fuin.dsl.cqrs.intellij.projectview;

import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.FileNodeWithNestedFileNodes;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A {@code .cqrs} file in the Project view that can be expanded into the declarations it holds.
 *
 * <p>The platform's own {@link PsiFileNode} answers with no children for an ordinary file - it only
 * descends into an archive root - and it consults no structure view, so "show members" does nothing for
 * a language on its own. Java solves this the same way, by swapping the file node for one that knows how
 * to expand ({@code ClassesTreeStructureProvider}).</p>
 */
public final class CqrsFileNode extends PsiFileNode implements FileNodeWithNestedFileNodes {

    private final Collection<? extends AbstractTreeNode<?>> nestedFileNodes;

    public CqrsFileNode(Project project, @NotNull PsiFile file, ViewSettings settings,
            Collection<? extends AbstractTreeNode<?>> nestedFileNodes) {
        super(project, file, settings);
        this.nestedFileNodes = nestedFileNodes == null ? List.of() : nestedFileNodes;
    }

    @Override
    public @NotNull Collection<? extends AbstractTreeNode<?>> getNestedFileNodes() {
        return nestedFileNodes;
    }

    @Override
    public @NotNull Collection<AbstractTreeNode<?>> getChildrenImpl() {
        final List<AbstractTreeNode<?>> children = new ArrayList<>(nestedFileNodes);
        final PsiFile file = getValue();
        if (file != null && file.isValid()) {
            for (CqrsNamedElement child : CqrsMembers.childrenOf(file)) {
                children.add(new CqrsMemberNode(myProject, child, getSettings()));
            }
        }
        return children;
    }

    /**
     * Draws the expand handle without being asked for the children first.
     *
     * <p>This language has no stubs, so listing the declarations means parsing the file. Letting the
     * tree ask "are you a leaf?" would parse every model in a directory just to render the directory.
     * The handle is promised up front instead, and the parse happens when somebody actually expands.
     * The price is that an empty model shows a handle that opens onto nothing.</p>
     */
    @Override
    public boolean isAlwaysShowPlus() {
        return true;
    }

    /** Double-click opens the file, as on any other file row, rather than expanding it. */
    @Override
    public boolean expandOnDoubleClick() {
        return false;
    }
}
