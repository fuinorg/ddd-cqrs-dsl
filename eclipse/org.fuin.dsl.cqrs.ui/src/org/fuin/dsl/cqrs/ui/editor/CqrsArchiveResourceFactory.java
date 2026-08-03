package org.fuin.dsl.cqrs.ui.editor;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.ResourceForIEditorInputFactory;

/**
 * Builds the EMF resource behind an editor, and keeps the <code>archive:</code> URI of a model that was
 * opened out of a <code>dependency</code>'s zip.
 * <p>
 * The inherited implementation derives the resource's URI from the storage's path
 * ({@code platform:/resource/…}), which assumes the storage <em>is</em> a workspace file. For a zip
 * entry that yields a URI addressing nothing, and the editor would show text whose elements cannot be
 * located - the reference that led there could not be revealed. Taking the URI the storage was built
 * from instead is what {@code JavaClassPathResourceForIEditorInputFactory} does for a jar entry, for
 * the same reason.
 * <p>
 * Such a resource is opened read-only, so linking diagnostics are switched off: a model of another
 * project is read, not authored, and a problem reported in it is not one the reader can fix. The
 * <code>@Check</code> rules are kept off it by {@code CqrsDslValidator} itself.
 */
public class CqrsArchiveResourceFactory extends ResourceForIEditorInputFactory {

    @Override
    protected Resource createResource(final IStorage storage) throws CoreException {
        if (storage instanceof CqrsArchiveStorage) {
            final URI uri = ((CqrsArchiveStorage) storage).getUri();
            final ResourceSet resourceSet = getResourceSet(storage);
            configureResourceSet(resourceSet, uri);
            final XtextResource resource = createResource(resourceSet, uri);
            resource.setValidationDisabled(true);
            return resource;
        }
        return super.createResource(storage);
    }

    /**
     * A model inside an archive belongs to no project of the workspace, so it gets the project-less
     * resource set. The inherited implementation would read a project name off the storage's path,
     * where the first segment is the name of a zip.
     */
    @Override
    protected ResourceSet getResourceSet(final IStorage storage) {
        if (storage instanceof CqrsArchiveStorage) {
            return getResourceSetProvider().get(null);
        }
        return super.getResourceSet(storage);
    }
}
