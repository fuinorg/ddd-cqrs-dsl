package org.fuin.dsl.cqrs.intellij;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

/** PSI file for a {@code .cqrs} document. */
public final class CqrsFile extends PsiFileBase {

    public CqrsFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, CqrsLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return CqrsFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "CQRS DSL file";
    }
}
