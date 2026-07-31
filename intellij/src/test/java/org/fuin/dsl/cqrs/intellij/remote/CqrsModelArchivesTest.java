package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The models of a dependency are read <em>inside</em> the artifact jar, and a {@code local} directory
 * is read directly. Nothing is ever unpacked.
 */
public class CqrsModelArchivesTest extends BasePlatformTestCase {

    private Path workDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        workDir = Files.createTempDirectory("cqrs-model-archives");
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            if (workDir != null) {
                try (var paths = Files.walk(workDir)) {
                    paths.sorted(java.util.Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
                }
            }
        } finally {
            super.tearDown();
        }
    }

    /** Entries below 'model/' are read from inside the jar - recursively, and nothing else. */
    public void testReadsModelsFromInsideTheJar() throws Exception {
        Path jar = writeJar(Map.of(
                "model/types.cqrs", "context r { module a { type Money } }",
                "model/sub/more.cqrs", "context r { module b { type Sku } }",
                "other/ignored.cqrs", "context r { module c { type Nope } }",
                "model/notamodel.txt", "ignored"));

        List<VirtualFile> models = archivesFor(jar).modelFiles(null,
                RemoteScopeEntry.parse("g:a:1", null), true);

        assertEquals("only the two '.cqrs' below 'model/': " + names(models), 2, models.size());
        assertTrue("must be read inside the jar: " + models.get(0).getUrl(),
                models.get(0).getUrl().startsWith("jar://"));
        assertTrue("nested entry must be found: " + names(models),
                names(models).contains("more.cqrs"));
        assertFalse("an entry outside 'model/' must be ignored: " + names(models),
                names(models).contains("ignored.cqrs"));
    }

    /** A 'local' directory bypasses resolution completely. */
    public void testReadsLocalDirectoryDirectly() throws Exception {
        Path provider = Files.createDirectories(workDir.resolve("provider"));
        Files.writeString(provider.resolve("provided.cqrs"), "context r { module a { type Money } }",
                StandardCharsets.UTF_8);
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(provider);

        CqrsModelArchives archives = new CqrsModelArchives(getProject());
        List<VirtualFile> models = archives.modelFiles(workDir,
                RemoteScopeEntry.parse("g:a:1", "provider"), false);

        assertEquals("the local model must be found: " + names(models), 1, models.size());
        assertNull("a resolvable local directory must not be reported",
                archives.problem(workDir, RemoteScopeEntry.parse("g:a:1", "provider")));
    }

    /** A 'local' directory that is not there is reported. */
    public void testMissingLocalDirectoryIsReported() {
        CqrsModelArchives archives = new CqrsModelArchives(getProject());
        String problem = archives.problem(workDir, RemoteScopeEntry.parse("g:a:1", "no-such-dir"));

        assertNotNull("a missing local directory must be reported", problem);
        assertTrue("message must name the directory: " + problem, problem.contains("no-such-dir"));
    }

    /** Nothing is resolved when resolution is not allowed - that keeps the resolve path off the network. */
    public void testDoesNotResolveWhenNotAllowed() {
        CqrsModelArchives archives = new CqrsModelArchives(getProject());

        assertTrue("must not resolve on the read path",
                archives.modelFiles(workDir, RemoteScopeEntry.parse("g:a:1", null), false).isEmpty());
        assertNull("and must not report anything either, since nothing was attempted",
                archives.problem(workDir, RemoteScopeEntry.parse("g:a:1", null)));
    }

    // ---- helpers ---------------------------------------------------------

    /** Archives that resolve every coordinate to the given jar, so no Maven is involved. */
    private CqrsModelArchives archivesFor(Path jar) {
        return new CqrsModelArchives(getProject()) {
            @Override
            Path resolveArtifact(RemoteScopeEntry entry) {
                return jar;
            }
        };
    }

    private Path writeJar(Map<String, String> entries) throws Exception {
        Path jar = workDir.resolve("provider-1.0.0.jar");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(bytes)) {
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                zip.putNextEntry(new ZipEntry(entry.getKey()));
                zip.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
                zip.closeEntry();
            }
        }
        Files.write(jar, bytes.toByteArray());
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(jar);
        return jar;
    }

    private static List<String> names(List<VirtualFile> files) {
        return files.stream().map(VirtualFile::getName).sorted().toList();
    }
}
