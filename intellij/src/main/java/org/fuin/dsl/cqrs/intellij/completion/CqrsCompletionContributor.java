package org.fuin.dsl.cqrs.intellij.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.fuin.dsl.cqrs.intellij.CqrsIcons;
import org.fuin.dsl.cqrs.intellij.CqrsLanguage;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAnnotationDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsCommandDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstraintDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstructorDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEnumObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEventDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsExceptionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNames;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamespaceDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsValueObject;
import org.fuin.dsl.cqrs.intellij.reference.CqrsResolveUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Context-aware keyword completion. Declared-name completion is provided by the reference's
 * {@code getVariants()} (see {@link org.fuin.dsl.cqrs.intellij.reference.CqrsReference}); this
 * contributor adds the relevant DSL keywords for the block surrounding the caret.
 */
public final class CqrsCompletionContributor extends CompletionContributor {

    private static final List<String> ELEMENT_KEYWORDS = List.of(
            "type", "value-object", "entity-id", "aggregate-id", "enum", "entity", "aggregate",
            "exception", "event", "command", "command-handler", "projection", "view", "constraint",
            "annotation", "service");

    private static final List<String> META_KEYWORDS = List.of(
            "slabel", "label", "tooltip", "prompt", "examples");

    public CqrsCompletionContributor() {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(CqrsLanguage.INSTANCE),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters,
                                                  @NotNull ProcessingContext context,
                                                  @NotNull CompletionResultSet result) {
                        PsiElement position = parameters.getPosition();
                        for (String keyword : keywordsFor(position)) {
                            result.addElement(LookupElementBuilder.create(keyword).bold());
                        }
                        // At the start of a new attribute or parameter the half-typed identifier is
                        // not yet parsed as a type_ref, so the reference's getVariants() never runs.
                        // Offer the visible type declarations here so types show up alongside keywords.
                        if (allowsTypeRef(position)) {
                            addTypeVariants(position, result);
                        }
                    }
                });
    }

    /** Whether a {@code type_ref} (attribute/parameter type) may legally begin at the caret. */
    private static boolean allowsTypeRef(PsiElement position) {
        return typeMemberContainer(position) != null;
    }

    /**
     * The type-declaring block surrounding the caret, or {@code null}. A half-typed attribute is
     * just one identifier (the type, no name yet), so {@code attribute} (pinned only after its name
     * ID) rolls back and the caret token can land outside the block node via error recovery. When
     * the direct ancestor lookup fails we therefore retry from the previous visible leaf, which is
     * part of the well-formed prefix (the block's {@code &#123;} or a prior member) and so is still
     * inside the block node.
     */
    private static PsiElement typeMemberContainer(PsiElement position) {
        PsiElement container = enclosingContainer(position);
        if (container != null) {
            return container;
        }
        PsiElement prev = PsiTreeUtil.prevVisibleLeaf(position);
        return prev == null ? null : enclosingContainer(prev);
    }

    private static PsiElement enclosingContainer(PsiElement element) {
        return PsiTreeUtil.getParentOfType(element,
                CqrsConstructorDef.class, CqrsMethodDef.class,
                CqrsValueObject.class, CqrsEntityId.class, CqrsAggregateId.class,
                CqrsEnumObject.class, CqrsEntityDef.class, CqrsAggregateDef.class,
                CqrsConstraintDef.class, CqrsAnnotationDef.class, CqrsExceptionDef.class,
                CqrsEventDef.class, CqrsCommandDef.class);
    }

    /** Adds every visible named declaration as a type candidate (mirrors {@code CqrsReference.getVariants}). */
    private static void addTypeVariants(PsiElement position, @NotNull CompletionResultSet result) {
        Set<String> seen = new HashSet<>();
        for (CqrsNamedElement decl : CqrsResolveUtil.visibleDeclarations(position)) {
            String name = decl.getName();
            if (name == null || !seen.add(name)) {
                continue;
            }
            // Insert the caret-escaped form when the name is a keyword, but show the plain name.
            result.addElement(LookupElementBuilder.create(decl, CqrsNames.escape(name))
                    .withPresentableText(name)
                    .withIcon(CqrsIcons.FILE)
                    .withTypeText(CqrsResolveUtil.getQualifiedName(decl), true));
        }
    }

    private static Set<String> keywordsFor(PsiElement position) {
        Set<String> keywords = new LinkedHashSet<>();

        CqrsConstructorDef ctor = PsiTreeUtil.getParentOfType(position, CqrsConstructorDef.class);
        CqrsMethodDef method = PsiTreeUtil.getParentOfType(position, CqrsMethodDef.class);
        if (ctor != null || method != null) {
            keywords.add("nullable");
            keywords.add("event");
            if (method != null) {
                keywords.add("returns");
            }
            return keywords;
        }

        if (PsiTreeUtil.getParentOfType(position, CqrsAggregateDef.class) != null
                || PsiTreeUtil.getParentOfType(position, CqrsEntityDef.class) != null) {
            keywords.addAll(META_KEYWORDS);
            keywords.add("nullable");
            keywords.add("business-rule");
            keywords.add("constructor");
            keywords.add("method");
            keywords.add("event");
            return keywords;
        }

        if (PsiTreeUtil.getParentOfType(position, CqrsEnumObject.class) != null) {
            keywords.addAll(META_KEYWORDS);
            keywords.add("instances");
            keywords.add("deprecated");
            return keywords;
        }

        if (PsiTreeUtil.getParentOfType(position, CqrsNamespaceDef.class) != null) {
            keywords.add("import");
            keywords.addAll(ELEMENT_KEYWORDS);
            keywords.addAll(META_KEYWORDS);
            keywords.add("nullable");
            keywords.add("base");
            keywords.add("message");
            keywords.add("constructor");
            keywords.add("method");
            return keywords;
        }

        // top level
        keywords.add("context");
        keywords.add("namespace");
        return keywords;
    }
}
