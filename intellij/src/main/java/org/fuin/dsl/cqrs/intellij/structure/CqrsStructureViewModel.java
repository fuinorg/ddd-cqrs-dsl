package org.fuin.dsl.cqrs.intellij.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Structure-view model: file → contexts → modules → elements. */
public final class CqrsStructureViewModel extends StructureViewModelBase
        implements StructureViewModel.ElementInfoProvider {

    private final Grouper grouper = new CqrsKindGrouper();

    public CqrsStructureViewModel(@Nullable PsiFile psiFile) {
        super(psiFile, new CqrsStructureViewElement(psiFile));
    }

    @Override
    public Sorter @NotNull [] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @Override
    public Grouper @NotNull [] getGroupers() {
        // One instance for the life of the model. The toolbar remembers which actions are on by the
        // object it was handed, so a fresh grouper on every call is a different action every time.
        return new Grouper[]{grouper};
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return false;
    }
}
