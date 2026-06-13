package org.fuin.dsl.cqrs.intellij.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import org.fuin.dsl.cqrs.intellij.psi.CqrsElementFactory;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypeRef;
import org.jetbrains.annotations.NotNull;

/** Rewrites a {@code type_ref} when a referenced declaration is renamed. */
public final class CqrsTypeRefManipulator extends AbstractElementManipulator<CqrsTypeRef> {

    @Override
    public CqrsTypeRef handleContentChange(@NotNull CqrsTypeRef element, @NotNull TextRange range,
                                           String newContent) {
        String oldText = element.getText();
        String updated = oldText.substring(0, range.getStartOffset())
                + newContent
                + oldText.substring(range.getEndOffset());
        CqrsTypeRef replacement = CqrsElementFactory.createTypeRef(element.getProject(), updated);
        if (replacement == null) {
            return element;
        }
        return (CqrsTypeRef) element.replace(replacement);
    }
}
