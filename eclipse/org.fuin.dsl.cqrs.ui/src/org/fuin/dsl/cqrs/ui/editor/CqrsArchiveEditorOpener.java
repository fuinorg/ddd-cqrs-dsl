package org.fuin.dsl.cqrs.ui.editor;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.xtext.ui.editor.LanguageSpecificURIEditorOpener;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives;

/**
 * Opens a model that lives inside a <code>dependency</code>'s archive, read-only.
 * <p>
 * The inherited opener turns a URI into an editor by asking the
 * {@link org.eclipse.xtext.ui.resource.IStorage2UriMapper} for a workspace resource. A model resolved
 * out of an artifact's zip has none - its URI is
 * <code>archive:file:/…/x.zip!/model/public/y.cqrs</code> - so the mapper answers with nothing and F3
 * silently does nothing at all. Reading the zip entry is the one thing missing; everything after it,
 * including revealing the element the reference pointed at, is inherited unchanged.
 * <p>
 * Only <code>archive:</code> URIs are handled here. Anything else is a file of the workspace and goes
 * to the inherited implementation.
 */
public class CqrsArchiveEditorOpener extends LanguageSpecificURIEditorOpener {

    private static final Logger LOG = Logger.getLogger(CqrsArchiveEditorOpener.class);

    @Override
    public IEditorPart open(final URI uri, final EReference crossReference, final int indexInList,
            final boolean select) {

        final URI resource = uri.trimFragment();
        if (!CqrsModelArchives.isArchived(resource)) {
            return super.open(uri, crossReference, indexInList, select);
        }
        final IWorkbenchPage page = activePage();
        if (page == null) {
            return null;
        }
        try {
            // A storage that is not an IFile becomes an XtextReadonlyEditorInput, which is what makes
            // the editor read-only - a dependency's model is read, not authored.
            final IEditorPart editor = IDE.openEditor(page,
                    EditorUtils.createEditorInput(new CqrsArchiveStorage(resource)), getEditorId());
            selectAndReveal(editor, uri, crossReference, indexInList, select);
            return EditorUtils.getXtextEditor(editor);
        } catch (final PartInitException ex) {
            LOG.error("Could not open '" + resource + "' out of the archive: " + ex.getMessage(), ex);
        } catch (final RuntimeException ex) {
            LOG.error("Could not open '" + resource + "' out of the archive: " + ex.getMessage(), ex);
        }
        return null;
    }

    /** The page an editor would be opened in, or <code>null</code> when there is no workbench. */
    private static IWorkbenchPage activePage() {
        if (!PlatformUI.isWorkbenchRunning()) {
            return null;
        }
        final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        return window == null ? null : window.getActivePage();
    }
}
