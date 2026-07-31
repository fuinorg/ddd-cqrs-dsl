package org.fuin.dsl.cqrs.intellij.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;

/**
 * A named, navigable declaration in the CQRS DSL (context, module, type, value-object,
 * entity, aggregate, event, exception, attribute, ...). Being a {@link PsiNameIdentifierOwner}
 * gives it rename, find-usages and go-to-symbol support.
 */
public interface CqrsNamedElement extends PsiNameIdentifierOwner, NavigatablePsiElement {
}
