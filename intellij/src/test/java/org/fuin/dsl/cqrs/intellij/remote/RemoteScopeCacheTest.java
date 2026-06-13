package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

/**
 * Verifies that {@link RemoteScopeCache} and {@link RemoteScopeCatalog} reproduce the Eclipse
 * plugin's on-disk contract: {@code <namespace>-<sha1(url)>.cqrs} cache file names, an
 * {@code index.json}, offline serving and {@code file:} staleness handling.
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

    public void testDownloadsAndCachesWithSha1Name() throws Exception {
        RemoteScopeCache cache = new RemoteScopeCache();
        Path cached = cache.getCachedModelFile(catalogDir, "com.acme.billing", url, true);

        assertNotNull("Expected the model to be downloaded", cached);
        assertEquals("com.acme.billing-" + sha1(url) + ".cqrs", cached.getFileName().toString());
        assertTrue(Files.exists(cached));

        Path index = catalogDir.resolve(RemoteScopeCache.CACHE_DIR_NAME)
                .resolve(RemoteScopeCache.INDEX_FILE_NAME);
        assertTrue("index.json must be written", Files.exists(index));
        String indexJson = Files.readString(index);
        assertTrue(indexJson.contains("com.acme.billing"));
        assertTrue(indexJson.contains(cached.getFileName().toString()));
    }

    public void testServesFromCacheOfflineAfterRestart() throws Exception {
        new RemoteScopeCache().getCachedModelFile(catalogDir, "com.acme.billing", url, true);

        // A fresh instance simulates an IDE restart: it must serve from disk without downloading.
        RemoteScopeCache restarted = new RemoteScopeCache();
        Path cached = restarted.getCachedModelFile(catalogDir, "com.acme.billing", url, false);
        assertNotNull("Cached model must be served offline", cached);
        assertTrue(Files.exists(cached));
    }

    public void testWildcardImportResolvesViaCatalog() throws Exception {
        Files.writeString(catalogDir.resolve(RemoteScopeCatalog.DEFAULT_FILE_NAME),
                "[ { \"com.acme.billing\": \"" + url + "\" } ]", StandardCharsets.UTF_8);
        Path nested = Files.createDirectories(catalogDir.resolve("sub").resolve("deep"));

        RemoteScopeCatalog catalog = new RemoteScopeCatalog();
        assertEquals(url, catalog.lookupUrl(nested, "com.acme.billing.*"));
        assertEquals(url, catalog.lookupUrl(nested, "com.acme.billing"));
        assertEquals(catalogDir, catalog.rootDir(nested));
    }

    public void testReDownloadsWhenFileSourceIsNewer() throws Exception {
        RemoteScopeCache cache = new RemoteScopeCache();
        Path cached = cache.getCachedModelFile(catalogDir, "com.acme.billing", url, true);
        assertNotNull(cached);

        // Change the source and mark it newer than the cached copy.
        Files.writeString(remoteSource,
                "context com.acme { namespace billing { type Money type Tax } }", StandardCharsets.UTF_8);
        Files.setLastModifiedTime(remoteSource,
                java.nio.file.attribute.FileTime.from(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5),
                        TimeUnit.MILLISECONDS));

        Path refreshed = cache.getCachedModelFile(catalogDir, "com.acme.billing", url, true);
        assertNotNull(refreshed);
        assertTrue("Stale file: source should have been re-downloaded",
                Files.readString(refreshed).contains("Tax"));
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
