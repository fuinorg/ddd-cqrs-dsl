package org.fuin.dsl.cqrs.intellij.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
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
import org.fuin.dsl.cqrs.intellij.psi.CqrsDataProtectionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEnumObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEventDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsExceptionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNames;
import org.fuin.dsl.cqrs.intellij.psi.CqrsContextDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamespaceDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProjectDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
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
            "annotation", "service", "data-protection");

    private static final List<String> META_KEYWORDS = List.of(
            "slabel", "label", "tooltip", "prompt", "examples");

    // ---- data-protection block: clause keywords and their enum values --------------------
    private static final List<String> DP_CLAUSE_KEYWORDS = List.of(
            "protection", "category", "subject", "purpose", "lawful-basis", "retention");
    private static final List<String> PROTECTION_LEVELS = List.of("none", "personal", "sensitive");
    private static final List<String> LAWFUL_BASES = List.of(
            "consent", "explicit_consent", "contract", "legal_obligation",
            "vital_interests", "public_task", "legitimate_interests");
    private static final List<String> SPECIAL_CATEGORIES = List.of(
            "health", "genetic", "biometric", "racial", "political", "religious",
            "philosophical", "trade_union", "sex_life", "sexual_orientation");
    private static final List<String> ERASURE_STRATEGIES = List.of(
            "delete", "anonymize", "pseudonymize", "archive", "review");
    private static final List<String> TIME_UNITS = List.of(
            "millis", "seconds", "minutes", "hours", "days", "weeks", "months", "years");
    private static final Set<IElementType> TIME_UNIT_TOKENS = Set.of(
            CqrsTypes.KW_MILLIS, CqrsTypes.KW_SECONDS, CqrsTypes.KW_MINUTES, CqrsTypes.KW_HOURS,
            CqrsTypes.KW_DAYS, CqrsTypes.KW_WEEKS, CqrsTypes.KW_MONTHS, CqrsTypes.KW_YEARS);

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

        // A 'data-protection { ... }' block is nested inside a namespace (or an entity/aggregate),
        // so it must be checked first; inside it we offer the clause keywords and, right after a
        // clause keyword, its enum values.
        if (enclosingDataProtection(position) != null) {
            return dataProtectionKeywords(position);
        }

        CqrsConstructorDef ctor = PsiTreeUtil.getParentOfType(position, CqrsConstructorDef.class);
        CqrsMethodDef method = PsiTreeUtil.getParentOfType(position, CqrsMethodDef.class);
        if (ctor != null || method != null) {
            keywords.add("optional");
            keywords.add("event");
            if (method != null) {
                keywords.add("returns");
            }
            return keywords;
        }

        if (PsiTreeUtil.getParentOfType(position, CqrsAggregateDef.class) != null
                || PsiTreeUtil.getParentOfType(position, CqrsEntityDef.class) != null) {
            keywords.addAll(META_KEYWORDS);
            keywords.add("optional");
            keywords.add("protected-by");
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
            keywords.add("optional");
            keywords.add("protected-by");
            keywords.add("base");
            keywords.add("message");
            keywords.add("constructor");
            keywords.add("method");
            return keywords;
        }

        // inside a context (but not a namespace)
        if (PsiTreeUtil.getParentOfType(position, CqrsContextDef.class) != null) {
            keywords.add("namespace");
            return keywords;
        }

        // inside a project (but not a context)
        if (PsiTreeUtil.getParentOfType(position, CqrsProjectDef.class) != null) {
            keywords.add("hint");
            keywords.add("context");
            return keywords;
        }

        // top level
        keywords.add("project");
        return keywords;
    }

    /**
     * The {@code data-protection} block surrounding the caret, or {@code null}. While the block is
     * being edited the value after a clause keyword is often a parse error, so the caret token can
     * land just outside the pinned {@code data_protection_def} node; we then retry from the previous
     * visible leaf (part of the well-formed prefix). A previous leaf of {@code &#125;} means the
     * block is already closed and the caret is back at the enclosing level, so it is excluded.
     */
    private static CqrsDataProtectionDef enclosingDataProtection(PsiElement position) {
        CqrsDataProtectionDef dp = PsiTreeUtil.getParentOfType(position, CqrsDataProtectionDef.class);
        if (dp != null) {
            return dp;
        }
        PsiElement prev = PsiTreeUtil.prevVisibleLeaf(position);
        if (prev == null || prev.getNode().getElementType() == CqrsTypes.RBRACE) {
            return null;
        }
        return PsiTreeUtil.getParentOfType(prev, CqrsDataProtectionDef.class);
    }

    /**
     * Completion inside a {@code data-protection { ... }} block. The token immediately before the
     * caret decides what to offer: the value set of the clause just named (e.g. after
     * {@code protection} → the protection levels), or otherwise the clause keywords themselves.
     */
    private static Set<String> dataProtectionKeywords(PsiElement position) {
        Set<String> keywords = new LinkedHashSet<>();
        PsiElement prev = PsiTreeUtil.prevVisibleLeaf(position);
        IElementType type = prev == null ? null : prev.getNode().getElementType();

        if (type == CqrsTypes.KW_DATA_PROTECTION) {
            return keywords; // user is still typing the policy name
        }
        if (type == CqrsTypes.KW_PROTECTION) {
            keywords.addAll(PROTECTION_LEVELS);
        } else if (type == CqrsTypes.KW_LAWFUL_BASIS) {
            keywords.addAll(LAWFUL_BASES);
        } else if (type == CqrsTypes.KW_CATEGORY || type == CqrsTypes.COMMA) {
            keywords.addAll(SPECIAL_CATEGORIES);
        } else if (type == CqrsTypes.KW_THEN) {
            keywords.addAll(ERASURE_STRATEGIES);
        } else if (type == CqrsTypes.NUMBER) {
            keywords.addAll(TIME_UNITS); // 'retention <number> <unit>'
        } else if (TIME_UNIT_TOKENS.contains(type)) {
            keywords.add("then"); // optional erasure clause after the retention duration
            keywords.addAll(DP_CLAUSE_KEYWORDS);
        } else {
            keywords.addAll(DP_CLAUSE_KEYWORDS);
        }
        return keywords;
    }
}
