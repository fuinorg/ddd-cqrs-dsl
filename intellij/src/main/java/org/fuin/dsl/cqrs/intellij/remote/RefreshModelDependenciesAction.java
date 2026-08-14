package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

/**
 * Forgets every resolved model <code>dependency</code> and re-highlights, so a dependency that changed
 * since the IDE started is picked up.
 *
 * <p>{@link CqrsRemoteScopeResolver} caches what a dependency resolved to for the lifetime of the
 * project, which is what makes reference resolution cheap - an archive is mounted once, not per
 * keystroke. The cost is that the cache has no natural end: a snapshot rebuilt outside the IDE, or a
 * coordinate that resolved to a stale build, keeps answering with what was true at startup. The symptom
 * is narrow and easy to misread - the modules an older build did contain still resolve, and only the
 * ones added since go red, which looks like a broken import rather than a stale archive.</p>
 *
 * <p>Restarting the IDE was previously the only way out. This action is that, without the restart.</p>
 *
 * <p>Nothing is re-resolved here. The caches are dropped and the open files are re-highlighted;
 * resolution then happens the way it always does, on the next reference that needs it, off the UI
 * thread. That also refreshes the archive in the VFS, so a rebuilt zip at an unchanged path is picked
 * up too.</p>
 */
public class RefreshModelDependenciesAction extends AnAction implements DumbAware {

    /** Shown in the daemon's diagnostics as the cause of the re-highlighting. */
    private static final String RESTART_REASON = "CQRS model dependencies refreshed";

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final Project project = event.getProject();
        if (project == null) {
            return;
        }
        CqrsRemoteScopeResolver.getInstance(project).invalidate();

        // Per open file: the no-argument restart() and the single-argument restart(PsiFile) are both
        // deprecated in favour of the overload that names a reason, which is what shows up in the
        // daemon's own diagnostics. Reading PSI needs no explicit read action - this runs on the EDT.
        final DaemonCodeAnalyzer daemon = DaemonCodeAnalyzer.getInstance(project);
        final PsiManager psiManager = PsiManager.getInstance(project);
        for (VirtualFile vf : FileEditorManager.getInstance(project).getOpenFiles()) {
            if (!vf.isValid()) {
                continue;
            }
            final PsiFile psiFile = psiManager.findFile(vf);
            if (psiFile != null) {
                daemon.restart(psiFile, RESTART_REASON);
            }
        }
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        event.getPresentation().setEnabledAndVisible(event.getProject() != null);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

}
