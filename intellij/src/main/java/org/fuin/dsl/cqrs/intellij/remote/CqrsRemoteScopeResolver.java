package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.PsiTreeUtil;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.CqrsFileType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsContextDef;
import org.fuin.dsl.cqrs.intellij.psi.CqrsDependencyDecl;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bridges the {@link CqrsModelArchives} into name resolution.
 *
 * <p>Which dependencies apply to a file follows the Xtext scoping: its own declarations plus the
 * context level ones of every same-named {@code context} block elsewhere in the project, because a
 * context may be split across files.</p>
 *
 * <p>The resolve path ({@link #remoteDeclarations}) only ever reads from the on-disk cache, so it is
 * safe under a read action / on the EDT. When a declared dependency is not yet cached, a background
 * download is scheduled; once it lands the VFS is refreshed and the daemon restarted so the new
 * declarations become resolvable. This is the IntelliJ counterpart of the Eclipse
 * {@code CqrsDslGlobalScopeProvider}, adapted to the platform's threading rules.</p>
 */
@Service(Service.Level.PROJECT)
public final class CqrsRemoteScopeResolver {

    private static final Logger LOG = Logger.getInstance(CqrsRemoteScopeResolver.class);

    private final Project project;
    private final CqrsModelArchives archives;
    private final Set<String> warmingInFlight = ConcurrentHashMap.newKeySet();


    public CqrsRemoteScopeResolver(Project project) {
        this(project, new CqrsModelArchives(project));
    }

    /** Lets a test supply archives that resolve without a Maven embedder. */
    CqrsRemoteScopeResolver(Project project, CqrsModelArchives archives) {
        this.archives = archives;
        this.project = project;
    }

    public static CqrsRemoteScopeResolver getInstance(Project project) {
        return project.getService(CqrsRemoteScopeResolver.class);
    }

    /** Declarations visible to {@code file} through its dependencies (cache-only, no network). */
    public @NotNull List<CqrsNamedElement> remoteDeclarations(PsiFile psiFile) {
        PsiFile file = physical(psiFile);
        if (file == null) {
            return List.of();
        }
        List<CqrsNamedElement> result = new ArrayList<>();
        boolean missing = false;
        PsiManager psiManager = PsiManager.getInstance(project);
        for (Declared declared : dependencyEntries(file)) {
            List<VirtualFile> models = archives.modelFiles(declared.modelDir(), declared.entry(), false);
            if (models.isEmpty()) {
                missing = true;
                continue;
            }
            for (VirtualFile vf : models) {
                PsiFile remote = psiManager.findFile(vf);
                if (remote instanceof CqrsFile) {
                    result.addAll(PsiTreeUtil.findChildrenOfType(remote, CqrsNamedElement.class));
                }
            }
        }
        if (missing) {
            scheduleWarm(file);
        }
        return result;
    }

    /** A dependency together with the directory of the file that declares it. */
    private record Declared(@Nullable Path modelDir, RemoteScopeEntry entry) {
    }

    /**
     * The dependencies that apply to {@code file}: every {@code dependency} it declares itself - on a
     * context or on a module - plus the context level dependencies of every <em>other</em> file
     * declaring a context of the same name. A context may be split across files, and a dependency
     * declared on it applies to all of them; this mirrors {@code CqrsDependencies.declared} of the
     * Xtext scoping, so the editor and the SrcGen4J build agree on what a model depends on.
     *
     * <p>A malformed coordinate is skipped &mdash; the annotator reports it.</p>
     */
    private static List<Declared> dependencyEntries(PsiFile file) {
        return CachedValuesManager.getCachedValue(file, () -> CachedValueProvider.Result
                .create(collectDependencies(file), PsiModificationTracker.MODIFICATION_COUNT));
    }

    private static List<Declared> collectDependencies(PsiFile file) {
        Map<String, Declared> entries = new LinkedHashMap<>();
        Path ownDir = parentNioPath(file);
        for (CqrsDependencyDecl decl : PsiTreeUtil.findChildrenOfType(file, CqrsDependencyDecl.class)) {
            put(entries, ownDir, decl);
        }

        Set<String> contextNames = contextNames(file);
        if (!contextNames.isEmpty()) {
            VirtualFile own = file.getVirtualFile();
            for (PsiFile other : cqrsFiles(file)) {
                if (other.getVirtualFile() == null || other.getVirtualFile().equals(own)) {
                    continue;
                }
                Path otherDir = parentNioPath(other);
                for (CqrsContextDef context : PsiTreeUtil.findChildrenOfType(other, CqrsContextDef.class)) {
                    if (contextNames.contains(context.getName())) {
                        // Only what the context declares itself: a dependency nested in one of its
                        // modules belongs to that module, not to the context.
                        for (CqrsDependencyDecl decl : context.getDependencyDeclList()) {
                            put(entries, otherDir, decl);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(entries.values());
    }

    /** Adds a declaration under its identity - coordinate plus local override - keeping the first. */
    private static void put(Map<String, Declared> target, @Nullable Path modelDir, CqrsDependencyDecl decl) {
        String local = CqrsPsiUtil.getDependencyLocal(decl);
        RemoteScopeEntry entry = RemoteScopeEntry.parse(CqrsPsiUtil.getDependencyCoordinate(decl), local);
        if (entry != null) {
            target.putIfAbsent(entry.getSourceId() + "|" + (local == null ? "" : local),
                    new Declared(modelDir, entry));
        }
    }

    /** Names of the contexts the file declares. */
    private static Set<String> contextNames(PsiFile file) {
        Set<String> result = new LinkedHashSet<>();
        for (CqrsContextDef context : PsiTreeUtil.findChildrenOfType(file, CqrsContextDef.class)) {
            String name = context.getName();
            if (name != null && !name.isEmpty()) {
                result.add(name);
            }
        }
        return result;
    }

    /** Every {@code .cqrs} file of the project, or none while the indexes are still being built. */
    private static List<PsiFile> cqrsFiles(PsiFile file) {
        Project project = file.getProject();
        Collection<VirtualFile> files;
        try {
            files = FileTypeIndex.getFiles(CqrsFileType.INSTANCE, GlobalSearchScope.allScope(project));
        } catch (IndexNotReadyException notReady) {
            return List.of(); // dumb mode; resolve again later
        }
        PsiManager psiManager = PsiManager.getInstance(project);
        List<PsiFile> result = new ArrayList<>();
        for (VirtualFile vf : files) {
            PsiFile psiFile = psiManager.findFile(vf);
            if (psiFile instanceof CqrsFile) {
                result.add(psiFile);
            }
        }
        return result;
    }

    /**
     * The file on disk behind a {@code .cqrs} PSI file, or {@code null} when there is none.
     *
     * <p>Code completion, intentions and inspections do not run on the file the user is editing but on
     * a non-physical copy of it. A copy has no {@link PsiFile#getVirtualFile() virtual file} - so no
     * directory a {@code local} clause could be resolved against - and it is not in the index either,
     * which is what the cross-file lookup needs. Its {@link PsiFile#getOriginalFile() original} is the
     * edited file and answers both.</p>
     */
    private static @Nullable PsiFile physical(PsiFile file) {
        if (!(file instanceof CqrsFile)) {
            return null;
        }
        PsiFile original = file.getOriginalFile();
        return original.getVirtualFile() == null ? null : original;
    }

    private static Path parentNioPath(PsiFile file) {
        VirtualFile vf = file.getVirtualFile();
        if (vf == null || vf.getParent() == null) {
            return null;
        }
        try {
            return vf.getParent().toNioPath();
        } catch (UnsupportedOperationException e) {
            return null;
        }
    }

    // ---- Background warming (downloads off the resolve thread) -----------

    private void scheduleWarm(PsiFile file) {
        VirtualFile vf = file.getVirtualFile();
        if (vf == null || !warmingInFlight.add(vf.getPath())) {
            return;
        }
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                List<Declared> pending = ApplicationManager.getApplication()
                        .runReadAction((Computable<List<Declared>>) () -> gatherPending(file));
                for (Declared p : pending) {
                    archives.modelFiles(p.modelDir(), p.entry(), true);
                }
                // Restart either way: a recorded failure has to reach the annotator too.
                ApplicationManager.getApplication().invokeLater(() -> {
                    if (!project.isDisposed() && file.isValid()) {
                        DaemonCodeAnalyzer.getInstance(project).restart(file);
                    }
                });
            } catch (Exception ex) {
                LOG.warn("Remote scope warming failed for " + vf.getPath(), ex);
            } finally {
                warmingInFlight.remove(vf.getPath());
            }
        });
    }

    private List<Declared> gatherPending(PsiFile file) {
        List<Declared> pending = new ArrayList<>();
        for (Declared declared : dependencyEntries(file)) {
            if (archives.modelFiles(declared.modelDir(), declared.entry(), false).isEmpty()) {
                pending.add(declared);
            }
        }
        return pending;
    }

    /**
     * Why the given dependency cannot be resolved, or {@code null} when it resolves, when the answer
     * is not known yet (a download is still running) or when the file has no location on disk.
     *
     * <p>Never resolves on the calling thread: it reads what is already on disk plus what the
     * background warming recorded, and starts that warming when nothing is known yet. Safe to call
     * from an annotator.</p>
     *
     * @param file File declaring the dependency.
     * @param entry Dependency to check.
     *
     * @return Problem description, or {@code null}.
     */
    public @Nullable String problem(@NotNull PsiFile psiFile, @NotNull RemoteScopeEntry entry) {
        PsiFile file = physical(psiFile);
        if (file == null) {
            return null;
        }
        Path startDir = parentNioPath(file);
        if (startDir == null) {
            // Inside a jar there is no directory to resolve a 'local' clause against, so nothing here
            // can be judged - a model of a dependency is read, not authored.
            return null;
        }
        String known = archives.problem(startDir, entry);
        if (known == null && archives.modelFiles(startDir, entry, false).isEmpty()) {
            // Nothing is known about this dependency yet. Resolution otherwise only starts while a
            // reference is being resolved, so a file that declares a dependency and holds no
            // reference at all - a context with just "dependency" and "hint", say - would never
            // report a broken coordinate. Start it here too; the daemon restart at the end of the
            // warming brings this annotator back with the answer.
            scheduleWarm(file);
        }
        return known;
    }



    /** Forget what was resolved, so the next call resolves again. */
    public void invalidate() {
        archives.invalidate();
    }
}
