package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

/**
 * Verifies that {@link RemoteScopeCache} and {@link RemoteScopeCatalog} reproduce the Eclipse
 * plugin's on-disk contract: a {@code <artifactId>-<version>-<sha1(gav)>} cache sub-directory per
 * Maven artifact (shared by all the namespaces it provides), an {@code index.json}, offline serving,
 * unpacking of {@code maven} {@code tar.gz} artifacts, and a {@code local} directory read directly.
 */
public class RemoteScopeCacheTest extends BasePlatformTestCase {

    private static final String GROUP_ID = "org.fuin.test";
    private static final String ARTIFACT_ID = "cqrs-model";
    private static final String VERSION = "0.1.0-SNAPSHOT";

    private Path workDir;
    private Path catalogDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        workDir = Files.createTempDirectory("cqrs-remote-test");
        catalogDir = Files.createDirectories(workDir.resolve("project"));
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

    /** Installs the test artifact into a fresh local repository under {@code workDir} and returns it. */
    private Path installArtifact() throws Exception {
        Path localRepo = Files.createDirectories(workDir.resolve("m2"));
        Path artifactDir = Files.createDirectories(
                localRepo.resolve("org/fuin/test/cqrs-model/0.1.0-SNAPSHOT"));
        Files.write(artifactDir.resolve("cqrs-model-0.1.0-SNAPSHOT-cqrs.tar.gz"),
                TarGzTestSupport.tarGz(Map.of(
                        "money.cqrs", "context com.acme { namespace billing { type Money } }",
                        "sku.cqrs", "context com.acme { namespace catalog { type Sku } }")));
        return localRepo;
    }

    private static RemoteScopeEntry mavenEntry() {
        return RemoteScopeEntry.maven(GROUP_ID, ARTIFACT_ID, VERSION);
    }

    public void testUnpacksMavenAndCachesByGav() throws Exception {
        Path localRepo = installArtifact();
        // Unreachable remote base: the local repository must be used instead.
        System.setProperty("cqrs.maven.repo.snapshots", "http://127.0.0.1:1/");
        System.setProperty("maven.repo.local", localRepo.toString());
        try {
            RemoteScopeEntry entry = mavenEntry();
            List<Path> cached = new RemoteScopeCache()
                    .getCachedModelFiles(catalogDir, "com.acme.billing", entry, true);

            assertEquals("both .cqrs files from the tar.gz must be unpacked", 2, cached.size());
            assertTrue(cached.stream().allMatch(Files::exists));
            assertTrue(cached.stream().anyMatch(p -> p.getFileName().toString().equals("money.cqrs")));
            assertTrue(cached.stream().anyMatch(p -> p.getFileName().toString().equals("sku.cqrs")));

            assertEquals("cache dir must be keyed by <artifactId>-<version>-<sha1(gav)>",
                    ARTIFACT_ID + "-" + VERSION + "-" + sha1(entry.getSourceId()),
                    cached.get(0).getParent().getFileName().toString());

            Path index = catalogDir.resolve(RemoteScopeCache.CACHE_DIR_NAME)
                    .resolve(RemoteScopeCache.INDEX_FILE_NAME);
            assertTrue("index.json must be written", Files.exists(index));
            String indexJson = Files.readString(index);
            assertTrue("index must record the GAV source", indexJson.contains(entry.getSourceId()));
        } finally {
            System.clearProperty("cqrs.maven.repo.snapshots");
            System.clearProperty("maven.repo.local");
        }
    }

    public void testTwoNamespacesShareOneCacheDir() throws Exception {
        Path localRepo = installArtifact();
        System.setProperty("cqrs.maven.repo.snapshots", "http://127.0.0.1:1/");
        System.setProperty("maven.repo.local", localRepo.toString());
        try {
            RemoteScopeCache cache = new RemoteScopeCache();
            RemoteScopeEntry entry = mavenEntry();
            cache.getCachedModelFiles(catalogDir, "com.acme.billing", entry, true);
            cache.getCachedModelFiles(catalogDir, "com.acme.catalog", entry, true);

            try (var dirs = Files.list(catalogDir.resolve(RemoteScopeCache.CACHE_DIR_NAME))) {
                long cacheDirs = dirs.filter(Files::isDirectory).count();
                assertEquals("the artifact must be cached once per GAV, shared by both namespaces",
                        1, cacheDirs);
            }
        } finally {
            System.clearProperty("cqrs.maven.repo.snapshots");
            System.clearProperty("maven.repo.local");
        }
    }

    public void testServesFromCacheOfflineAfterRestart() throws Exception {
        Path localRepo = installArtifact();
        System.setProperty("cqrs.maven.repo.snapshots", "http://127.0.0.1:1/");
        System.setProperty("maven.repo.local", localRepo.toString());
        try {
            new RemoteScopeCache().getCachedModelFiles(catalogDir, "com.acme.billing", mavenEntry(), true);
        } finally {
            System.clearProperty("cqrs.maven.repo.snapshots");
            System.clearProperty("maven.repo.local");
        }

        // A fresh instance simulates an IDE restart: with downloads disabled it must serve from disk.
        RemoteScopeCache restarted = new RemoteScopeCache();
        List<Path> cached = restarted.getCachedModelFiles(catalogDir, "com.acme.billing", mavenEntry(), false);
        assertEquals("cached models must be served offline", 2, cached.size());
        assertTrue(cached.stream().allMatch(Files::exists));
    }

    public void testWildcardImportResolvesViaCatalog() throws Exception {
        Files.writeString(catalogDir.resolve(RemoteScopeCatalog.DEFAULT_FILE_NAME),
                "[ { \"type\": \"maven\", \"namespaces\": [\"com.acme.billing\"], \"data\": {"
                        + " \"groupId\": \"" + GROUP_ID + "\", \"artifactId\": \"" + ARTIFACT_ID
                        + "\", \"version\": \"" + VERSION + "\" } } ]", StandardCharsets.UTF_8);
        Path nested = Files.createDirectories(catalogDir.resolve("sub").resolve("deep"));

        RemoteScopeCatalog catalog = new RemoteScopeCatalog();
        // A trailing .* wildcard resolves to the same entry as the bare namespace.
        assertEquals(ARTIFACT_ID, catalog.lookupEntry(nested, "com.acme.billing.*").getArtifactId());
        assertEquals(VERSION, catalog.lookupEntry(nested, "com.acme.billing").getVersion());
        assertEquals(catalogDir, catalog.rootDir(nested));
    }

    public void testLocalDirectoryServedDirectly() throws Exception {
        Path localModels = Files.createDirectories(workDir.resolve("local-models"));
        Files.writeString(localModels.resolve("billing.cqrs"),
                "context com.acme { namespace billing { type Money } }", StandardCharsets.UTF_8);

        RemoteScopeEntry entry = RemoteScopeEntry.maven(GROUP_ID, ARTIFACT_ID, VERSION, localModels.toString());
        // No maven repository configured: a local directory must never touch the network.
        List<Path> served = new RemoteScopeCache().getCachedModelFiles(catalogDir, "com.acme.billing", entry, true);

        assertEquals("the local model must be served directly", 1, served.size());
        assertEquals("billing.cqrs", served.get(0).getFileName().toString());
        assertTrue(Files.exists(served.get(0)));
        assertFalse("a local directory must not create a cache",
                Files.exists(catalogDir.resolve(RemoteScopeCache.CACHE_DIR_NAME)));
    }

    private static String sha1(String value) throws Exception {
        byte[] bytes = MessageDigest.getInstance("SHA-1").digest(value.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
