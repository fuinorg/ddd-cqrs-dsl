package org.fuin.dsl.cqrs.scoping;

import java.nio.file.Path;

/**
 * Resolves the Maven artifact that carries the models of a <code>dependency</code>.
 *
 * <p>The artifact is an ordinary jar - no classifier - whose <code>model/</code> folder holds the
 * <code>.cqrs</code> files. It is <em>not</em> unpacked: the models are read in place, straight out of
 * the jar in the local repository, so this only has to answer <em>where that jar is</em>.</p>
 *
 * <p>There is one implementation per environment, because each of them already has a Maven:
 * {@code M2eArtifactResolver} in the Eclipse plugin and {@code MimaArtifactResolver} in the Maven jar
 * (which serves both the console and the SrcGen4J Maven plugin). {@link CqrsArtifactResolvers} picks
 * whichever is present. Every one of them honours the user's <code>settings.xml</code> - mirrors,
 * servers, proxies and the local repository location - which the hand written downloader this replaces
 * never did.</p>
 */
public interface CqrsArtifactResolver {

    /** Packaging of a model artifact. It carries no classifier. */
    String EXTENSION = "jar";

    /** Folder inside the artifact that holds the <code>.cqrs</code> files. */
    String MODEL_DIR = "model";

    /**
     * Resolves an artifact, downloading it into the local repository if it is not there yet.
     *
     * @param groupId Group id.
     * @param artifactId Artifact id.
     * @param version Version.
     *
     * @return Path of the artifact in the local repository, never <code>null</code>.
     *
     * @throws Exception If the artifact cannot be resolved. The message is shown to the user on the
     *             <code>dependency</code> coordinate, so it should be the resolver's own diagnosis.
     */
    Path resolve(String groupId, String artifactId, String version) throws Exception;
}
