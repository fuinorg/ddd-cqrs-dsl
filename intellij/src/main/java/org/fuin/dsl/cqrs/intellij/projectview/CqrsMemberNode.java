package org.fuin.dsl.cqrs.intellij.projectview;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Queryable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.fuin.dsl.cqrs.intellij.CqrsIcons;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** One declaration of a {@code .cqrs} file, as a row of the Project view tree. */
public final class CqrsMemberNode extends ProjectViewNode<CqrsNamedElement> {

    public CqrsMemberNode(Project project, @NotNull CqrsNamedElement element, ViewSettings settings) {
        super(project, element, settings);
    }

    @Override
    public @NotNull Collection<? extends AbstractTreeNode<?>> getChildren() {
        final CqrsNamedElement element = getValue();
        if (element == null || !element.isValid()) {
            return Collections.emptyList();
        }
        return CqrsKindGroupNode.groupsOf(myProject, element, CqrsMembers.childrenOf(element), getSettings());
    }

    @Override
    protected void update(@NotNull PresentationData presentation) {
        final CqrsNamedElement element = getValue();
        if (element == null || !element.isValid()) {
            // The tree can outlive the PSI it was built from - a node whose element went away is
            // dropped rather than drawn half broken.
            setValue(null);
            return;
        }
        presentation.setPresentableText(element.getName());
        presentation.setIcon(CqrsIcons.forElement(element));
        // Deliberately no location string. The element's own ItemPresentation appends the file name,
        // which earns its place in go-to-symbol but is pure noise directly beneath that same file.
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        final CqrsNamedElement element = getValue();
        if (element == null || !element.isValid()) {
            return false;
        }
        final PsiFile containing = element.getContainingFile();
        return containing != null && file.equals(containing.getVirtualFile());
    }

    @Override
    public boolean canNavigate() {
        final CqrsNamedElement element = getValue();
        return element != null && element.isValid() && element.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        final CqrsNamedElement element = getValue();
        return element != null && element.isValid() && element.canNavigateToSource();
    }

    @Override
    public void navigate(boolean requestFocus) {
        final CqrsNamedElement element = getValue();
        if (element != null && element.isValid()) {
            element.navigate(requestFocus);
        }
    }

    /** Double-click opens the declaration rather than expanding the row. */
    @Override
    public boolean expandOnDoubleClick() {
        return false;
    }

    @Override
    public @Nullable String toTestString(@Nullable Queryable.PrintInfo printInfo) {
        final CqrsNamedElement element = getValue();
        return element != null && element.isValid() ? element.getName() : null;
    }
}
