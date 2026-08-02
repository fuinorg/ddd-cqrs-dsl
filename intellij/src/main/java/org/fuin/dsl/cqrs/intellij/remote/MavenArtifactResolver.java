package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.model.MavenArtifact;
import org.jetbrains.idea.maven.model.MavenArtifactInfo;
import org.jetbrains.idea.maven.model.MavenRemoteRepository;
import org.jetbrains.idea.maven.project.MavenEmbeddersManager;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.server.MavenEmbedderWrapper;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Resolves the artifact of a <code>dependency</code> through the IDE's own Maven.
 *
 * <p>Everything the user configured for Maven in this IDE applies - the local repository, the remote
 * repositories, mirrors, servers (authentication) and proxies of their <code>settings.xml</code> -
 * which is why the plugin borrows the bundled Maven plugin instead of shipping a second Maven. It is
 * the IntelliJ counterpart of the Eclipse plugin's use of m2e.</p>
 *
 * <p>The artifact is a plain zip with no classifier; the models live inside it under
 * <code>model/</code> and are never unpacked (see {@link CqrsModelArchives}).</p>
 *
 * <p><b>Threading:</b> this contacts the Maven server and may download, so it must only be called from
 * a background thread - {@link CqrsRemoteScopeResolver} does so from its pooled-thread warming.</p>
 */
public final class MavenArtifactResolver {

    /** Packaging of a model artifact. It carries no classifier. */
    public static final String EXTENSION = "zip";

    private final Project project;

    public MavenArtifactResolver(@NotNull Project project) {
        this.project = project;
    }

    /**
     * Resolves an artifact, downloading it into the local repository if needed.
     *
     * @param groupId Group id.
     * @param artifactId Artifact id.
     * @param version Version.
     *
     * @return Path of the artifact in the local repository.
     *
     * @throws Exception If it cannot be resolved.
     */
    public @NotNull Path resolve(String groupId, String artifactId, String version) throws Exception {
        final MavenProjectsManager manager = MavenProjectsManager.getInstance(project);
        final List<MavenRemoteRepository> repositories = new ArrayList<>(manager.getRemoteRepositories());
        final MavenEmbedderWrapper embedder = manager.getEmbeddersManager()
                .getEmbedder(MavenEmbeddersManager.FOR_DEPENDENCIES_RESOLVE, project.getBasePath());
        try {
            // No classifier - the models are the archive itself.
            final MavenArtifact artifact = embedder.resolve(
                    new MavenArtifactInfo(groupId, artifactId, version, EXTENSION, null), repositories);
            if (artifact == null) {
                throw new IllegalStateException("Maven resolved nothing");
            }
            final File file = artifact.getFile();
            if (file == null || !file.isFile()) {
                throw new IllegalStateException("the artifact is not in the local repository");
            }
            return file.toPath();
        } finally {
            manager.getEmbeddersManager().release(embedder);
        }
    }
}
