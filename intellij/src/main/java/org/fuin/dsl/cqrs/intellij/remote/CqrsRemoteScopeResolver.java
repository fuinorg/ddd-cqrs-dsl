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
import org.fuin.dsl.cqrs.intellij.psi.CqrsImportDecl;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;
import org.fuin.dsl.cqrs.intellij.psi.CqrsPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bridges the {@link RemoteScopeCatalog}/{@link RemoteScopeCache} into name resolution.
 *
 * <p>The resolve path ({@link #remoteDeclarations}) only ever reads from the on-disk cache, so it is
 * safe under a read action / on the EDT. When an imported model is configured but not yet cached, a
 * background download is scheduled; once it lands the VFS is refreshed and the daemon restarted so
 * the new declarations become resolvable. This is the IntelliJ counterpart of the Eclipse
 * {@code CqrsDslGlobalScopeProvider}, adapted to the platform's threading rules.</p>
 */
@Service(Service.Level.PROJECT)
public final class CqrsRemoteScopeResolver {

    private static final Logger LOG = Logger.getInstance(CqrsRemoteScopeResolver.class);

    private final Project project;
    private final RemoteScopeCatalog catalog = new RemoteScopeCatalog();
    private final RemoteScopeCache cache = new RemoteScopeCache();
    private final Set<String> warmingInFlight = ConcurrentHashMap.newKeySet();

    public CqrsRemoteScopeResolver(Project project) {
        this.project = project;
    }

    public static CqrsRemoteScopeResolver getInstance(Project project) {
        return project.getService(CqrsRemoteScopeResolver.class);
    }

    /** Remote declarations visible to {@code file} through its imports (cache-only, no network). */
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
        for (String namespace : importedNamespaces(file)) {
            String url = catalog.lookupUrl(startDir, namespace);
            if (url == null) {
                continue;
            }
            Path root = catalog.rootDir(startDir);
            Path cached = cache.getCachedModelFile(root, namespace, url, false);
            if (cached == null) {
                missing = true;
                continue;
            }
            VirtualFile vf = LocalFileSystem.getInstance().findFileByNioFile(cached);
            if (vf == null) {
                missing = true;
                continue;
            }
            PsiFile remote = psiManager.findFile(vf);
            if (remote instanceof CqrsFile) {
                result.addAll(PsiTreeUtil.findChildrenOfType(remote, CqrsNamedElement.class));
            }
        }
        if (missing) {
            scheduleWarm(file);
        }
        return result;
    }

    private static List<String> importedNamespaces(PsiFile file) {
        List<String> namespaces = new ArrayList<>();
        for (CqrsImportDecl imp : PsiTreeUtil.findChildrenOfType(file, CqrsImportDecl.class)) {
            String ns = CqrsPsiUtil.getImportedNamespace(imp);
            if (ns != null) {
                namespaces.add(ns);
            }
        }
        return namespaces;
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
                List<Path> fetched = new ArrayList<>();
                for (Pending p : pending) {
                    Path cached = cache.getCachedModelFile(p.root(), p.namespace(), p.url(), true);
                    if (cached != null) {
                        fetched.add(cached);
                    }
                }
                if (!fetched.isEmpty()) {
                    LocalFileSystem.getInstance().refreshNioFiles(fetched);
                    ApplicationManager.getApplication().invokeLater(() -> {
                        if (!project.isDisposed() && file.isValid()) {
                            DaemonCodeAnalyzer.getInstance(project).restart(file);
                        }
                    });
                }
            } catch (Exception ex) {
                LOG.warn("Remote scope warming failed for " + vf.getPath(), ex);
            } finally {
                warmingInFlight.remove(vf.getPath());
            }
        });
    }

    private record Pending(Path root, String namespace, String url) {
    }

    private List<Pending> gatherPending(PsiFile file) {
        Path startDir = parentNioPath(file);
        if (startDir == null) {
            return List.of();
        }
        Set<Pending> pending = new LinkedHashSet<>();
        for (String namespace : importedNamespaces(file)) {
            String url = catalog.lookupUrl(startDir, namespace);
            Path root = catalog.rootDir(startDir);
            if (url != null && root != null
                    && cache.getCachedModelFile(root, namespace, url, false) == null) {
                pending.add(new Pending(root, namespace, url));
            }
        }
        return new ArrayList<>(pending);
    }

    /** Forget cached index state (call when a catalog or cache file changes on disk). */
    public void invalidate() {
        cache.invalidate();
    }
}
