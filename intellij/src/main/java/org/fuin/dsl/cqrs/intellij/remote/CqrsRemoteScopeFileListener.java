package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Watches for changes to {@code dependencies.json} catalogs and the {@code .dependencies-cache}
 * directory. When one changes, the in-memory cache index is dropped and the daemon restarted so
 * resolution reflects the new state (mirroring the Eclipse "re-read on modification" behavior).
 */
public final class CqrsRemoteScopeFileListener implements BulkFileListener {

    @Override
    public void after(@NotNull List<? extends VFileEvent> events) {
        boolean relevant = false;
        String catalogName = RemoteScopeCatalog.fileName();
        for (VFileEvent event : events) {
            String path = event.getPath();
            if (path.endsWith("/" + catalogName) || path.endsWith(catalogName)
                    || path.contains(RemoteScopeCache.CACHE_DIR_NAME)) {
                relevant = true;
                break;
            }
        }
        if (!relevant) {
            return;
        }
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            if (project.isDisposed()) {
                continue;
            }
            CqrsRemoteScopeResolver.getInstance(project).invalidate();
            ApplicationManager.getApplication().invokeLater(() -> {
                if (!project.isDisposed()) {
                    DaemonCodeAnalyzer.getInstance(project).restart();
                }
            });
        }
    }
}
