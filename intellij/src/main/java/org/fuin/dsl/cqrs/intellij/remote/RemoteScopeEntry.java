package org.fuin.dsl.cqrs.intellij.remote;

/**
 * A single typed source entry of the <code>dependencies.json</code> catalog: it describes <em>where
 * one or more namespaces live</em>, selected by a <code>type</code> discriminator. The namespaces
 * an entry provides are kept by the catalog (one map key per namespace), not on the entry itself.
 *
 * <p>Two types are supported:</p>
 * <ul>
 * <li><b>simple</b> &ndash; a single <code>.cqrs</code> file referenced by {@link #getUrl() url}
 * (a <code>file:</code> or <code>http(s):</code> URL).</li>
 * <li><b>maven</b> &ndash; a Maven artifact ({@link #getGroupId() groupId}/{@link #getArtifactId()
 * artifactId}/{@link #getVersion() version}) with classifier <code>cqrs</code> and type
 * <code>tar.gz</code> whose archive contains one or more <code>.cqrs</code> files at the top
 * level.</li>
 * </ul>
 *
 * <p>This class is plain JDK code (no Gson, EMF or IntelliJ types) so the same source can be shared
 * verbatim by the Maven and Eclipse projects.</p>
 */
public final class RemoteScopeEntry {

    /** Discriminator value for a single-file source. */
    public static final String TYPE_SIMPLE = "simple";

    /** Discriminator value for a Maven artifact source. */
    public static final String TYPE_MAVEN = "maven";

    private final String type;
    private final String url;
    private final String groupId;
    private final String artifactId;
    private final String version;

    private RemoteScopeEntry(String type, String url, String groupId, String artifactId,
            String version) {
        this.type = type;
        this.url = url;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    /** Creates a {@link #TYPE_SIMPLE simple} entry whose model is fetched from {@code url}. */
    public static RemoteScopeEntry simple(String url) {
        return new RemoteScopeEntry(TYPE_SIMPLE, url, null, null, null);
    }

    /** Creates a {@link #TYPE_MAVEN maven} entry resolving its model(s) from the given artifact. */
    public static RemoteScopeEntry maven(String groupId, String artifactId, String version) {
        return new RemoteScopeEntry(TYPE_MAVEN, null, groupId, artifactId, version);
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    /**
     * A canonical identity for the source, used as the cache key (a directory named after its SHA-1)
     * and stored in the cache index. Returns <code>null</code> for an unknown type.
     */
    public String getSourceId() {
        if (TYPE_SIMPLE.equals(type)) {
            return url;
        }
        if (TYPE_MAVEN.equals(type)) {
            return "mvn:" + groupId + ":" + artifactId + ":" + version + ":" + MavenArtifactResolver.CLASSIFIER
                    + ":" + MavenArtifactResolver.EXTENSION;
        }
        return null;
    }

    @Override
    public String toString() {
        return getSourceId();
    }
}
