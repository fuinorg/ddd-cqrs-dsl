package org.fuin.dsl.cqrs.intellij;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

/** File type for {@code .cqrs} DSL files. */
public final class CqrsFileType extends LanguageFileType {

    public static final CqrsFileType INSTANCE = new CqrsFileType();

    public static final String DEFAULT_EXTENSION = "cqrs";

    private CqrsFileType() {
        super(CqrsLanguage.INSTANCE);
    }

    @Override
    public @NotNull String getName() {
        return "CQRS DSL";
    }

    @Override
    public @NotNull String getDescription() {
        return "DDD/CQRS domain model definition";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override
    public Icon getIcon() {
        return CqrsIcons.FILE;
    }
}
