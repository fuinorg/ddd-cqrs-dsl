package org.fuin.dsl.cqrs.scoping;

import java.util.Arrays;
import java.util.List;

/**
 * Finds the {@link CqrsArtifactResolver} of the environment the DSL is running in.
 *
 * <p>An IDE <b>pushes</b> its resolver in with {@link #set(CqrsArtifactResolver)} - the Eclipse UI
 * plugin registers the m2e one, the IntelliJ plugin has its own path - because their Maven integration
 * must not be on this bundle's class path. m2e in particular re-exports Maven's own Guice, which
 * shadows Xtext's and breaks the MWE2 language-generation launch.</p>
 *
 * <p>Everywhere else the resolver is found by probing class names rather than through a
 * {@link java.util.ServiceLoader}: {@code MimaArtifactResolver} exists only in the Maven jar (it is
 * kept out of the Eclipse tree by the exclude list of
 * <code>mirror-eclipse-sources-to-maven.sh</code>), so probing needs no service registry and, unlike
 * {@code ServiceLoader}, no thought about OSGi class loading.</p>
 */
public final class CqrsArtifactResolvers {

    private static final List<String> CANDIDATES = Arrays.asList(
            "org.fuin.dsl.cqrs.scoping.MimaArtifactResolver");

    private static CqrsArtifactResolver instance;

    private CqrsArtifactResolvers() {
    }

    /**
     * The resolver of this environment, created once and kept.
     *
     * @return Resolver, never <code>null</code>.
     *
     * @throws IllegalStateException If none of the implementations is on the class path, which means
     *             the DSL was embedded without a Maven to resolve with.
     */
    public static synchronized CqrsArtifactResolver get() {
        if (instance == null) {
            instance = create();
        }
        return instance;
    }

    /** Replaces the resolver. Intended for tests; pass <code>null</code> to fall back to probing. */
    public static synchronized void set(CqrsArtifactResolver resolver) {
        instance = resolver;
    }

    private static CqrsArtifactResolver create() {
        for (final String name : CANDIDATES) {
            try {
                return (CqrsArtifactResolver) Class.forName(name).getDeclaredConstructor().newInstance();
            } catch (final ClassNotFoundException | NoClassDefFoundError ex) {
                // Not this environment - try the next one.
            } catch (final Exception ex) {
                throw new IllegalStateException("Found '" + name + "' but could not create it", ex);
            }
        }
        throw new IllegalStateException(
                "No Maven artifact resolver available (tried " + CANDIDATES + "). An IDE registers one "
                        + "with set(...); everything else needs the MIMA runtime on its class path.");
    }
}
