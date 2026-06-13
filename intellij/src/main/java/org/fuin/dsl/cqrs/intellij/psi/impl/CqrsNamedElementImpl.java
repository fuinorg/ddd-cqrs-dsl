package org.fuin.dsl.cqrs.intellij.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.fuin.dsl.cqrs.intellij.CqrsIcons;
import org.fuin.dsl.cqrs.intellij.psi.CqrsElementFactory;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsQualifiedName;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

/**
 * Base class (mixin) for every generated named-declaration PSI implementation. It implements the
 * {@link CqrsNamedElement} contract directly (rather than via generated delegation) so that the
 * Grammar-Kit generator needs nothing on its classpath beyond the platform interfaces.
 */
public abstract class CqrsNamedElementImpl extends ASTWrapperPsiElement implements CqrsNamedElement {

    public CqrsNamedElementImpl(ASTNode node) {
        super(node);
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        // context/namespace are named by a (possibly dotted) qualified_name node
        CqrsQualifiedName qualified = PsiTreeUtil.getChildOfType(this, CqrsQualifiedName.class);
        if (qualified != null) {
            return qualified;
        }
        // every other declaration carries exactly one direct ID child: its name
        ASTNode id = getNode().findChildByType(CqrsTypes.ID);
        return id != null ? id.getPsi() : null;
    }

    @Override
    public @Nullable String getName() {
        PsiElement id = getNameIdentifier();
        return id != null ? id.getText() : null;
    }

    @Override
    public PsiElement setName(String newName) throws IncorrectOperationException {
        PsiElement id = getNameIdentifier();
        if (id != null) {
            PsiElement replacement = id instanceof CqrsQualifiedName
                    ? CqrsElementFactory.createQualifiedName(getProject(), newName)
                    : CqrsElementFactory.createIdentifier(getProject(), newName);
            if (replacement != null) {
                id.replace(replacement);
            }
        }
        return this;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            public @Nullable String getPresentableText() {
                return getName();
            }

            @Override
            public @Nullable String getLocationString() {
                return getContainingFile() != null ? getContainingFile().getName() : null;
            }

            @Override
            public @Nullable Icon getIcon(boolean unused) {
                return CqrsIcons.FILE;
            }
        };
    }
}
