package org.fuin.dsl.cqrs.intellij;

import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.PsiTreeUtil;
import org.fuin.dsl.cqrs.intellij.psi.CqrsModuleDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsReferenceElement;
import org.fuin.dsl.cqrs.intellij.reference.CqrsResolveUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Which module depends on which, across the project's {@code .cqrs} files.
 *
 * <p>Ported from the Xtext side's {@code CqrsModuleDependencies}, which the SrcGen4J build uses, so
 * the editor and the build answer the same question the same way. As there, an <b>import is not a
 * dependency</b>: the edges are references that actually resolve, because an unused import would
 * otherwise invent a dependency and a fully qualified reference would hide one.</p>
 *
 * <p>A module is its <em>name</em>, not its block. A bounded context whose aggregates are split off
 * has a block of the same name in both halves, and those two blocks are one logical module - keyed
 * separately they would look like a module depending on itself.</p>
 *
 * <p><b>The graph is cached.</b> {@link CqrsResolveUtil#allDeclarations(Project)} walks every file's
 * PSI on every call, so recomputing this per annotation pass would be felt while typing. It is built
 * once per PSI modification, the same way {@code CqrsRemoteScopeResolver} caches what a file depends
 * on.</p>
 */
public final class CqrsModuleGraph {

    private CqrsModuleGraph() {
    }

    /**
     * The dependency graph of the whole project, keyed by the qualified module name.
     *
     * @param project Project to read.
     *
     * @return Module name to the names it depends on. Empty while indexes are still building.
     */
    public static @NotNull Map<String, Set<String>> of(@NotNull Project project) {
        return CachedValuesManager.getManager(project).getCachedValue(project,
                () -> CachedValueProvider.Result.create(build(project), PsiModificationTracker.MODIFICATION_COUNT));
    }

    /**
     * The cycle the named module takes part in, or an empty list when it takes part in none.
     *
     * @param moduleName Qualified module name.
     * @param graph      Graph to search.
     *
     * @return The cycle, starting and ending at this module, or empty.
     */
    public static @NotNull List<String> cycleThrough(@NotNull String moduleName,
            @NotNull Map<String, Set<String>> graph) {
        List<String> path = new ArrayList<>();
        List<String> found = walk(moduleName, graph, new LinkedHashSet<>(), new LinkedHashSet<>(), path);
        if (found != null && found.get(0).equals(moduleName)) {
            return found;
        }
        return List.of();
    }

    private static Map<String, Set<String>> build(Project project) {
        Map<String, Set<String>> graph = new TreeMap<>();
        Collection<VirtualFile> files;
        try {
            files = FileTypeIndex.getFiles(CqrsFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        } catch (IndexNotReadyException notReady) {
            // Indexes are being built. An empty graph reports nothing, which is the only honest answer
            // - "no cycle" would be a guess and a cycle would be a false alarm.
            return graph;
        }
        PsiManager psiManager = PsiManager.getInstance(project);
        for (VirtualFile vf : files) {
            PsiFile psiFile = psiManager.findFile(vf);
            if (!(psiFile instanceof CqrsFile)) {
                continue;
            }
            for (CqrsModuleDef module : PsiTreeUtil.findChildrenOfType(psiFile, CqrsModuleDef.class)) {
                String name = CqrsResolveUtil.getQualifiedName(module);
                if (name == null || name.isEmpty()) {
                    continue;
                }
                graph.computeIfAbsent(name, key -> new TreeSet<>()).addAll(dependenciesOf(module, name));
            }
        }
        // A name that is only ever pointed at - something from a dependency archive - is not a node.
        graph.values().forEach(targets -> targets.retainAll(graph.keySet()));
        return graph;
    }

    /** The modules the given block addresses, by resolving every reference it contains. */
    private static Set<String> dependenciesOf(CqrsModuleDef module, String ownName) {
        Set<String> result = new TreeSet<>();
        for (CqrsReferenceElement ref : PsiTreeUtil.findChildrenOfType(module, CqrsReferenceElement.class)) {
            PsiReference reference = ref.getReference();
            if (reference == null) {
                continue;
            }
            PsiElement target = reference.resolve();
            if (target == null) {
                // Unresolvable: reported by the import and reference checks, and no basis for an edge.
                continue;
            }
            String other = CqrsResolveUtil.enclosingModuleFqn(target);
            if (other != null && !other.isEmpty() && !other.equals(ownName)) {
                result.add(other);
            }
        }
        return result;
    }

    /** Depth first search returning the closing path as soon as it steps onto a module already on it. */
    private static List<String> walk(String current, Map<String, Set<String>> graph, Set<String> settled,
            Set<String> onPath, List<String> path) {
        if (onPath.contains(current)) {
            List<String> cycle = new ArrayList<>(path.subList(path.indexOf(current), path.size()));
            cycle.add(current);
            return cycle;
        }
        if (settled.contains(current)) {
            return null;
        }
        onPath.add(current);
        path.add(current);
        for (String next : graph.getOrDefault(current, Collections.emptySet())) {
            List<String> found = walk(next, graph, settled, onPath, path);
            if (found != null) {
                return found;
            }
        }
        path.remove(path.size() - 1);
        onPath.remove(current);
        settled.add(current);
        return null;
    }

}
