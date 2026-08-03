package org.fuin.dsl.cqrs.scoping;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.Module;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * The names that may follow an <code>import</code>: every reachable context and module as a wildcard,
 * plus every single type.
 * <p>
 * Two sources, the same two the scope resolves against: the Xtext index, so models of another file
 * are included, and whatever the declared dependencies provide - an IDE index never holds those,
 * because they are read out of an artifact's zip. The module the import is written in is left out
 * (importing it would be redundant), as is anything it already imports.
 */
@Singleton
public class CqrsImportProposals {

    @Inject
    private ResourceDescriptionsProvider resourceDescriptionsProvider;

    @Inject
    private IContainer.Manager containerManager;

    @Inject
    private CqrsDependencies dependencies;

    /**
     * Proposals for an import written at the given place, alphabetically.
     *
     * @param context Place the import is written at - may be <code>null</code>.
     *
     * @return Importable names, never <code>null</code>.
     */
    public List<String> candidates(EObject context) {
        Set<String> result = new TreeSet<>();
        if (context == null) {
            return new ArrayList<>(result);
        }
        Resource resource = context.eResource();
        if (resource == null || resource.getResourceSet() == null) {
            return new ArrayList<>(result);
        }
        IResourceDescriptions descriptions = resourceDescriptionsProvider
                .getResourceDescriptions(resource.getResourceSet());
        IResourceDescription self = descriptions.getResourceDescription(resource.getURI());

        List<String> elements = new ArrayList<>();
        Set<String> modules = new LinkedHashSet<>();
        if (self != null) {
            for (IContainer container : containerManager.getVisibleContainers(self, descriptions)) {
                for (IEObjectDescription description : container.getExportedObjects()) {
                    if (description.getName() == null) {
                        continue;
                    }
                    String name = description.getName().toString();
                    // Kept on the description: resolving the proxy would load every model in the index.
                    if (isModuleDescription(description)) {
                        result.add(name + ".*");
                        modules.add(name);
                    } else if (isContextDescription(description)) {
                        result.add(name + ".*");
                    } else {
                        elements.add(name);
                    }
                }
            }
        }
        // What the declared dependencies provide. The index cannot answer for those in an IDE - it
        // holds workspace files and never a model read out of an artifact's zip - so without this a
        // dependency's types would simply never be proposed.
        for (Map.Entry<QualifiedName, EObject> provided : dependencies.providedElements(resource).entrySet()) {
            String name = provided.getKey().toString();
            EObject element = provided.getValue();
            if (element instanceof Module) {
                result.add(name + ".*");
                modules.add(name);
            } else if (element instanceof Context) {
                result.add(name + ".*");
            } else {
                elements.add(name);
            }
        }

        String ownModule = CqrsVisibleNames.moduleFqn(context);
        if (ownModule != null) {
            result.remove(ownModule + ".*");
        }
        for (String name : elements) {
            if (!owningModule(name, modules).equals(nullToEmpty(ownModule))) {
                result.add(name);
            }
        }
        result.removeAll(CqrsVisibleNames.importedNames(context));
        return new ArrayList<>(result);
    }

    /**
     * The module an element belongs to: the longest module name that is a prefix of its qualified
     * name. Comparing prefixes is what keeps a module named <code>a.b</code> apart from one named
     * <code>a.b.c</code>.
     */
    private static String owningModule(String qualifiedName, Set<String> modules) {
        String longest = "";
        for (String module : modules) {
            if (qualifiedName.startsWith(module + ".") && module.length() > longest.length()) {
                longest = module;
            }
        }
        return longest;
    }

    private static boolean isModuleDescription(IEObjectDescription description) {
        return description.getEClass() != null
                && Module.class.getSimpleName().equals(description.getEClass().getName());
    }

    private static boolean isContextDescription(IEObjectDescription description) {
        return description.getEClass() != null
                && Context.class.getSimpleName().equals(description.getEClass().getName());
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
