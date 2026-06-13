package org.fuin.dsl.cqrs.intellij.psi;

import com.intellij.psi.tree.IElementType;
import org.fuin.dsl.cqrs.intellij.CqrsLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/** Token type for the CQRS DSL (terminals and keywords). */
public final class CqrsTokenType extends IElementType {

    public CqrsTokenType(@NotNull @NonNls String debugName) {
        super(debugName, CqrsLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "CqrsTokenType." + super.toString();
    }
}
