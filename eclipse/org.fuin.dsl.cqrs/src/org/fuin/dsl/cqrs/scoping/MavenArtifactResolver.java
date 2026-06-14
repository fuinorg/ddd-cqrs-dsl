package org.fuin.dsl.cqrs.scoping;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Resolves a Maven artifact with classifier <code>cqrs</code> and type <code>tar.gz</code> to an
 * {@link InputStream}. The local repository (<code>~/.m2/repository</code>) is consulted first; on a
 * miss the artifact is downloaded from Maven Central (releases) or Sonatype Snapshots (SNAPSHOTs).
 *
 * <p>For a SNAPSHOT version the timestamped file name is looked up in <code>maven-metadata.xml</code>
 * using the JDK DOM parser. Everything here is plain JDK code (no Aether/Maven Resolver and no Apache
 * Commons), so the same source can be shared verbatim by the Maven and Eclipse projects.</p>
 *
 * <p>The repository base URLs and the local repository location can be overridden with the system
 * properties <code>cqrs.maven.repo.snapshots</code>, <code>cqrs.maven.repo.releases</code> and
 * <code>maven.repo.local</code> (mainly to point tests at a local server).</p>
 */
public final class MavenArtifactResolver {

    /** Classifier of the published CQRS model archive. */
    public static final String CLASSIFIER = "cqrs";

    /** Packaging/extension of the published CQRS model archive. */
    public static final String EXTENSION = "tar.gz";

    private static final String CENTRAL = "https://repo.maven.apache.org/maven2/";

    private static final String SNAPSHOTS = "https://central.sonatype.com/repository/maven-snapshots/";

    /**
     * Opens the artifact content, preferring the local repository and otherwise downloading from the
     * matching remote repository. The caller owns the returned stream and must close it.
     */
    public InputStream openArtifact(String groupId, String artifactId, String version) throws Exception {
        File local = localFile(groupId, artifactId, version);
        if (local != null && local.isFile()) {
            return new FileInputStream(local);
        }
        String url = remoteUrl(groupId, artifactId, version);
        return new URI(url).toURL().openStream();
    }

    private File localFile(String groupId, String artifactId, String version) {
        File repo = localRepo();
        if (repo == null) {
            return null;
        }
        String path = groupId.replace('.', '/') + '/' + artifactId + '/' + version + '/' + artifactId
                + '-' + version + '-' + CLASSIFIER + '.' + EXTENSION;
        return new File(repo, path);
    }

    private static File localRepo() {
        String override = System.getProperty("maven.repo.local");
        if (override != null && !override.isEmpty()) {
            return new File(override);
        }
        String home = System.getProperty("user.home");
        return (home == null || home.isEmpty()) ? null : new File(home, ".m2/repository");
    }

    private static String remoteUrl(String groupId, String artifactId, String version) {
        boolean snapshot = version.endsWith("-SNAPSHOT");
        String dir = baseUrl(snapshot) + groupId.replace('.', '/') + '/' + artifactId + '/' + version + '/';
        if (snapshot) {
            return dir + snapshotFileName(dir, artifactId, version);
        }
        return dir + artifactId + '-' + version + '-' + CLASSIFIER + '.' + EXTENSION;
    }

    private static String baseUrl(boolean snapshot) {
        String url = snapshot ? System.getProperty("cqrs.maven.repo.snapshots", SNAPSHOTS)
                : System.getProperty("cqrs.maven.repo.releases", CENTRAL);
        return url.endsWith("/") ? url : url + '/';
    }

    /**
     * Maps a SNAPSHOT version to its timestamped artifact file name by reading
     * <code>maven-metadata.xml</code>. Falls back to the plain <code>-SNAPSHOT</code> file name when
     * the metadata is missing or unreadable (e.g. a locally installed SNAPSHOT mirrored on a server).
     */
    private static String snapshotFileName(String dir, String artifactId, String version) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            NodeList versions = factory.newDocumentBuilder().parse(dir + "maven-metadata.xml")
                    .getElementsByTagName("snapshotVersion");
            for (int i = 0; i < versions.getLength(); i++) {
                Element el = (Element) versions.item(i);
                if (CLASSIFIER.equals(text(el, "classifier")) && EXTENSION.equals(text(el, "extension"))) {
                    String value = text(el, "value");
                    if (value != null && !value.isEmpty()) {
                        return artifactId + '-' + value + '-' + CLASSIFIER + '.' + EXTENSION;
                    }
                }
            }
        } catch (Exception ex) {
            // fall back to the non-timestamped name below
        }
        return artifactId + '-' + version + '-' + CLASSIFIER + '.' + EXTENSION;
    }

    private static String text(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        return list.getLength() > 0 ? list.item(0).getTextContent() : null;
    }
}
