package org.fuin.dsl.cqrs.intellij.reference;

import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.CqrsFileType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsContextDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsImportDecl;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamespaceDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsPsiUtil;
import org.fuin.dsl.cqrs.intellij.remote.CqrsRemoteScopeResolver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Name resolution for the CQRS DSL. Mirrors the spirit of the Xtext scoping: a reference resolves
 * against declarations that are local (same file/namespace), imported, or provided by a remote
 * model (see {@link CqrsRemoteScopeResolver}). The matching is intentionally lenient — an IDE
 * preferring a useful navigation target over a strict miss — while the annotator reports names
 * that resolve to nothing at all.
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

    /** {@code context.namespace} of the namespace enclosing the given element, or "". */
    public static String enclosingNamespaceFqn(PsiElement element) {
        CqrsNamespaceDef ns = PsiTreeUtil.getParentOfType(element, CqrsNamespaceDef.class);
        if (ns == null) {
            return "";
        }
        CqrsContextDef ctx = PsiTreeUtil.getParentOfType(ns, CqrsContextDef.class);
        String nsName = ns.getName();
        String ctxName = ctx != null ? ctx.getName() : null;
        if (ctxName == null) {
            return nsName != null ? nsName : "";
        }
        return nsName != null ? ctxName + "." + nsName : ctxName;
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
        if (ns == null) {
            return List.of();
        }
        return new ArrayList<>(PsiTreeUtil.getChildrenOfTypeAsList(ns, CqrsImportDecl.class));
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
        List<CqrsNamedElement> strict = new ArrayList<>();
        List<CqrsNamedElement> lenient = new ArrayList<>();

        for (CqrsNamedElement decl : candidates) {
            String simple = decl.getName();
            if (simple == null) {
                continue;
            }
            String fqn = getQualifiedName(decl);

            if (qualified) {
                if (fqn.equals(referencedName) || fqn.endsWith("." + referencedName)) {
                    strict.add(decl);
                }
                continue;
            }

            if (!simple.equals(referencedName)) {
                continue;
            }
            // simple-name reference
            boolean sameFile = contextFile != null && contextFile.equals(decl.getContainingFile());
            boolean sameNamespace = !currentNs.isEmpty() && fqn.equals(currentNs + "." + simple);
            String declNs = namespaceOf(fqn);
            boolean wildcardImported = wildcards.contains(declNs);
            boolean specificImported = specifics.contains(fqn);
            if (sameFile || sameNamespace || wildcardImported || specificImported) {
                strict.add(decl);
            } else {
                lenient.add(decl);
            }
        }
        return !strict.isEmpty() ? strict : lenient;
    }

    private static String namespaceOf(String fqn) {
        int dot = fqn.lastIndexOf('.');
        return dot < 0 ? "" : fqn.substring(0, dot);
    }
}
