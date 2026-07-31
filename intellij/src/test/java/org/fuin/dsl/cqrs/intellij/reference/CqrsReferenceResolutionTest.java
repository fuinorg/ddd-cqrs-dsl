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
 * declaration reached through a dependency declared more than once must resolve a reference to exactly
 * one declaration &mdash; while a genuinely ambiguous name (two distinct declarations in two
 * modules) must still resolve to several.
 *
 * <p>The models live on the real filesystem (not the in-memory fixture) because the dependency
 * resolve path works off the declaring file's directory. A {@code local} clause keeps the whole test
 * offline.</p>
 */
public class CqrsReferenceResolutionTest extends BasePlatformTestCase {

    /**
     * A consumer that references {@code Foo} and declares the test dependency the given number of
     * times. The models it provides are read straight from the {@code local} directory, so a
     * reference resolves against everything the dependency declares - no import is involved.
     */
    private static String consumer(int dependencyCount, String... imports) {
        StringBuilder sb = new StringBuilder("context cp {\n");
        for (int i = 0; i < dependencyCount; i++) {
            sb.append("  dependency \"g:a:1\" local \"local-models\"\n");
        }
        sb.append("  module cc.nn {\n");
        // A dependency only makes the models resolvable - an import decides what is visible.
        for (String imported : imports) {
            sb.append("    import ").append(imported).append("\n");
        }
        return sb.append("    event E {\n      Foo value\n    }\n  }\n}\n").toString();
    }

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

    /** One declaration reached through a dependency declared twice must resolve exactly once. */
    public void testSameTypeReachedTwiceResolvesOnce() throws Exception {
        writeModel("foo.cqrs", "context rp { module c.n { type Foo } }");

        CqrsFile consumer = writeAndLoadConsumer(consumer(2, "rp.c.n.*"));
        ResolveResult[] results = resolveFoo(consumer);

        assertEquals("the same declaration reached through a repeated dependency must be de-duplicated",
                1, results.length);
        assertNotNull("must resolve to a declaration", results[0].getElement());
    }

    /** Two genuinely distinct types named {@code Foo} must still resolve to several targets. */
    public void testGenuinelyDistinctTypesStayMultiple() throws Exception {
        writeModel("foo_n.cqrs", "context rn { module c.n { type Foo } }");
        writeModel("foo_m.cqrs", "context rm { module c.m { type Foo } }");

        CqrsFile consumer = writeAndLoadConsumer(consumer(1, "rn.c.n.*", "rm.c.m.*"));
        ResolveResult[] results = resolveFoo(consumer);

        assertEquals("two distinct declarations must both remain as separate targets",
                2, results.length);
    }

    /** A reference resolves against a declaration in the same module. */
    public void testReferenceResolvesWithinNamespacelessContext() {
        PsiFile file = myFixture.configureByText("m.cqrs", """
                context p {
                  module c {
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
        assertTrue("reference must resolve within the module, but got " + results.length,
                results.length >= 1);
        assertNotNull("must resolve to a declaration", results[0].getElement());
    }

    /**
     * Two files, one module: a reference resolves against a declaration that lives in the same
     * module but in another file, without an import. This is what "same module" means &mdash;
     * being in the same file is not a requirement.
     */
    public void testReferenceResolvesAcrossFilesWithinSameNamespace() {
        myFixture.configureByText("types.cqrs", """
                context p {
                    module c.n {
                      type String
                    }
                }
                """);
        PsiFile file = myFixture.configureByText("money.cqrs", """
                context p {
                    module c.n {
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
        ResolveResult[] results = ((PsiPolyVariantReference) ref.getReference()).multiResolve(false);
        assertTrue("must resolve to the declaration in the same module of the other file, but got "
                + results.length, results.length >= 1);
        assertNotNull("must resolve to a declaration", results[0].getElement());
    }

    // ---- helpers ---------------------------------------------------------

    /** Writes a model into the directory the consumer's {@code local} clause points at. */
    private void writeModel(String fileName, String content) throws Exception {
        Path model = localModels.resolve(fileName);
        Files.writeString(model, content, StandardCharsets.UTF_8);
        // Warm the model into the VFS so the cache-only resolve path can find it.
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(model);
    }

    private CqrsFile writeAndLoadConsumer(String content) throws Exception {
        Path consumer = projectDir.resolve("consumer.cqrs");
        Files.writeString(consumer, content, StandardCharsets.UTF_8);
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
