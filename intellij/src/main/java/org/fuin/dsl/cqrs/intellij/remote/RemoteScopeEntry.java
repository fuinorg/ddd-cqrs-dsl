package org.fuin.dsl.cqrs.intellij.remote;

/**
 * A single typed source entry of the <code>dependencies.json</code> catalog: it describes <em>where
 * one or more namespaces live</em>, selected by a <code>type</code> discriminator. The namespaces
 * an entry provides are kept by the catalog (one map key per namespace), not on the entry itself.
 *
 * <p>Only the <b>maven</b> type is supported: a Maven artifact ({@link #getGroupId() groupId}/
 * {@link #getArtifactId() artifactId}/{@link #getVersion() version}) with classifier
 * <code>cqrs</code> and type <code>tar.gz</code> whose archive contains one or more
 * <code>.cqrs</code> files at the top level. As an override, {@link #getLocal() local} may point at a
 * local directory of <code>.cqrs</code> files that is read directly instead of downloading the
 * artifact.</p>
 *
 * <p>This class is plain JDK code (no Gson, EMF or IntelliJ types) so the same source can be shared
 * verbatim by the Maven and Eclipse projects.</p>
 */
public final class RemoteScopeEntry {

    /** Discriminator value for a Maven artifact source. */
    public static final String TYPE_MAVEN = "maven";

    private final String type;
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String local;

    private RemoteScopeEntry(String type, String groupId, String artifactId, String version,
            String local) {
        this.type = type;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.local = local;
    }

    /** Creates a {@link #TYPE_MAVEN maven} entry resolving its model(s) from the given artifact. */
    public static RemoteScopeEntry maven(String groupId, String artifactId, String version) {
        return maven(groupId, artifactId, version, null);
    }

    /**
     * Creates a {@link #TYPE_MAVEN maven} entry. When {@code local} is non-<code>null</code> the
     * model(s) are read directly from that local directory instead of downloading the artifact.
     */
    public static RemoteScopeEntry maven(String groupId, String artifactId, String version, String local) {
        return new RemoteScopeEntry(TYPE_MAVEN, groupId, artifactId, version, local);
    }

    public String getType() {
        return type;
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

    /** Local directory of <code>.cqrs</code> files to read instead of the artifact, or <code>null</code>. */
    public String getLocal() {
        return local;
    }

    /**
     * A canonical identity for the source, used as the cache key (a directory named after its SHA-1)
     * and stored in the cache index. Returns <code>null</code> for an unknown type.
     */
    public String getSourceId() {
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
