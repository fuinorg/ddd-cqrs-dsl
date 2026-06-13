package org.fuin.dsl.cqrs.intellij.structure;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/** A node in the CQRS DSL structure view (the file or a named declaration). */
public final class CqrsStructureViewElement implements StructureViewTreeElement, SortableTreeElement {

    private final PsiElement element;

    public CqrsStructureViewElement(PsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (element instanceof Navigatable navigatable) {
            navigatable.navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return element instanceof Navigatable navigatable && navigatable.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element instanceof Navigatable navigatable && navigatable.canNavigateToSource();
    }

    @Override
    public @NotNull String getAlphaSortKey() {
        String name = element instanceof PsiNamedElement named ? named.getName() : null;
        return name != null ? name : "";
    }

    @Override
    public @NotNull ItemPresentation getPresentation() {
        if (element instanceof NavigationItem item) {
            ItemPresentation presentation = item.getPresentation();
            if (presentation != null) {
                return presentation;
            }
        }
        return new PresentationData(element.getContainingFile() != null
                ? element.getContainingFile().getName() : "", null, null, null);
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        List<TreeElement> children = new ArrayList<>();
        for (CqrsNamedElement named : PsiTreeUtil.findChildrenOfType(element, CqrsNamedElement.class)) {
            if (nearestNamedAncestor(named) == element) {
                children.add(new CqrsStructureViewElement(named));
            }
        }
        return children.toArray(TreeElement.EMPTY_ARRAY);
    }

    private PsiElement nearestNamedAncestor(CqrsNamedElement named) {
        for (PsiElement cur = named.getParent(); cur != null; cur = cur.getParent()) {
            if (cur instanceof CqrsNamedElement || cur instanceof CqrsFile) {
                return cur;
            }
        }
        return null;
    }
}
