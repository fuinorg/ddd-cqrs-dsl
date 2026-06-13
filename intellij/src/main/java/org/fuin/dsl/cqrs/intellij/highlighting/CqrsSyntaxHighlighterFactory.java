package org.fuin.dsl.cqrs.intellij.highlighting;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Supplies the {@link CqrsSyntaxHighlighter} for {@code .cqrs} files. */
public final class CqrsSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    @Override
    public @NotNull SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project,
                                                           @Nullable VirtualFile virtualFile) {
        return new CqrsSyntaxHighlighter();
    }
}
