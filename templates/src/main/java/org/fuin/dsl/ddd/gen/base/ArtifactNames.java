package org.fuin.dsl.ddd.gen.base;

/**
 * Derives the base name used for generated artifacts from a DSL element name by stripping a
 * redundant trailing role suffix, so a {@code view PersonListView} and a {@code view PersonList}
 * both generate {@code PersonListView} / {@code PersonListController} / ... instead of doubling the
 * suffix ({@code PersonListViewView}).
 */
public final class ArtifactNames {

    private ArtifactNames() {
    }

    /**
     * Strips the first matching suffix (checked in the given order, so pass the longest first) from
     * {@code name}. The suffix is only removed when it leaves a non-empty remainder, so a name that
     * equals a suffix is returned unchanged.
     *
     * @param name     element name (must not be null).
     * @param suffixes candidate suffixes, checked in order.
     *
     * @return name without the first matching suffix, or the unchanged name.
     */
    public static String stripSuffix(final String name, final String... suffixes) {
        if (name == null) {
            throw new IllegalArgumentException("argument 'name' cannot be null");
        }
        for (final String suffix : suffixes) {
            if (suffix != null && !suffix.isEmpty()
                    && name.length() > suffix.length() && name.endsWith(suffix)) {
                return name.substring(0, name.length() - suffix.length());
            }
        }
        return name;
    }

    /**
     * Base name for a {@code view}: the DSL name without a trailing {@code "View"}.
     *
     * @param viewName DSL view name.
     *
     * @return base name (e.g. {@code "PersonListView"} and {@code "PersonList"} both give {@code "PersonList"}).
     */
    public static String viewBaseName(final String viewName) {
        return stripSuffix(viewName, "View");
    }

    /**
     * Base name for a {@code process-manager}: the DSL name without a trailing {@code "ProcessManager"}
     * or {@code "Process"} ({@code "ProcessManager"} is checked first as it is the longer suffix).
     *
     * @param processManagerName DSL process-manager name.
     *
     * @return base name (e.g. {@code "OrderPaymentProcess"}, {@code "OrderPaymentProcessManager"} and
     *         {@code "OrderPayment"} all give {@code "OrderPayment"}).
     */
    public static String processManagerBaseName(final String processManagerName) {
        return stripSuffix(processManagerName, "ProcessManager", "Process");
    }

    /**
     * Code-reference-registry key under which a view's generated REST contract interface
     * ({@code <Base>ControllerApi} / {@code <Base>ResourceApi}) is registered, so that the concrete
     * controller/resource (a different module/package) can import it. Runtime-independent so the
     * interface factory and the implementing-class factory agree on the same key.
     *
     * @param viewUniqueName Unique name of the view (see CqrsAbstractElementExtensions.uniqueName).
     *
     * @return Reference key.
     */
    public static String restApiRefKey(final String viewUniqueName) {
        return viewUniqueName + "#RestApi";
    }
}
