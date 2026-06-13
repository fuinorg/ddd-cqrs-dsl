package org.fuin.dsl.cqrs.intellij.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A cross-reference occurrence (the {@code type_ref} rule): a use of a name that should resolve
 * to a {@link CqrsNamedElement} declaration, possibly in a remote model.
 */
public interface CqrsReferenceElement extends PsiElement {

    /** The (possibly qualified) referenced name as written in the source. */
    @NotNull
    String getReferencedName();

    /** The reference resolving this occurrence to its declaration(s). */
    @Override
    @Nullable
    com.intellij.psi.PsiReference getReference();
}
