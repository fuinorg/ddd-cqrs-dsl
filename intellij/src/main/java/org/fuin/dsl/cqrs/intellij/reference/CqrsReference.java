package org.fuin.dsl.cqrs.intellij.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import org.fuin.dsl.cqrs.intellij.CqrsIcons;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNames;
import org.fuin.dsl.cqrs.intellij.psi.CqrsReferenceElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Resolves a {@code type_ref} occurrence to its declaration(s). Poly-variant because a lenient
 * simple-name match can hit several same-named declarations across modules.
 */
public final class CqrsReference extends PsiReferenceBase<CqrsReferenceElement>
        implements PsiPolyVariantReference {

    public CqrsReference(@NotNull CqrsReferenceElement element) {
        super(element, lastSegmentRange(element));
    }

    /** Only the last {@code .}-segment is the rename target; resolution still uses the full text. */
    private static TextRange lastSegmentRange(CqrsReferenceElement element) {
        String text = element.getText();
        int dot = text.lastIndexOf('.');
        int start = dot < 0 ? 0 : dot + 1;
        return new TextRange(start, text.length());
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        List<CqrsNamedElement> targets =
                CqrsResolveUtil.resolve(getElement(), getElement().getReferencedName());
        List<ResolveResult> results = new ArrayList<>(targets.size());
        for (CqrsNamedElement target : targets) {
            results.add(new PsiElementResolveResult(target));
        }
        return results.toArray(ResolveResult.EMPTY_ARRAY);
    }

    @Override
    public @Nullable PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length == 1 ? results[0].getElement() : null;
    }

    @Override
    public Object @NotNull [] getVariants() {
        List<LookupElement> variants = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (CqrsNamedElement decl : CqrsResolveUtil.referenceableDeclarations(getElement())) {
            String name = decl.getName();
            if (name == null || !seen.add(name)) {
                continue;
            }
            // Insert the caret-escaped form when the name is a keyword, but show the plain name.
            variants.add(LookupElementBuilder.create(decl, CqrsNames.escape(name))
                    .withPresentableText(name)
                    .withIcon(CqrsIcons.FILE)
                    .withTypeText(CqrsResolveUtil.getQualifiedName(decl), true));
        }
        return variants.toArray();
    }
}
