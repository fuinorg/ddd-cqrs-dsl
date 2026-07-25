package org.fuin.dsl.cqrs.intellij.reference;

import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.CqrsFileType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAggregateId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsAnnotationDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsBusinessRule;
import org.fuin.dsl.cqrs.intellij.psi.CqrsCommandDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsCommandHandler;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstraintDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsConstructorDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsContextDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsDataProtectionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEntityId;
import org.fuin.dsl.cqrs.intellij.psi.CqrsEventDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsExceptionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsExternalType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsImportDecl;
import org.fuin.dsl.cqrs.intellij.psi.CqrsMethodDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamespaceDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsOperationContext;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProcessState;
import org.fuin.dsl.cqrs.intellij.psi.CqrsProjectionDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsPsiUtil;
import org.fuin.dsl.cqrs.intellij.psi.CqrsServiceDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.fuin.dsl.cqrs.intellij.psi.CqrsViewDef;
import org.fuin.dsl.cqrs.intellij.remote.CqrsRemoteScopeResolver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Name resolution for the CQRS DSL. Mirrors the Xtext scoping: a reference resolves against
 * declarations that are local (same file or same namespace), imported, or provided by a remote model
 * (see {@link CqrsRemoteScopeResolver}) — and against nothing else. A name that exists somewhere in
 * the project but is not imported does <em>not</em> resolve, so the annotator reports it exactly like
 * the Eclipse plugin and the SrcGen4J build do. Resolving it anyway would be more convenient to
 * navigate, but it would hide a model error until the build fails.
 */
public final class CqrsResolveUtil {

    private CqrsResolveUtil() {
    }

    /** Fully qualified name of a declaration: the dotted chain of all enclosing named elements. */
    public static String getQualifiedName(CqrsNamedElement element) {
        Deque<String> parts = new ArrayDeque<>();
        for (PsiElement cur = element; cur != null; cur = cur.getParent()) {
            if (cur instanceof CqrsNamedElement) {
                String name = ((CqrsNamedElement) cur).getName();
                if (name != null && !name.isEmpty()) {
                    parts.addFirst(name);
                }
            }
        }
        return String.join(".", parts);
    }

    /**
     * Fully qualified name ({@code project.context.namespace}) of the namespace enclosing the given
     * element. The namespace is optional: when the element lives directly in a context (no
     * namespace), the enclosing scope is the context itself ({@code project.context}). Returns ""
     * when there is neither.
     * <p>
     * The name includes the project, so that it can be compared with the qualified name of a
     * declaration and with the imports, which are fully qualified as well.
     */
    public static String enclosingNamespaceFqn(PsiElement element) {
        CqrsNamespaceDef ns = PsiTreeUtil.getParentOfType(element, CqrsNamespaceDef.class);
        if (ns != null) {
            return getQualifiedName(ns);
        }
        CqrsContextDef ctx = PsiTreeUtil.getParentOfType(element, CqrsContextDef.class);
        return ctx != null ? getQualifiedName(ctx) : "";
    }

    /** Namespaces imported with a trailing wildcard ({@code import a.b.*}). */
    public static List<String> wildcardImports(PsiElement element) {
        List<String> result = new ArrayList<>();
        for (CqrsImportDecl imp : imports(element)) {
            String ns = CqrsPsiUtil.getImportedNamespace(imp);
            if (ns != null && ns.endsWith(".*")) {
                result.add(ns.substring(0, ns.length() - 2));
            }
        }
        return result;
    }

    /** Specific imports ({@code import a.b.Name}) — the full names they bring into scope. */
    public static List<String> specificImports(PsiElement element) {
        List<String> result = new ArrayList<>();
        for (CqrsImportDecl imp : imports(element)) {
            String ns = CqrsPsiUtil.getImportedNamespace(imp);
            if (ns != null && !ns.endsWith(".*")) {
                result.add(ns);
            }
        }
        return result;
    }

    private static List<CqrsImportDecl> imports(PsiElement element) {
        CqrsNamespaceDef ns = PsiTreeUtil.getParentOfType(element, CqrsNamespaceDef.class);
        if (ns != null) {
            return new ArrayList<>(PsiTreeUtil.getChildrenOfTypeAsList(ns, CqrsImportDecl.class));
        }
        // Namespace omitted: imports are declared directly on the enclosing context.
        CqrsContextDef ctx = PsiTreeUtil.getParentOfType(element, CqrsContextDef.class);
        if (ctx != null) {
            return new ArrayList<>(PsiTreeUtil.getChildrenOfTypeAsList(ctx, CqrsImportDecl.class));
        }
        return List.of();
    }

    /** All named declarations across the project's {@code .cqrs} files. */
    public static List<CqrsNamedElement> allDeclarations(Project project) {
        List<CqrsNamedElement> result = new ArrayList<>();
        Collection<VirtualFile> files;
        try {
            files = FileTypeIndex.getFiles(CqrsFileType.INSTANCE, GlobalSearchScope.allScope(project));
        } catch (IndexNotReadyException notReady) {
            return result; // indexes are being built (dumb mode); resolve again later
        }
        PsiManager psiManager = PsiManager.getInstance(project);
        for (VirtualFile vf : files) {
            PsiFile psiFile = psiManager.findFile(vf);
            if (psiFile instanceof CqrsFile) {
                result.addAll(PsiTreeUtil.findChildrenOfType(psiFile, CqrsNamedElement.class));
            }
        }
        return result;
    }

    /** Declarations visible to {@code element}: project-local plus remote (import-resolved). */
    public static List<CqrsNamedElement> visibleDeclarations(PsiElement element) {
        Project project = element.getProject();
        List<CqrsNamedElement> all = new ArrayList<>(allDeclarations(project));
        all.addAll(CqrsRemoteScopeResolver.getInstance(project).remoteDeclarations(element.getContainingFile()));
        return dedupByLocation(all);
    }

    /**
     * The declarations that may be referenced at the given position - the visible ones, narrowed to
     * what the grammar allows there.
     *
     * @param element Position the reference is written at.
     *
     * @return Visible declarations of the expected kind.
     */
    public static List<CqrsNamedElement> referenceableDeclarations(PsiElement element) {
        Predicate<CqrsNamedElement> expected = expectedDeclaration(element);
        List<CqrsNamedElement> visible = visibleDeclarations(element);
        if (expected == null) {
            return visible;
        }
        List<CqrsNamedElement> result = new ArrayList<>();
        for (CqrsNamedElement decl : visible) {
            if (expected.test(decl)) {
                result.add(decl);
            }
        }
        return result;
    }

    /**
     * Returns what kind of declaration a reference at the given position may point at, or
     * {@code null} when any type is allowed.
     * <p>
     * Almost every cross-reference in the grammar is typed - {@code fires} names an event, a view's
     * {@code uses} a projection, {@code identifies} an entity - but the IntelliJ grammar parses all of
     * them as a plain {@code type_ref}, so the kind has to be recovered from the syntactic position.
     * The keyword that introduces the clause is what identifies it; a few keywords serve two clauses
     * with different types and are told apart by the declaration they sit in.
     * <p>
     * A position not listed here allows any type - that is the honest answer for {@code returns},
     * {@code instance-key}, an attribute or parameter type, a generic argument and a constraint's
     * {@code input}, all of which are declared {@code [Type|FQN]}.
     *
     * @param element Position the reference is written at.
     *
     * @return Predicate the declaration has to satisfy, or {@code null} for "any type".
     */
    @Nullable
    public static Predicate<CqrsNamedElement> expectedDeclaration(PsiElement element) {
        PsiElement keyword = introducingKeyword(element);
        if (keyword == null) {
            return null;
        }
        IElementType type = keyword.getNode().getElementType();

        if (type == CqrsTypes.KW_OPERATION_CONTEXT) {
            return kind(CqrsServiceDef.class);
        }
        if (type == CqrsTypes.KW_PROTECTED_BY) {
            return kind(CqrsDataProtectionDef.class);
        }
        if (type == CqrsTypes.KW_BASE) {
            return kind(CqrsExternalType.class);
        }
        if (type == CqrsTypes.KW_ROOT) {
            return kind(CqrsAggregateDef.class);
        }
        if (type == CqrsTypes.KW_FIRES || type == CqrsTypes.KW_REACTS_TO) {
            return kind(CqrsEventDef.class);
        }
        if (type == CqrsTypes.KW_REF) {
            return kind(CqrsMethodDef.class);
        }
        if (type == CqrsTypes.KW_INVARIANTS || type == CqrsTypes.KW_PRECONDITIONS) {
            return kind(CqrsConstraintDef.class);
        }
        if (type == CqrsTypes.KW_BUSINESS_RULES) {
            return kind(CqrsBusinessRule.class);
        }
        if (type == CqrsTypes.AT) {
            return kind(CqrsAnnotationDef.class);
        }
        if (type == CqrsTypes.KW_HANDLES || type == CqrsTypes.KW_ISSUES_COMMANDS) {
            return kind(CqrsCommandDef.class);
        }
        if (type == CqrsTypes.KW_IN_STATE || type == CqrsTypes.KW_TRANSITION_TO) {
            return kind(CqrsProcessState.class);
        }
        // An operation: a constructor or a method, so two PSI classes rather than one.
        if (type == CqrsTypes.KW_COPIES_ATTRIBUTES_OF || type == CqrsTypes.KW_TARGET) {
            return decl -> decl instanceof CqrsConstructorDef || decl instanceof CqrsMethodDef;
        }
        // 'identifies' points the id at what it identifies, 'identifier' the other way round - which
        // one, and whether an entity or an aggregate, follows from the declaration being written.
        if (type == CqrsTypes.KW_IDENTIFIES) {
            if (PsiTreeUtil.getParentOfType(element, CqrsEntityId.class) != null) {
                return kind(CqrsEntityDef.class);
            }
            if (PsiTreeUtil.getParentOfType(element, CqrsAggregateId.class) != null) {
                return kind(CqrsAggregateDef.class);
            }
            return null;
        }
        if (type == CqrsTypes.KW_IDENTIFIER) {
            if (PsiTreeUtil.getParentOfType(element, CqrsEntityDef.class) != null) {
                return kind(CqrsEntityId.class);
            }
            if (PsiTreeUtil.getParentOfType(element, CqrsAggregateDef.class) != null) {
                return kind(CqrsAggregateId.class);
            }
            return null;
        }
        // A view 'uses' its projection, a command handler the aggregates it works on.
        if (type == CqrsTypes.KW_USES) {
            if (PsiTreeUtil.getParentOfType(element, CqrsViewDef.class) != null) {
                return kind(CqrsProjectionDef.class);
            }
            if (PsiTreeUtil.getParentOfType(element, CqrsCommandHandler.class) != null) {
                return kind(CqrsAggregateDef.class);
            }
            return null;
        }
        // A projection's 'input' are events; a constraint's 'input' is any type.
        if (type == CqrsTypes.KW_INPUT) {
            return PsiTreeUtil.getParentOfType(element, CqrsProjectionDef.class) != null
                    ? kind(CqrsEventDef.class) : null;
        }
        // 'exception' either references one (on a constraint or a business rule) or introduces the
        // declaration of one - and the name of a new declaration is not a reference to anything.
        if (type == CqrsTypes.KW_EXCEPTION) {
            return PsiTreeUtil.getParentOfType(element, CqrsExceptionDef.class) != null
                    ? NONE : kind(CqrsExceptionDef.class);
        }
        return null;
    }

    /** Matches nothing: the position takes the name of a new declaration, not a reference. */
    private static final Predicate<CqrsNamedElement> NONE = decl -> false;

    private static Predicate<CqrsNamedElement> kind(Class<? extends CqrsNamedElement> expected) {
        return expected::isInstance;
    }

    /**
     * The keyword introducing the reference clause the given position belongs to, or {@code null}.
     * <p>
     * Several clauses take a comma-separated list ({@code fires A, B}), so the search walks back over
     * whole references and the commas between them; it stops at anything else. Starting from the token
     * before the position rather than from the PSI node keeps it working while the reference is still
     * being typed, when it is not yet part of its clause.
     *
     * @param element Position to look back from.
     *
     * @return Introducing token, or {@code null} at the start of the file.
     */
    @Nullable
    private static PsiElement introducingKeyword(PsiElement element) {
        PsiElement cur = PsiTreeUtil.prevVisibleLeaf(element);
        while (cur != null && cur.getNode().getElementType() == CqrsTypes.COMMA) {
            cur = PsiTreeUtil.prevVisibleLeaf(cur);
            // Walk over the (possibly qualified) reference that comma separated from this one.
            while (cur != null && (cur.getNode().getElementType() == CqrsTypes.ID
                    || cur.getNode().getElementType() == CqrsTypes.DOT)) {
                cur = PsiTreeUtil.prevVisibleLeaf(cur);
            }
        }
        return cur;
    }

    /**
     * Collapses declarations that denote the same physical source location. The project scope
     * ({@link #allDeclarations}) and the remote scope ({@link CqrsRemoteScopeResolver#remoteDeclarations})
     * overlap — a cached model is also indexed as a project file, and one artifact providing several
     * imported namespaces is served once per namespace — so the same declaration would otherwise be
     * offered several times and surface as a bogus "Multiple Implementations" choice. Keying on the
     * source location (file path + start offset) rather than the name keeps genuinely distinct,
     * same-named declarations across namespaces separate.
     */
    private static List<CqrsNamedElement> dedupByLocation(List<CqrsNamedElement> declarations) {
        List<CqrsNamedElement> result = new ArrayList<>(declarations.size());
        Set<String> seen = new LinkedHashSet<>();
        for (CqrsNamedElement decl : declarations) {
            if (seen.add(locationKey(decl))) {
                result.add(decl);
            }
        }
        return result;
    }

    /** Stable identity of a declaration's source location; falls back to object identity in-memory. */
    private static String locationKey(CqrsNamedElement decl) {
        PsiFile file = decl.getContainingFile();
        VirtualFile vf = file != null ? file.getVirtualFile() : null;
        String path = vf != null ? vf.getUrl() : "mem:" + System.identityHashCode(file);
        return path + "#" + decl.getTextRange().getStartOffset();
    }

    /** Resolve a (possibly qualified) reference name to its declaration(s). */
    public static List<CqrsNamedElement> resolve(PsiElement context, String referencedName) {
        if (referencedName == null || referencedName.isEmpty()) {
            return List.of();
        }
        List<CqrsNamedElement> candidates = visibleDeclarations(context);
        PsiFile contextFile = context.getContainingFile();
        String currentNs = enclosingNamespaceFqn(context);
        List<String> wildcards = wildcardImports(context);
        List<String> specifics = specificImports(context);

        boolean qualified = referencedName.contains(".");
        List<CqrsNamedElement> result = new ArrayList<>();

        for (CqrsNamedElement decl : candidates) {
            String simple = decl.getName();
            if (simple == null) {
                continue;
            }
            String fqn = getQualifiedName(decl);

            if (qualified) {
                if (fqn.equals(referencedName) || fqn.endsWith("." + referencedName)) {
                    result.add(decl);
                }
                continue;
            }

            if (!simple.equals(referencedName)) {
                continue;
            }
            // simple-name reference: only visible when local or imported
            boolean sameFile = contextFile != null && contextFile.equals(decl.getContainingFile());
            boolean sameNamespace = !currentNs.isEmpty() && fqn.equals(currentNs + "." + simple);
            String declNs = namespaceOf(fqn);
            boolean wildcardImported = wildcards.contains(declNs);
            boolean specificImported = specifics.contains(fqn);
            if (sameFile || sameNamespace || wildcardImported || specificImported) {
                result.add(decl);
            }
        }
        return ofExpectedKind(context, result);
    }

    /**
     * Keeps only the declarations the reference may point to, per {@link #expectedDeclaration}. A
     * command's {@code target} names the operation it triggers and a view's {@code uses} a projection,
     * so anything else is not a candidate: the reference is left unresolved and the annotator reports
     * it, exactly as the Xtext scoping and therefore the build do. Resolving it anyway would be more
     * convenient to navigate, but it would hide a model error until the build fails.
     *
     * @param context Position the reference is written at.
     * @param candidates Declarations matching by name.
     *
     * @return Those of them the grammar allows at that position.
     */
    private static List<CqrsNamedElement> ofExpectedKind(PsiElement context,
            List<CqrsNamedElement> candidates) {
        Predicate<CqrsNamedElement> expected = expectedDeclaration(context);
        if (expected == null) {
            return candidates;
        }
        List<CqrsNamedElement> result = new ArrayList<>();
        for (CqrsNamedElement decl : candidates) {
            if (expected.test(decl)) {
                result.add(decl);
            }
        }
        return result;
    }

    private static String namespaceOf(String fqn) {
        int dot = fqn.lastIndexOf('.');
        return dot < 0 ? "" : fqn.substring(0, dot);
    }
}
