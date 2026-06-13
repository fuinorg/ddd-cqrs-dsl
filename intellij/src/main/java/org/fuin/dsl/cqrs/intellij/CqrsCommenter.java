package org.fuin.dsl.cqrs.intellij;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/** Enables line ({@code //}) and block ({@code /* *}{@code /}) commenting actions. */
public final class CqrsCommenter implements Commenter {

    @Override
    public @Nullable String getLineCommentPrefix() {
        return "//";
    }

    @Override
    public @Nullable String getBlockCommentPrefix() {
        return "/*";
    }

    @Override
    public @Nullable String getBlockCommentSuffix() {
        return "*/";
    }

    @Override
    public @Nullable String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Override
    public @Nullable String getCommentedBlockCommentSuffix() {
        return null;
    }
}
