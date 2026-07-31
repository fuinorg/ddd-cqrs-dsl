package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.psi.CqrsDependencyDecl;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bridges the {@link CqrsModelArchives} into name resolution.
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
    public @NotNull List<CqrsNamedElement> remoteDeclarations(PsiFile file) {
        if (!(file instanceof CqrsFile) || file.getVirtualFile() == null) {
            return List.of();
        }
        Path startDir = parentNioPath(file);
        if (startDir == null) {
            return List.of();
        }
        List<CqrsNamedElement> result = new ArrayList<>();
        boolean missing = false;
        PsiManager psiManager = PsiManager.getInstance(project);
        for (RemoteScopeEntry entry : dependencyEntries(file)) {
            List<VirtualFile> models = archives.modelFiles(startDir, entry, false);
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

    /**
     * The dependencies that apply to {@code file}: every {@code dependency} it declares itself, on a
     * project or on a context. A malformed coordinate is skipped &mdash; the annotator reports it.
     */
    private static List<RemoteScopeEntry> dependencyEntries(PsiFile file) {
        Map<String, RemoteScopeEntry> entries = new LinkedHashMap<>();
        for (CqrsDependencyDecl decl : PsiTreeUtil.findChildrenOfType(file, CqrsDependencyDecl.class)) {
            String local = CqrsPsiUtil.getDependencyLocal(decl);
            RemoteScopeEntry entry = RemoteScopeEntry.parse(CqrsPsiUtil.getDependencyCoordinate(decl), local);
            if (entry != null) {
                entries.putIfAbsent(entry.getSourceId() + "|" + (local == null ? "" : local), entry);
            }
        }
        return new ArrayList<>(entries.values());
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
                List<Pending> pending = ApplicationManager.getApplication()
                        .runReadAction((Computable<List<Pending>>) () -> gatherPending(file));
                for (Pending p : pending) {
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

    private record Pending(Path modelDir, RemoteScopeEntry entry) {
    }

    private List<Pending> gatherPending(PsiFile file) {
        Path startDir = parentNioPath(file);
        if (startDir == null) {
            return List.of();
        }
        List<Pending> pending = new ArrayList<>();
        for (RemoteScopeEntry entry : dependencyEntries(file)) {
            if (archives.modelFiles(startDir, entry, false).isEmpty()) {
                pending.add(new Pending(startDir, entry));
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
    public @Nullable String problem(@NotNull PsiFile file, @NotNull RemoteScopeEntry entry) {
        Path startDir = parentNioPath(file);
        if (startDir == null) {
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
