package org.fuin.dsl.cqrs.intellij.psi;

import com.intellij.psi.tree.IElementType;
import org.fuin.dsl.cqrs.intellij.CqrsLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/** Composite element type for CQRS DSL PSI nodes. */
public final class CqrsElementType extends IElementType {

    public CqrsElementType(@NotNull @NonNls String debugName) {
        super(debugName, CqrsLanguage.INSTANCE);
    }
}
