package org.fuin.dsl.cqrs.intellij.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import org.fuin.dsl.cqrs.intellij.psi.CqrsElementFactory;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNames;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypeRef;
import org.jetbrains.annotations.NotNull;

/** Rewrites a {@code type_ref} when a referenced declaration is renamed. */
public final class CqrsTypeRefManipulator extends AbstractElementManipulator<CqrsTypeRef> {

    @Override
    public CqrsTypeRef handleContentChange(@NotNull CqrsTypeRef element, @NotNull TextRange range,
                                           String newContent) {
        String oldText = element.getText();
        // Re-add the caret escape if the new (last) segment would otherwise be a keyword.
        String updated = oldText.substring(0, range.getStartOffset())
                + CqrsNames.escape(newContent)
                + oldText.substring(range.getEndOffset());
        CqrsTypeRef replacement = CqrsElementFactory.createTypeRef(element.getProject(), updated);
        if (replacement == null) {
            return element;
        }
        return (CqrsTypeRef) element.replace(replacement);
    }
}
