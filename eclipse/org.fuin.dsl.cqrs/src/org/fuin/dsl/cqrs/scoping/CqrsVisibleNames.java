package org.fuin.dsl.cqrs.scoping;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.fuin.dsl.cqrs.cqrsDsl.Context;
import org.fuin.dsl.cqrs.cqrsDsl.Import;
import org.fuin.dsl.cqrs.cqrsDsl.Module;

/**
 * Decides whether a declaration can be written at a given place <em>as it is named there</em>.
 * <p>
 * The scope a cross reference resolves against is deliberately wider than that: besides the names a
 * module reaches directly it also carries every element under its container-relative name
 * (<code>otherModule.Type</code>) and, through the global scope, every element of the workspace under
 * its fully qualified name. That is what makes a fully qualified reference work without an
 * <code>import</code> - but proposing all of it turns content assist into a dump of the whole
 * workspace.
 * <p>
 * This narrows it back down to what the module actually reaches: its own elements and whatever it -
 * or its context - imports. A nested name such as <code>Order.rename</code> stays proposable, because
 * it is still relative to the module.
 */
public final class CqrsVisibleNames {

    private CqrsVisibleNames() {
    }

    /**
     * Narrows a content assist filter to the names that are addressable at the given place.
     *
     * @param context Place the reference is written at - may be <code>null</code>.
     * @param delegate Filter to keep applying first - may be <code>null</code>.
     *
     * @return Filter that additionally requires {@link #isAddressable(EObject, IEObjectDescription)}.
     */
    public static Predicate<IEObjectDescription> addressableOnly(final EObject context,
            final Predicate<IEObjectDescription> delegate) {
        return new Predicate<IEObjectDescription>() {
            @Override
            public boolean apply(IEObjectDescription input) {
                if (delegate != null && !delegate.apply(input)) {
                    return false;
                }
                return isAddressable(context, input);
            }
        };
    }

    /**
     * Whether the given description is addressable from the given place under the name the scope
     * offers it as.
     *
     * @param context Place the reference is written at - may be <code>null</code>.
     * @param description Description offered by the scope - may be <code>null</code>.
     *
     * @return TRUE if the name may be written there as it stands.
     */
    public static boolean isAddressable(EObject context, IEObjectDescription description) {
        if (context == null || description == null) {
            return false;
        }
        QualifiedName name = description.getName();
        QualifiedName qualifiedName = description.getQualifiedName();
        if (name == null || qualifiedName == null) {
            return false;
        }
        String shortName = name.toString();
        String fqn = qualifiedName.toString();
        if (!fqn.endsWith("." + shortName)) {
            return false; // offered under its full name, i.e. only through the global scope
        }
        // What the offered name is relative to.
        String prefix = fqn.substring(0, fqn.length() - shortName.length() - 1);

        // The module's own elements, including one nested deeper ("Order.rename").
        if (prefix.equals(moduleFqn(context))) {
            return true;
        }
        // Everything else has to arrive through an import, which always yields a simple name: an
        // element the scope also offers as "otherModule.Type" is reachable, but not written that way.
        if (name.getSegmentCount() != 1) {
            return false;
        }
        for (String imported : importedNames(context)) {
            if (imported.endsWith(".*")) {
                // A wildcard reaches the module it names, and - for a context wide import - every
                // module below it.
                String wildcardPrefix = imported.substring(0, imported.length() - 2);
                if (prefix.equals(wildcardPrefix) || prefix.startsWith(wildcardPrefix + ".")) {
                    return true;
                }
            } else if (imported.equals(fqn)) {
                return true;
            }
        }
        return false;
    }

    /** Whether an imported name covers a fully qualified name (exact, or below a wildcard). */
    public static boolean covers(String imported, String qualifiedName) {
        if (imported == null || qualifiedName == null) {
            return false;
        }
        if (imported.endsWith(".*")) {
            return qualifiedName.startsWith(imported.substring(0, imported.length() - 1));
        }
        return imported.equals(qualifiedName);
    }

    /** Fully qualified name of the module enclosing the given object, or <code>null</code>. */
    public static String moduleFqn(EObject obj) {
        Module module = enclosing(obj, Module.class);
        if (module == null || module.getName() == null) {
            return null;
        }
        Context context = enclosing(module, Context.class);
        if (context == null || context.getName() == null) {
            return module.getName();
        }
        return context.getName() + "." + module.getName();
    }

    /** The names imported by the enclosing module and by the enclosing context, in that order. */
    public static List<String> importedNames(EObject obj) {
        List<String> result = new ArrayList<>();
        Module module = enclosing(obj, Module.class);
        if (module != null) {
            add(result, module.getImports());
        }
        Context context = enclosing(obj, Context.class);
        if (context != null) {
            add(result, context.getImports());
        }
        return result;
    }

    private static void add(List<String> target, List<Import> imports) {
        if (imports == null) {
            return;
        }
        for (Import imp : imports) {
            if (imp != null && imp.getImportedNamespace() != null) {
                target.add(imp.getImportedNamespace());
            }
        }
    }

    private static <T> T enclosing(EObject obj, Class<T> type) {
        for (EObject cur = obj; cur != null; cur = cur.eContainer()) {
            if (type.isInstance(cur)) {
                return type.cast(cur);
            }
        }
        return null;
    }
}
