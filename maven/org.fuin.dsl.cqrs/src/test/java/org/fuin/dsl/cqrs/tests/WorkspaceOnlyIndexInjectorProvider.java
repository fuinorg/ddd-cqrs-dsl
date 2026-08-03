package org.fuin.dsl.cqrs.tests;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.containers.FlatResourceSetBasedAllContainersState;
import org.eclipse.xtext.resource.containers.IAllContainersState;
import org.eclipse.xtext.resource.containers.ResourceSetBasedAllContainersStateProvider;
import org.fuin.dsl.cqrs.CqrsDslRuntimeModule;
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives;

/**
 * An injector whose Xtext index behaves the way an IDE's does: it knows the files of the project and
 * <em>never</em> a model that was read out of a dependency's archive.
 * <p>
 * That difference is the whole reason this class exists. Headless the index is backed by the resource
 * set, and a dependency's models are loaded into it while the cross references are resolved - so
 * everything the artifact provides happens to be in the index as well, and a check that only ever asks
 * the index still gets the right answer. In Eclipse the index is the JDT/builder state, which is fed
 * from workspace files alone; a model inside a zip in the local Maven repository is never in it. Code
 * that consults the index and nothing else is therefore correct headless and wrong in the editor,
 * which is exactly the class of bug that cannot be reproduced with the default test setup.
 * <p>
 * Dropping the <code>archive:</code> URIs from the containers reproduces it: the resource set still
 * holds the dependency's models, so scoping resolves against them as it always did, but the index no
 * longer offers them.
 */
public class WorkspaceOnlyIndexInjectorProvider extends CqrsDslInjectorProvider {

    @Override
    protected CqrsDslRuntimeModule createRuntimeModule() {
        return new CqrsDslRuntimeModule() {
            @Override
            public ClassLoader bindClassLoaderToInstance() {
                return WorkspaceOnlyIndexInjectorProvider.this.getClass().getClassLoader();
            }

            @Override
            public Class<? extends IAllContainersState.Provider> bindIAllContainersState$Provider() {
                return WorkspaceOnlyContainersStateProvider.class;
            }
        };
    }

    /** Hands out a container state that hides everything living inside an archive. */
    public static class WorkspaceOnlyContainersStateProvider extends ResourceSetBasedAllContainersStateProvider {

        @Override
        protected IAllContainersState handleAdapterNotFound(final ResourceSet resourceSet) {
            return new FlatResourceSetBasedAllContainersState(resourceSet) {

                @Override
                public Collection<URI> getContainedURIs(String containerHandle) {
                    Collection<URI> result = new LinkedHashSet<>();
                    for (URI uri : super.getContainedURIs(containerHandle)) {
                        if (!CqrsModelArchives.isArchived(uri)) {
                            result.add(uri);
                        }
                    }
                    return result;
                }

                @Override
                public boolean containsURI(String containerHandle, URI candidateURI) {
                    return !CqrsModelArchives.isArchived(candidateURI)
                            && super.containsURI(containerHandle, candidateURI);
                }
            };
        }
    }
}
