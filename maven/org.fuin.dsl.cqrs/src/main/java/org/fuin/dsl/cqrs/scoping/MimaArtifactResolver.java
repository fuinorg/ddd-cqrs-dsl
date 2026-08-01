package org.fuin.dsl.cqrs.scoping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import org.eclipse.aether.artifact.Artifact;
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

    /** Guards the warning about reduced resolution, so it is logged once and not per artifact. */
    private static final AtomicBoolean WARNED = new AtomicBoolean();

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
        final Artifact artifact = new DefaultArtifact(groupId, artifactId, EXTENSION, version);
        try (Context context = Runtimes.INSTANCE.getRuntime().create(overrides)) {
            final ArtifactRequest request = new ArtifactRequest();
            request.setArtifact(artifact);
            request.setRepositories(context.remoteRepositories());

            final ArtifactResult result = context.repositorySystem()
                    .resolveArtifact(context.repositorySystemSession(), request);
            return result.getArtifact().getFile().toPath();
        } catch (LinkageError error) {
            return resolveFromLocalRepository(artifact, error);
        }
    }

    /**
     * Finds the artifact in the local repository, for the one environment where MIMA cannot be built:
     * inside a Maven build.
     *
     * <p><b>Why this is needed.</b> In the SrcGen4J plugin this jar sits in the plugin's class realm
     * next to Maven's own. MIMA's standalone runtime builds a {@code SettingsDecrypter} while it starts
     * up - unconditionally, so asking it for a context {@code withUserSettings(false)} does not avoid
     * it - and linking {@code DefaultSettingsDecrypter(SecDispatcher)} fails, because the plugin realm
     * and Maven's realm each hold their own copy of {@code SecDispatcher}.</p>
     *
     * <p>Building a Resolver of our own instead does not work either: Maven provides
     * {@code org.apache.maven.resolver:*} from its core realm and keeps those artifacts out of every
     * plugin realm, so the connector and transport implementations a standalone Resolver needs are not
     * there to be loaded. What is left is the local repository, which is enough in practice - inside a
     * Maven build the model artifact has been installed or resolved by the surrounding build already.
     * Standalone (the console) never gets here: there is no competing realm and MIMA resolves normally,
     * downloads included.</p>
     *
     * @param artifact Artifact to look up.
     * @param error Error that made the MIMA path impossible - reported once, so the reduced resolution
     *            is never silent.
     *
     * @return Path of the artifact in the local repository.
     *
     * @throws Exception If it is not there, naming the command that puts it there.
     */
    private Path resolveFromLocalRepository(Artifact artifact, LinkageError error) throws Exception {
        if (WARNED.compareAndSet(false, true)) {
            LOG.warn("Maven's own classes and this jar's collide in the plugin class realm ("
                    + error.getClass().getSimpleName() + "), so a dependency is looked up in the local"
                    + " repository instead of being resolved. Nothing is downloaded here; an artifact"
                    + " that is not installed yet has to be fetched by the surrounding build.");
        }

        final Path dir = localRepository()
                .resolve(artifact.getGroupId().replace('.', '/'))
                .resolve(artifact.getArtifactId())
                .resolve(artifact.getVersion());
        final Path jar = dir.resolve(artifact.getArtifactId() + "-" + artifact.getVersion() + "."
                + artifact.getExtension());
        if (Files.isRegularFile(jar)) {
            return jar;
        }

        // A snapshot installed from a remote build carries a timestamp instead of "-SNAPSHOT".
        final Path newest = newestSnapshot(dir, artifact);
        if (newest != null) {
            return newest;
        }

        throw new IllegalStateException("'" + artifact + "' is not in the local repository (" + dir
                + "). Inside a Maven build nothing can be downloaded from here - run"
                + " 'mvn dependency:get -Dartifact=" + artifact.getGroupId() + ":"
                + artifact.getArtifactId() + ":" + artifact.getVersion() + "' once, or declare the"
                + " model as a dependency of this build.");
    }

    /** Newest timestamped snapshot file in the given directory, or <code>null</code> if there is none. */
    private static Path newestSnapshot(Path dir, Artifact artifact) throws Exception {
        if (!Files.isDirectory(dir)) {
            return null;
        }
        final String prefix = artifact.getArtifactId() + "-";
        final String suffix = "." + artifact.getExtension();
        try (Stream<Path> files = Files.list(dir)) {
            return files.filter(Files::isRegularFile)
                    .filter(f -> f.getFileName().toString().startsWith(prefix)
                            && f.getFileName().toString().endsWith(suffix))
                    .max(Comparator.comparing(f -> f.getFileName().toString()))
                    .orElse(null);
        }
    }

    /** Local repository of the running build, or the default one. */
    private static Path localRepository() {
        final String configured = System.getProperty("maven.repo.local");
        if (configured != null && !configured.isEmpty()) {
            return Paths.get(configured);
        }
        return Paths.get(System.getProperty("user.home"), ".m2", "repository");
    }

}
