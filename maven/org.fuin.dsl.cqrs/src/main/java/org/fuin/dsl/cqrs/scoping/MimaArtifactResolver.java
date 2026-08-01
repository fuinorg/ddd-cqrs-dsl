package org.fuin.dsl.cqrs.scoping;

import java.nio.file.Path;


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
 * <p><b>Inside a Maven build</b> MIMA's <code>embedded-maven</code> runtime takes over: it activates
 * only when running within Maven and hands out the repository system, the session and the repositories
 * of the running build, so its settings, mirrors, credentials and its <code>-o</code> / <code>-s</code>
 * apply. That runtime is what makes this work in a plugin realm at all - the standalone one builds a
 * <code>SettingsDecrypter</code> while starting up, and linking it fails there because Maven's own copy
 * of <code>SecDispatcher</code> competes with this jar's.</p>
 *
 * <p>This class lives only in the Maven tree; it is kept out of the Eclipse plugin (which uses m2e) by
 * the exclude list of <code>mirror-eclipse-sources-to-maven.sh</code>.</p>
 */
public final class MimaArtifactResolver implements CqrsArtifactResolver {

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
        try (Context context = Runtimes.INSTANCE.getRuntime().create(overrides)) {
            final ArtifactRequest request = new ArtifactRequest();
            request.setArtifact(new DefaultArtifact(groupId, artifactId, EXTENSION, version));
            request.setRepositories(context.remoteRepositories());

            final ArtifactResult result = context.repositorySystem()
                    .resolveArtifact(context.repositorySystemSession(), request);
            return result.getArtifact().getFile().toPath();
        }
    }
}
