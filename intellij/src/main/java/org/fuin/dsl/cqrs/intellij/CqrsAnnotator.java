package org.fuin.dsl.cqrs.intellij;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import org.fuin.dsl.cqrs.intellij.psi.CqrsReferenceElement;
import org.jetbrains.annotations.NotNull;

/** Reports cross-references that resolve to no declaration (local or remote). */
public final class CqrsAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof CqrsReferenceElement ref)) {
            return;
        }
        if (!(ref.getReference() instanceof PsiPolyVariantReference poly)) {
            return;
        }
        if (poly.multiResolve(false).length == 0) {
            holder.newAnnotation(HighlightSeverity.WEAK_WARNING,
                            "Cannot resolve '" + ref.getReferencedName() + "'")
                    .range(element)
                    .create();
        }
    }
}
