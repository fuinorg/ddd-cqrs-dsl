package org.fuin.dsl.cqrs.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNames;
import org.fuin.dsl.cqrs.intellij.psi.CqrsReferenceElement;
import org.fuin.dsl.cqrs.intellij.reference.CqrsReference;
import org.jetbrains.annotations.NotNull;

/**
 * Base class (mixin) for the generated {@code type_ref} PSI implementation. Implements the
 * {@link CqrsReferenceElement} contract directly so no generator-side delegation is required.
 */
public abstract class CqrsReferenceElementImpl extends ASTWrapperPsiElement implements CqrsReferenceElement {

    public CqrsReferenceElementImpl(ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull String getReferencedName() {
        // Strip whitespace and the caret keyword-escape so the name matches the declaration.
        return CqrsNames.unescapeQualified(getText().replaceAll("\\s+", ""));
    }

    @Override
    public PsiReference getReference() {
        return new CqrsReference(this);
    }

    /** Expose the single reference through {@code getReferences()} so {@code findReferenceAt} sees it. */
    @Override
    public PsiReference @NotNull [] getReferences() {
        PsiReference reference = getReference();
        return reference == null ? PsiReference.EMPTY_ARRAY : new PsiReference[]{reference};
    }
}
