package org.fuin.dsl.cqrs.ui.editor;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * A model of a <code>dependency</code>, read straight out of the artifact's zip.
 * <p>
 * A <code>dependency</code> is resolved to entries <em>inside</em> the archive in the local Maven
 * repository - nothing is ever unpacked, so such a model has no {@link org.eclipse.core.resources.IFile}
 * and Eclipse has nothing to open. Wrapping the entry in an {@link IStorage} gives the workbench the
 * one thing it needs: a name and a stream. That is the same shape JDT uses to show a class file out of
 * a jar, and {@link org.eclipse.xtext.ui.editor.XtextReadonlyEditorInput} turns it into a read-only
 * editor input.
 * <p>
 * Reading goes through EMF's {@link URIConverter}, which is what loaded the model in the first place,
 * so the editor cannot end up showing something other than what was resolved against.
 */
public class CqrsArchiveStorage extends PlatformObject implements IStorage {

    private final URI uri;

    /**
     * Constructor with the URI of the model inside the archive.
     *
     * @param uri <code>archive:file:/…/x.zip!/model/…/y.cqrs</code> - never <code>null</code>.
     */
    public CqrsArchiveStorage(final URI uri) {
        if (uri == null || !uri.isArchive()) {
            throw new IllegalArgumentException("Not a URI inside an archive: " + uri);
        }
        this.uri = uri;
    }

    /**
     * Returns the URI the model was read from. This is the identity of the model - the resource the
     * editor shows has to carry it, or nothing in it could be located again.
     *
     * @return Archive URI, never <code>null</code>.
     */
    public URI getUri() {
        return uri;
    }

    @Override
    public InputStream getContents() throws CoreException {
        try {
            return URIConverter.INSTANCE.createInputStream(uri);
        } catch (final IOException ex) {
            throw new CoreException(new Status(IStatus.ERROR, "org.fuin.dsl.cqrs.ui",
                    "Cannot read '" + uri + "': " + ex.getMessage(), ex));
        }
    }

    /**
     * A path that reads like the location it stands for - <code>/the-artifact.zip/model/public/x.cqrs</code>.
     * It is not a workspace path and never resolves to one; the workbench uses it for the editor's
     * tooltip and for telling two inputs apart.
     */
    @Override
    public IPath getFullPath() {
        // "file:/…/cqrs-common-model-1.2.3.zip!" - the trailing '!' is the archive separator.
        final String authority = uri.authority();
        final URI archive = URI.createURI(authority.endsWith("!")
                ? authority.substring(0, authority.length() - 1)
                : authority);
        IPath path = new Path("/" + archive.lastSegment());
        for (final String segment : uri.segments()) {
            path = path.append(segment);
        }
        return path;
    }

    @Override
    public String getName() {
        return uri.lastSegment();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CqrsArchiveStorage && uri.equals(((CqrsArchiveStorage) obj).uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    @Override
    public String toString() {
        return uri.toString();
    }
}
