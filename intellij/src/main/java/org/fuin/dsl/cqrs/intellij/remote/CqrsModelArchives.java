package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Turns a <code>dependency</code> into the <code>.cqrs</code> files it provides.
 *
 * <p>A Maven dependency is resolved to its zip in the local repository and the models are then read
 * <em>inside</em> that archive through {@link JarFileSystem}, which mounts a zip like any other one -
 * nothing is unpacked, and because the entries are real {@link VirtualFile}s the usual PSI, navigation
 * and find-usages all work on them. Only entries below {@code model/} count, taken recursively so a
 * model may sit in a sub folder.</p>
 *
 * <p>A dependency with a <code>local</code> clause skips resolution entirely and reads a directory of
 * <code>.cqrs</code> files, relative to the model that declares it.</p>
 *
 * <p>What an artifact resolved to is remembered for the session, success and failure alike, so a
 * coordinate that cannot be resolved is attempted once rather than on every keystroke.</p>
 */
public class CqrsModelArchives {

    private static final Logger LOG = Logger.getInstance(CqrsModelArchives.class);

    /** Folder inside the artifact holding the models. */
    public static final String MODEL_DIR = "model";

    private static final String CQRS_EXTENSION = ".cqrs";

    private final Project project;

    /** Archive of an artifact, keyed by {@link RemoteScopeEntry#getSourceId}. */
    private final Map<String, Path> resolved = new ConcurrentHashMap<>();

    /** Why an artifact could not be resolved, keyed by {@link RemoteScopeEntry#getSourceId}. */
    private final Map<String, String> problems = new ConcurrentHashMap<>();

    public CqrsModelArchives(@NotNull Project project) {
        this.project = project;
    }

    /**
     * The models the given dependency provides.
     *
     * @param modelDir Directory of the model declaring it - a <code>local</code> path is relative to it.
     * @param entry Dependency to read.
     * @param allowResolve When {@code false} nothing is resolved: only what is already known is
     *            returned, which is what keeps the resolve path off the network.
     *
     * @return Model files, never {@code null}.
     */
    public @NotNull List<VirtualFile> modelFiles(@Nullable Path modelDir, @Nullable RemoteScopeEntry entry,
                                                 boolean allowResolve) {
        if (entry == null) {
            return List.of();
        }
        if (entry.getLocal() != null && !entry.getLocal().isEmpty()) {
            return localFiles(modelDir, entry);
        }
        final Path archive = archiveOf(entry, allowResolve);
        return archive == null ? List.of() : entriesOf(entry, archive);
    }

    /**
     * Why the given dependency cannot be resolved, or {@code null} when it resolves or when the answer
     * is not known yet (nothing has been resolved so far).
     */
    public @Nullable String problem(@Nullable Path modelDir, @Nullable RemoteScopeEntry entry) {
        if (entry == null) {
            return null;
        }
        if (entry.getLocal() != null && !entry.getLocal().isEmpty()) {
            return localFiles(modelDir, entry).isEmpty()
                    ? "the local directory '" + entry.getLocal() + "' does not exist or holds no '.cqrs' files"
                    : null;
        }
        return problems.get(entry.getSourceId());
    }

    /** Forgets what was resolved, so the next call resolves again. */
    public void invalidate() {
        resolved.clear();
        problems.clear();
    }

    private @Nullable Path archiveOf(RemoteScopeEntry entry, boolean allowResolve) {
        final String key = entry.getSourceId();
        if (key == null) {
            return null;
        }
        final Path known = resolved.get(key);
        if (known != null) {
            return known;
        }
        if (!allowResolve || problems.containsKey(key)) {
            return null;
        }
        try {
            final Path archive = resolveArtifact(entry);
            resolved.put(key, archive);
            problems.remove(key);
            return archive;
        } catch (Exception ex) {
            final String message = message(ex);
            LOG.warn("Could not resolve dependency '" + key + "': " + message, ex);
            problems.put(key, message);
            return null;
        }
    }

    /**
     * Resolves the artifact through the IDE's Maven. Overridable so a test can supply an archive of its
     * own without an embedder.
     *
     * @param entry Dependency to resolve.
     *
     * @return Path of the artifact in the local repository.
     *
     * @throws Exception If it cannot be resolved.
     */
    Path resolveArtifact(RemoteScopeEntry entry) throws Exception {
        return new MavenArtifactResolver(project)
                .resolve(entry.getGroupId(), entry.getArtifactId(), entry.getVersion());
    }

    /**
     * Every {@code .cqrs} below {@code model/} in the archive, as a virtual file.
     *
     * <p>Anything that goes wrong here is recorded as a problem of the dependency rather than answered
     * with a silent empty list: the artifact was resolved, so the only symptom left would be every type
     * it provides failing to resolve. {@link JarFileSystem} mounts what the IDE knows as an archive, and
     * a user who re-assigned {@code *.zip} to another file type in <i>Settings | Editor | File Types</i>
     * takes that knowledge away.</p>
     */
    private @NotNull List<VirtualFile> entriesOf(RemoteScopeEntry entry, Path archive) {
        final VirtualFile local = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(archive);
        if (local == null) {
            return recordProblem(entry, "the artifact is not visible to the IDE: " + archive);
        }
        final VirtualFile root = JarFileSystem.getInstance().getJarRootForLocalFile(local);
        if (root == null) {
            return recordProblem(entry, "the artifact could not be opened as an archive - is '"
                    + archive.getFileName() + "' still associated with 'Archive' in the file type settings?");
        }
        final VirtualFile models = root.findChild(MODEL_DIR);
        if (models == null) {
            return recordProblem(entry, "the artifact holds no '" + MODEL_DIR + "/' folder");
        }
        final List<VirtualFile> result = new ArrayList<>();
        collect(models, result);
        if (result.isEmpty()) {
            return recordProblem(entry, "the artifact holds no '" + CQRS_EXTENSION + "' files below '"
                    + MODEL_DIR + "/'");
        }
        return result;
    }

    /** Records why the models of a resolved artifact cannot be read, and answers with none. */
    private @NotNull List<VirtualFile> recordProblem(RemoteScopeEntry entry, String message) {
        final String key = entry.getSourceId();
        if (key != null) {
            problems.put(key, message);
        }
        return List.of();
    }

    private static void collect(VirtualFile dir, List<VirtualFile> target) {
        for (final VirtualFile child : dir.getChildren()) {
            if (child.isDirectory()) {
                collect(child, target);
            } else if (child.getName().endsWith(CQRS_EXTENSION)) {
                target.add(child);
            }
        }
    }

    /** The {@code .cqrs} files of a {@code local} directory, relative to the declaring model. */
    private @NotNull List<VirtualFile> localFiles(@Nullable Path modelDir, RemoteScopeEntry entry) {
        Path dir = Path.of(entry.getLocal());
        if (!dir.isAbsolute()) {
            if (modelDir == null) {
                return List.of();
            }
            dir = modelDir.resolve(dir);
        }
        final VirtualFile vf = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(dir);
        if (vf == null || !vf.isDirectory()) {
            return List.of();
        }
        final List<VirtualFile> result = new ArrayList<>();
        for (final VirtualFile child : vf.getChildren()) {
            if (!child.isDirectory() && child.getName().endsWith(CQRS_EXTENSION)) {
                result.add(child);
            }
        }
        return result;
    }

    /** The most telling message of a failure chain - the outermost one is often just a wrapper. */
    private static String message(Throwable ex) {
        String result = null;
        for (Throwable current = ex; current != null; current = current.getCause()) {
            if (current.getMessage() != null && !current.getMessage().isEmpty()) {
                result = current.getMessage();
            }
        }
        return result == null ? ex.getClass().getSimpleName() : result;
    }
}
