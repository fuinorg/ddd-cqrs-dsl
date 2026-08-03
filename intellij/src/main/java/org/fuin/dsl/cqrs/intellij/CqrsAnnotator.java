package org.fuin.dsl.cqrs.intellij;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import org.fuin.dsl.cqrs.intellij.psi.CqrsReferenceElement;
import org.fuin.dsl.cqrs.intellij.remote.CqrsRemoteScopeResolver;
import org.jetbrains.annotations.NotNull;

/**
 * Reports cross-references that resolve to no declaration (local or remote).
 *
 * <p>Only in a model of this project. A model that is merely read - an entry of a dependency's
 * archive, or a file of a {@code local} directory outside the project - is reported on by whoever
 * authors it, not here: the reader cannot act on the message, and a model that publishes only part of
 * itself legitimately names types it kept to itself.</p>
 */
public final class CqrsAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof CqrsReferenceElement ref)) {
            return;
        }
        if (!CqrsRemoteScopeResolver.inProject(element.getContainingFile())) {
            return;
        }
        if (!(ref.getReference() instanceof PsiPolyVariantReference poly)) {
            return;
        }
        if (poly.multiResolve(false).length == 0) {
            holder.newAnnotation(HighlightSeverity.ERROR,
                            "Cannot resolve '" + ref.getReferencedName() + "'")
                    .range(element)
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                    .create();
        }
    }
}
