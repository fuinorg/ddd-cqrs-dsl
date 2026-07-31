package org.fuin.dsl.cqrs.ui.scoping;

import java.io.File;
import java.nio.file.Path;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolver;

/**
 * Resolves through <b>m2e</b>, i.e. through the Maven the Eclipse IDE already runs.
 *
 * <p>Everything the user configured for Maven in this IDE applies: the local repository location, the
 * remote repositories, mirrors, servers (authentication) and proxies of their
 * <code>settings.xml</code>. That is the whole point of borrowing m2e rather than shipping a second
 * Maven inside the plugin.</p>
 *
 * <p>It lives in the <b>UI</b> plugin, not in the language bundle. m2e drags Maven's own Guice onto
 * whatever requires it, and the language bundle is what the "Generate CqrsDsl Language
 * Infrastructure" MWE2 launch runs against - that Guice shadowed Xtext's and broke the launch with a
 * missing <code>javax.inject</code>. The language bundle is also the one mirrored into the plain Maven
 * jar, where m2e cannot exist at all. Registered through {@link CqrsDslUiModule}.</p>
 */
public final class M2eArtifactResolver implements CqrsArtifactResolver {

    @Override
    public Path resolve(String groupId, String artifactId, String version) throws Exception {
        final IMaven maven = MavenPlugin.getMaven();
        // No classifier: a model artifact is an ordinary jar.
        final org.apache.maven.artifact.Artifact artifact = maven.resolve(groupId, artifactId, version,
                CqrsArtifactResolver.EXTENSION, null, maven.getArtifactRepositories(), new NullProgressMonitor());
        if (artifact == null) {
            throw new IllegalStateException("m2e resolved nothing");
        }
        final File file = artifact.getFile();
        if (file == null) {
            throw new IllegalStateException("m2e resolved the artifact but it has no file");
        }
        return file.toPath();
    }
}
