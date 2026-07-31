package org.fuin.dsl.cqrs.scoping;

import java.nio.file.Path;

import org.apache.log4j.Logger;

import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;

import eu.maveniverse.maven.mima.context.Context;
import eu.maveniverse.maven.mima.context.ContextOverrides;
import eu.maveniverse.maven.mima.context.Runtimes;

/**
 * Resolves through <a href="https://github.com/maveniverse/mima">MIMA</a>, i.e. through Maven Resolver
 * configured the way Maven itself would configure it.
 *
 * <p>This is the implementation of the two environments that have no IDE to borrow a Maven from: the
 * console and the SrcGen4J Maven plugin. By default it reads the user's <code>settings.xml</code>, so
 * the local repository location, the remote repositories, mirrors, servers and proxies declared there
 * all apply.</p>
 *
 * <p><b>Inside a Maven build</b> the session of the running build is <em>not</em> inherited - the
 * SrcGen4J Mojo does not pass one down - so a <code>-o</code> or <code>-s other-settings.xml</code> on
 * that build does not reach here. Worse, the user settings cannot be read there either; see
 * {@link #createContext()} for why, and what is lost.</p>
 *
 * <p>This class lives only in the Maven tree; it is kept out of the Eclipse plugin (which uses m2e) by
 * the exclude list of <code>mirror-eclipse-sources-to-maven.sh</code>.</p>
 */
public final class MimaArtifactResolver implements CqrsArtifactResolver {

    private static final Logger LOG = Logger.getLogger(MimaArtifactResolver.class);

    private final ContextOverrides overrides;

    /** Resolver reading the user's <code>settings.xml</code>. */
    public MimaArtifactResolver() {
        this(ContextOverrides.create().withUserSettings(true).build());
    }

    /**
     * Resolver with explicit overrides - used by the console's <code>--settings</code> /
     * <code>--offline</code> options and by the tests, which point at a local repository of their own.
     *
     * @param overrides Overrides to create the MIMA context with.
     */
    public MimaArtifactResolver(ContextOverrides overrides) {
        this.overrides = overrides;
    }

    @Override
    public Path resolve(String groupId, String artifactId, String version) throws Exception {
        try (Context context = createContext()) {
            final ArtifactRequest request = new ArtifactRequest();
            request.setArtifact(new DefaultArtifact(groupId, artifactId, EXTENSION, version));
            request.setRepositories(context.remoteRepositories());

            final ArtifactResult result = context.repositorySystem()
                    .resolveArtifact(context.repositorySystemSession(), request);
            return result.getArtifact().getFile().toPath();
        }
    }

    /**
     * Creates the MIMA context, falling back to one without the user settings when reading them is
     * impossible.
     *
     * <p><b>Why the fallback exists.</b> Inside a Maven build - the SrcGen4J plugin - this jar sits in
     * the plugin's class realm next to Maven's own. Building the settings needs
     * {@code DefaultSettingsDecrypter}, and the plugin realm and Maven's realm each have their own copy
     * of its {@code SecDispatcher} parameter type, so linking the constructor fails with a
     * {@link LinkageError}. That is the known cost of running a standalone Resolver inside Maven, and
     * it is not something this side can fix without the Mojo handing its session down.</p>
     *
     * <p>Resolution then uses the default repositories and the local repository, which is enough for
     * anything already there or on Central - but a repository, mirror or credential declared only in
     * <code>settings.xml</code> will not apply. Standalone (the console) is unaffected: there is no
     * competing realm, and the settings are read normally.</p>
     */
    private Context createContext() {
        try {
            return Runtimes.INSTANCE.getRuntime().create(overrides);
        } catch (LinkageError error) {
            LOG.warn("Could not read the Maven settings (" + error.getClass().getSimpleName()
                    + "); resolving with the default repositories only. This happens inside a Maven"
                    + " build, where Maven's own classes and this jar's collide.");
            return Runtimes.INSTANCE.getRuntime().create(
                    ContextOverrides.create().withUserSettings(false).build());
        }
    }
}
