package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Verifies that {@link RemoteScopeCache} and {@link RemoteScopeCatalog} reproduce the Eclipse
 * plugin's on-disk contract: a {@code <namespace>-<sha1(source)>} cache sub-directory (with the
 * version added for {@code maven} sources) holding the model file(s), an {@code index.json}, offline
 * serving, {@code file:} staleness handling and unpacking of {@code maven} {@code tar.gz} artifacts.
 */
public class RemoteScopeCacheTest extends BasePlatformTestCase {

    private Path workDir;
    private Path catalogDir;
    private Path remoteSource;
    private String url;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        workDir = Files.createTempDirectory("cqrs-remote-test");
        catalogDir = Files.createDirectories(workDir.resolve("project"));
        remoteSource = workDir.resolve("billing.cqrs");
        Files.writeString(remoteSource,
                "context com.acme { namespace billing { type Money } }", StandardCharsets.UTF_8);
        url = remoteSource.toUri().toString();
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

    public void testDownloadsAndCachesInSha1Directory() throws Exception {
        RemoteScopeCache cache = new RemoteScopeCache();
        RemoteScopeEntry entry = RemoteScopeEntry.simple(url);
        List<Path> cached = cache.getCachedModelFiles(catalogDir, "com.acme.billing", entry, true);

        assertEquals("Expected the single model to be downloaded", 1, cached.size());
        Path file = cached.get(0);
        assertEquals(RemoteScopeCache.SIMPLE_FILE_NAME, file.getFileName().toString());
        assertEquals("com.acme.billing-" + sha1(url), file.getParent().getFileName().toString());
        assertTrue(Files.exists(file));

        Path index = catalogDir.resolve(RemoteScopeCache.CACHE_DIR_NAME)
                .resolve(RemoteScopeCache.INDEX_FILE_NAME);
        assertTrue("index.json must be written", Files.exists(index));
        String indexJson = Files.readString(index);
        assertTrue(indexJson.contains("com.acme.billing"));
        assertTrue(indexJson.contains(url));
        assertTrue(indexJson.contains("com.acme.billing-" + sha1(url)));
    }

    public void testServesFromCacheOfflineAfterRestart() throws Exception {
        RemoteScopeEntry entry = RemoteScopeEntry.simple(url);
        new RemoteScopeCache().getCachedModelFiles(catalogDir, "com.acme.billing", entry, true);

        // A fresh instance simulates an IDE restart: it must serve from disk without downloading.
        RemoteScopeCache restarted = new RemoteScopeCache();
        List<Path> cached = restarted.getCachedModelFiles(catalogDir, "com.acme.billing", entry, false);
        assertEquals("Cached model must be served offline", 1, cached.size());
        assertTrue(Files.exists(cached.get(0)));
    }

    public void testWildcardImportResolvesViaCatalog() throws Exception {
        Files.writeString(catalogDir.resolve(RemoteScopeCatalog.DEFAULT_FILE_NAME),
                "[ { \"type\": \"simple\", \"namespaces\": [\"com.acme.billing\"], \"data\": { \"url\": \""
                        + url + "\" } } ]", StandardCharsets.UTF_8);
        Path nested = Files.createDirectories(catalogDir.resolve("sub").resolve("deep"));

        RemoteScopeCatalog catalog = new RemoteScopeCatalog();
        assertEquals(url, catalog.lookupEntry(nested, "com.acme.billing.*").getUrl());
        assertEquals(url, catalog.lookupEntry(nested, "com.acme.billing").getUrl());
        assertEquals(catalogDir, catalog.rootDir(nested));
    }

    public void testReDownloadsWhenFileSourceIsNewer() throws Exception {
        RemoteScopeCache cache = new RemoteScopeCache();
        RemoteScopeEntry entry = RemoteScopeEntry.simple(url);
        assertEquals(1, cache.getCachedModelFiles(catalogDir, "com.acme.billing", entry, true).size());

        // Change the source and mark it newer than the cached copy.
        Files.writeString(remoteSource,
                "context com.acme { namespace billing { type Money type Tax } }", StandardCharsets.UTF_8);
        Files.setLastModifiedTime(remoteSource,
                java.nio.file.attribute.FileTime.from(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5),
                        TimeUnit.MILLISECONDS));

        List<Path> refreshed = cache.getCachedModelFiles(catalogDir, "com.acme.billing", entry, true);
        assertEquals(1, refreshed.size());
        assertTrue("Stale file: source should have been re-downloaded",
                Files.readString(refreshed.get(0)).contains("Tax"));
    }

    public void testUnpacksMavenTarGzFromLocalRepository() throws Exception {
        Path localRepo = Files.createDirectories(workDir.resolve("m2"));
        Path artifactDir = Files.createDirectories(
                localRepo.resolve("org/fuin/test/cqrs-model/0.1.0-SNAPSHOT"));
        Files.write(artifactDir.resolve("cqrs-model-0.1.0-SNAPSHOT-cqrs.tar.gz"),
                TarGzTestSupport.tarGz(Map.of(
                        "money.cqrs", "context com.acme { namespace billing { type Money } }",
                        "sku.cqrs", "context com.acme { namespace catalog { type Sku } }")));

        // Unreachable remote base: the local repository must be used instead.
        System.setProperty("cqrs.maven.repo.snapshots", "http://127.0.0.1:1/");
        System.setProperty("maven.repo.local", localRepo.toString());
        try {
            RemoteScopeEntry entry = RemoteScopeEntry.maven("org.fuin.test",
                    "cqrs-model", "0.1.0-SNAPSHOT");
            List<Path> cached = new RemoteScopeCache()
                    .getCachedModelFiles(catalogDir, "com.acme.billing", entry, true);

            assertEquals("both .cqrs files from the tar.gz must be unpacked", 2, cached.size());
            assertTrue(cached.stream().allMatch(Files::exists));
            assertTrue(cached.stream().anyMatch(p -> p.getFileName().toString().equals("money.cqrs")));
            assertTrue(cached.stream().anyMatch(p -> p.getFileName().toString().equals("sku.cqrs")));
            assertEquals("the maven version must be part of the cache directory name",
                    "com.acme.billing-0.1.0-SNAPSHOT-" + sha1(entry.getSourceId()),
                    cached.get(0).getParent().getFileName().toString());
        } finally {
            System.clearProperty("cqrs.maven.repo.snapshots");
            System.clearProperty("maven.repo.local");
        }
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
