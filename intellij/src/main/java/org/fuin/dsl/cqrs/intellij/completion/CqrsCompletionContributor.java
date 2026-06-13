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
import org.fuin.dsl.cqrs.intellij.CqrsLanguage;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstructorDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEnumObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamespaceDef;
import org.jetbrains.annotations.NotNull;

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
                        for (String keyword : keywordsFor(parameters.getPosition())) {
                            result.addElement(LookupElementBuilder.create(keyword).bold());
                        }
                    }
                });
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
