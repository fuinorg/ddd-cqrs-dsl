package org.fuin.dsl.cqrs.intellij.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
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
import org.fuin.dsl.cqrs.intellij.psi.CqrsProcessManager;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProcessReaction;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProjectDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.fuin.dsl.cqrs.intellij.psi.CqrsValueObject;
import org.fuin.dsl.cqrs.intellij.psi.CqrsViewDef;
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
            "exception", "event", "command", "command-handler", "projection", "view", "process-manager",
            "constraint", "annotation", "service", "data-protection");

    // ---- process-manager block: manager clauses and reaction clauses ---------------------
    private static final List<String> PM_BODY_KEYWORDS = List.of(
            "cron-schedule", "instance-key", "process-states", "reacts-to");
    private static final List<String> PM_REACTION_KEYWORDS = List.of(
            "correlate-by", "issues-commands", "transition-to", "arm-timeout", "cancel-timeout");

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
                // 'rest-path' is a header clause of a *view* method (before the '{'): it is an error
                // on any other method, and inside the body it would be invalid syntax.
                if (enclosingView(position) != null && beforeBody(method, position)) {
                    keywords.add("rest-path");
                }
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

        // A 'process-manager { ... }' block (and its 'reacts-to { ... }' reactions) is nested inside a
        // namespace/context, so it must be checked before them - a caret in a reaction body is offered
        // the reaction clauses; elsewhere in the manager body, the manager clauses.
        if (enclosingProcessManager(position) != null) {
            return processManagerKeywords(position);
        }

        // A 'view ... { ... }' body holds business rules and methods (a caret inside a view method is
        // already handled by the constructor/method branch above).
        CqrsViewDef view = enclosingView(position);
        if (view != null) {
            // 'rest-path' sits in the view header (before the '{'), everything else in the body.
            if (beforeBody(view, position)) {
                keywords.add("rest-path");
                return keywords;
            }
            keywords.add("hint");
            keywords.add("cron-schedule");
            keywords.add("business-rule");
            keywords.add("method");
            return keywords;
        }

        if (enclosingNamespace(position) != null) {
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

        // inside a context (but not a namespace): a context accepts a 'namespace' block and/or
        // imports/elements directly, mixed as siblings (the same content a namespace holds).
        if (enclosingContext(position) != null) {
            keywords.add("namespace");
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
    /**
     * Completion inside a {@code process-manager { ... }} block. Inside a {@code reacts-to} reaction
     * body the reaction clauses are offered (and, right after a duration number, the time units for
     * {@code arm-timeout}); before the reaction's brace only {@code in-state} applies. Elsewhere in
     * the manager body the manager clauses are offered.
     */
    private static Set<String> processManagerKeywords(PsiElement position) {
        Set<String> keywords = new LinkedHashSet<>();
        CqrsProcessReaction reaction = enclosingProcessReaction(position);
        if (reaction != null) {
            ASTNode lbrace = reaction.getNode().findChildByType(CqrsTypes.LBRACE);
            boolean inBody = lbrace != null && lbrace.getStartOffset() < position.getTextOffset();
            if (!inBody) {
                keywords.add("in-state"); // still in the 'reacts-to <Event> ...' header
                return keywords;
            }
            PsiElement prev = PsiTreeUtil.prevVisibleLeaf(position);
            IElementType type = prev == null ? null : prev.getNode().getElementType();
            if (type == CqrsTypes.NUMBER) {
                keywords.addAll(TIME_UNITS); // 'arm-timeout <number> <unit>'
            } else {
                keywords.addAll(PM_REACTION_KEYWORDS);
            }
            return keywords;
        }
        keywords.addAll(PM_BODY_KEYWORDS);
        return keywords;
    }

    /**
     * The {@code process-manager} block surrounding the caret, or {@code null}. Mirrors
     * {@link #enclosingDataProtection}: when the caret token lands just outside the pinned node via
     * error recovery, it retries from the previous visible leaf, excluding a closing {@code &#125;}
     * (which means the block is already closed and the caret is back at the enclosing level).
     */
    private static CqrsProcessManager enclosingProcessManager(PsiElement position) {
        CqrsProcessManager pm = PsiTreeUtil.getParentOfType(position, CqrsProcessManager.class);
        if (pm != null) {
            return pm;
        }
        PsiElement prev = PsiTreeUtil.prevVisibleLeaf(position);
        if (prev == null || prev.getNode().getElementType() == CqrsTypes.RBRACE) {
            return null;
        }
        return PsiTreeUtil.getParentOfType(prev, CqrsProcessManager.class);
    }

    /**
     * Determines whether the caret sits in the header of the given element - that is, before its
     * opening <code>&#123;</code>. Header clauses such as {@code rest-path} are only legal there.
     * An element whose body brace is missing (still being typed) counts as "header".
     */
    private static boolean beforeBody(PsiElement element, PsiElement position) {
        PsiElement brace = null;
        for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNode() != null && child.getNode().getElementType() == CqrsTypes.LBRACE) {
                brace = child;
                break;
            }
        }
        return brace == null || position.getTextOffset() <= brace.getTextOffset();
    }

    /** The {@code view} block surrounding the caret, or {@code null} (same error recovery as above). */
    private static CqrsViewDef enclosingView(PsiElement position) {
        CqrsViewDef view = PsiTreeUtil.getParentOfType(position, CqrsViewDef.class);
        if (view != null) {
            return view;
        }
        PsiElement prev = PsiTreeUtil.prevVisibleLeaf(position);
        if (prev == null || prev.getNode().getElementType() == CqrsTypes.RBRACE) {
            return null;
        }
        return PsiTreeUtil.getParentOfType(prev, CqrsViewDef.class);
    }

    /**
     * The {@code context} block surrounding the caret, or {@code null} (same error recovery as above).
     * A context is a pinned block, so a caret in an otherwise-empty context lands just outside the
     * pinned node; we retry from the previous visible leaf, excluding a closing {@code &#125;}.
     */
    private static CqrsContextDef enclosingContext(PsiElement position) {
        CqrsContextDef ctx = PsiTreeUtil.getParentOfType(position, CqrsContextDef.class);
        if (ctx != null) {
            return ctx;
        }
        PsiElement prev = PsiTreeUtil.prevVisibleLeaf(position);
        if (prev == null || prev.getNode().getElementType() == CqrsTypes.RBRACE) {
            return null;
        }
        return PsiTreeUtil.getParentOfType(prev, CqrsContextDef.class);
    }

    /** The {@code namespace} block surrounding the caret, or {@code null} (same recovery as above). */
    private static CqrsNamespaceDef enclosingNamespace(PsiElement position) {
        CqrsNamespaceDef ns = PsiTreeUtil.getParentOfType(position, CqrsNamespaceDef.class);
        if (ns != null) {
            return ns;
        }
        PsiElement prev = PsiTreeUtil.prevVisibleLeaf(position);
        if (prev == null || prev.getNode().getElementType() == CqrsTypes.RBRACE) {
            return null;
        }
        return PsiTreeUtil.getParentOfType(prev, CqrsNamespaceDef.class);
    }

    /** The {@code reacts-to} reaction surrounding the caret, or {@code null} (same recovery as above). */
    private static CqrsProcessReaction enclosingProcessReaction(PsiElement position) {
        CqrsProcessReaction reaction = PsiTreeUtil.getParentOfType(position, CqrsProcessReaction.class);
        if (reaction != null) {
            return reaction;
        }
        PsiElement prev = PsiTreeUtil.prevVisibleLeaf(position);
        if (prev == null || prev.getNode().getElementType() == CqrsTypes.RBRACE) {
            return null;
        }
        return PsiTreeUtil.getParentOfType(prev, CqrsProcessReaction.class);
    }

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
