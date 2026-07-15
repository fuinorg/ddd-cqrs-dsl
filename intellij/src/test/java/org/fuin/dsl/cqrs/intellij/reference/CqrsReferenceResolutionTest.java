package org.fuin.dsl.cqrs.intellij.reference;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypeRef;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reproduces the "Multiple Implementations" popup that listed the same type several times. A single
 * remote source reached through more than one imported namespace (a Maven artifact provides several
 * namespaces, so {@link org.fuin.dsl.cqrs.intellij.remote.CqrsRemoteScopeResolver} serves its models
 * once per namespace) must resolve a reference to exactly one declaration &mdash; while a genuinely
 * ambiguous name (two distinct declarations in two namespaces) must still resolve to several.
 *
 * <p>The models live on the real filesystem (not the in-memory fixture) because the remote resolve
 * path walks the file's directory to discover the {@code dependencies.json} catalog. A {@code local}
 * catalog entry keeps the whole test offline.</p>
 */
public class CqrsReferenceResolutionTest extends BasePlatformTestCase {

    private static final String CONSUMER = """
            project cp {
              context cc {
                namespace nn {
                  import c.n.*
                  import c.m.*
                  event E {
                    Foo value
                  }
                }
              }
            }
            """;

    private Path workDir;
    private Path projectDir;
    private Path localModels;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        workDir = Files.createTempDirectory("cqrs-resolve-test");
        projectDir = Files.createDirectories(workDir.resolve("project"));
        localModels = Files.createDirectories(projectDir.resolve("local-models"));
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

    /** One remote declaration reached through two imported namespaces must resolve exactly once. */
    public void testSameTypeReachedTwiceResolvesOnce() throws Exception {
        writeCatalog("c.n", "c.m");
        // The single model declares Foo only in namespace n, yet both imports route to this same dir.
        writeModel("foo.cqrs", "project rp { context c { namespace n { type Foo } } }");

        CqrsFile consumer = writeAndLoadConsumer();
        ResolveResult[] results = resolveFoo(consumer);

        assertEquals("the same declaration reached through two imports must be de-duplicated",
                1, results.length);
        assertNotNull("must resolve to a declaration", results[0].getElement());
    }

    /** Two genuinely distinct types named {@code Foo} must still resolve to several targets. */
    public void testGenuinelyDistinctTypesStayMultiple() throws Exception {
        writeCatalog("c.n", "c.m");
        writeModel("foo_n.cqrs", "project rn { context c { namespace n { type Foo } } }");
        writeModel("foo_m.cqrs", "project rm { context c { namespace m { type Foo } } }");

        CqrsFile consumer = writeAndLoadConsumer();
        ResolveResult[] results = resolveFoo(consumer);

        assertEquals("two distinct declarations must both remain as separate targets",
                2, results.length);
    }

    /** A reference resolves against a declaration in the same namespace-less context. */
    public void testReferenceResolvesWithinNamespacelessContext() {
        PsiFile file = myFixture.configureByText("m.cqrs", """
                project p {
                  context c {
                    type String
                    value-object Money base String {
                      String amount
                    }
                  }
                }
                """);

        CqrsTypeRef ref = null;
        for (CqrsTypeRef candidate : PsiTreeUtil.findChildrenOfType(file, CqrsTypeRef.class)) {
            if ("String".equals(candidate.getReferencedName())) {
                ref = candidate;
                break;
            }
        }
        assertNotNull("a type reference to 'String' must be present", ref);
        PsiReference reference = ref.getReference();
        assertInstanceOf(reference, PsiPolyVariantReference.class);
        ResolveResult[] results = ((PsiPolyVariantReference) reference).multiResolve(false);
        assertTrue("reference must resolve within the namespace-less context, but got " + results.length,
                results.length >= 1);
        assertNotNull("must resolve to a declaration", results[0].getElement());
    }

    // ---- helpers ---------------------------------------------------------

    /** A {@code dependencies.json} whose single {@code local} entry provides all the given namespaces. */
    private void writeCatalog(String... namespaces) throws Exception {
        StringBuilder ns = new StringBuilder();
        for (String n : namespaces) {
            if (ns.length() > 0) {
                ns.append(", ");
            }
            ns.append('"').append(n).append('"');
        }
        Files.writeString(projectDir.resolve("dependencies.json"),
                "[ { \"type\": \"maven\", \"namespaces\": [" + ns + "], \"data\": {"
                        + " \"groupId\": \"g\", \"artifactId\": \"a\", \"version\": \"1\","
                        + " \"local\": \"local-models\" } } ]", StandardCharsets.UTF_8);
    }

    private void writeModel(String fileName, String content) throws Exception {
        Path model = localModels.resolve(fileName);
        Files.writeString(model, content, StandardCharsets.UTF_8);
        // Warm the model into the VFS so the cache-only resolve path can find it.
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(model);
    }

    private CqrsFile writeAndLoadConsumer() throws Exception {
        Path consumer = projectDir.resolve("consumer.cqrs");
        Files.writeString(consumer, CONSUMER, StandardCharsets.UTF_8);
        VirtualFile vf = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(consumer);
        assertNotNull("virtual file for " + consumer, vf);
        PsiFile psi = PsiManager.getInstance(getProject()).findFile(vf);
        assertInstanceOf(psi, CqrsFile.class);
        return (CqrsFile) psi;
    }

    private ResolveResult[] resolveFoo(CqrsFile consumer) {
        CqrsTypeRef ref = null;
        for (CqrsTypeRef candidate : PsiTreeUtil.findChildrenOfType(consumer, CqrsTypeRef.class)) {
            if ("Foo".equals(candidate.getReferencedName())) {
                ref = candidate;
                break;
            }
        }
        assertNotNull("a type reference to 'Foo' must be present", ref);
        PsiReference reference = ref.getReference();
        assertInstanceOf(reference, PsiPolyVariantReference.class);
        return ((PsiPolyVariantReference) reference).multiResolve(false);
    }
}
