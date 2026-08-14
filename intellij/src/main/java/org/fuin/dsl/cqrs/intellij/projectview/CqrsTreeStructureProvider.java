package org.fuin.dsl.cqrs.intellij.projectview;

import com.intellij.ide.projectView.SelectableTreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

/**
 * Lets a {@code .cqrs} file be expanded in the Project view when "show members" is on, the way a Java
 * file can be.
 *
 * <p>Every {@link PsiFileNode} over a CQRS file is swapped for a {@link CqrsFileNode}, which knows how
 * to list the declarations inside. Replacing the node is the only way in: {@code modify} returns the
 * children <em>of its parent</em>, and {@link PsiFileNode} answers with no children for anything that is
 * not an archive root. Java does the same with {@code ClassesTreeStructureProvider}.</p>
 *
 * <p>Nothing else about the tree is touched, and with the setting off the children are handed back as
 * they came, so an ordinary directory expansion costs one {@code instanceof} per file.</p>
 */
public final class CqrsTreeStructureProvider implements SelectableTreeStructureProvider, DumbAware {

    @Override
    public @NotNull Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent,
            @NotNull Collection<AbstractTreeNode<?>> children, ViewSettings settings) {

        // "Show members" lives on ViewSettings itself, not on ProjectViewSettings - narrowing to the
        // latter would quietly turn the feature off in every pane that passes something else.
        if (settings == null || !settings.isShowMembers()) {
            return children;
        }

        ArrayList<AbstractTreeNode<?>> result = null;
        for (AbstractTreeNode<?> child : children) {
            ProgressManager.checkCanceled();
            final CqrsFileNode replacement = replacementFor(child, settings);
            if (replacement == null) {
                if (result != null) {
                    result.add(child);
                }
                continue;
            }
            if (result == null) {
                // Most directories hold no model at all, so the copy is only made once one turns up.
                result = new ArrayList<>(children.size());
                for (AbstractTreeNode<?> seen : children) {
                    if (seen == child) {
                        break;
                    }
                    result.add(seen);
                }
            }
            result.add(replacement);
        }
        return result != null ? result : children;
    }

    /**
     * Where "select in project view" and autoscroll-from-source should land: the innermost declaration
     * around the caret that the tree actually draws a row for. Without this the selection stops at the
     * file, which is the thing the caret had already left.
     *
     * @param element Element at the caret.
     *
     * @return Declaration to select, the file itself when the caret sits outside every declaration, or
     *         <code>null</code> for a file this provider knows nothing about.
     */
    @Override
    public @Nullable PsiElement getTopLevelElement(@NotNull PsiElement element) {
        final PsiFile file = element.getContainingFile();
        if (!(file instanceof CqrsFile)) {
            return null;
        }
        final Deque<CqrsNamedElement> enclosing = new ArrayDeque<>();
        for (PsiElement cur = element; cur != null && !(cur instanceof PsiFile); cur = cur.getParent()) {
            if (cur instanceof CqrsNamedElement named) {
                enclosing.addFirst(named);
            }
        }
        PsiElement selected = file;
        for (CqrsNamedElement candidate : enclosing) {
            if (!CqrsMembers.isShownAnywhere(candidate)) {
                break;
            }
            selected = candidate;
            if (CqrsMembers.isLeaf(candidate)) {
                break;
            }
        }
        return selected;
    }

    /** The expandable node for a CQRS file node, or <code>null</code> for anything else. */
    private CqrsFileNode replacementFor(AbstractTreeNode<?> node, ViewSettings settings) {
        if (!(node instanceof PsiFileNode fileNode) || node instanceof CqrsFileNode) {
            return null;
        }
        final PsiFile file = fileNode.getValue();
        if (!(file instanceof CqrsFile) || !file.isValid()) {
            return null;
        }
        // A file node may be carrying others nested beneath it (the "file nesting" feature). Replacing
        // it without taking those along would make them disappear from the tree.
        final Collection<? extends AbstractTreeNode<?>> nested = nestedNodesOf(node);
        return new CqrsFileNode(fileNode.getProject(), file, settings, nested);
    }

    private Collection<? extends AbstractTreeNode<?>> nestedNodesOf(AbstractTreeNode<?> node) {
        if (node instanceof com.intellij.ide.projectView.impl.nodes.FileNodeWithNestedFileNodes nesting) {
            return nesting.getNestedFileNodes();
        }
        return List.of();
    }
}
