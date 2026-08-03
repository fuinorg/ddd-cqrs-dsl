package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypeRef;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Opening a model out of a dependency's archive - the file the reader actually sees when navigating
 * into a published model.
 *
 * <p>Such a file belongs to no project: it is in no index and in no scope, not even for itself. Its
 * names therefore resolve against the archive it was read from, and nothing in it is reported to a
 * reader who cannot act on the message.</p>
 */
public class CqrsArchiveSelfResolutionTest extends BasePlatformTestCase {

    private Path workDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        workDir = Files.createTempDirectory("cqrs-archive-resolution");
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

    /** A type of one entry resolves from another entry of the same archive. */
    public void testReferenceResolvesToASiblingEntryOfTheSameArchive() throws Exception {
        Path archive = writeZip(Map.of(
                "model/public/types.cqrs", "context cm { module basics { type String } }",
                "model/public/values.cqrs", """
                        context cm {
                            module other {
                                import cm.basics.*
                                value-object Email base String {
                                    String value
                                }
                            }
                        }
                        """));

        PsiFile values = entry(archive, "model/public/values.cqrs");
        ResolveResult[] results = resolveFirst(values, "String");

        assertEquals("must resolve to exactly one declaration", 1, results.length);
        String url = results[0].getElement().getContainingFile().getVirtualFile().getUrl();
        assertTrue("must resolve inside the same archive, but was: " + url, url.startsWith("jar://"));
        assertTrue("must resolve to the sibling entry, but was: " + url,
                url.endsWith("model/public/types.cqrs"));
    }

    /** A declaration of the very file being read resolves - it is in no index either. */
    public void testReferenceResolvesWithinTheSameArchivedFile() throws Exception {
        Path archive = writeZip(Map.of("model/public/all.cqrs", """
                context cm {
                    module basics {
                        type String
                        value-object Email base String {
                            String value
                        }
                    }
                }
                """));

        PsiFile all = entry(archive, "model/public/all.cqrs");
        assertEquals("a type of the same file must resolve", 1, resolveFirst(all, "String").length);
    }

    /**
     * Nothing in a model that is only read is reported - not even a reference that really cannot be
     * resolved, which is what a model publishing only part of itself leaves behind.
     */
    public void testNothingIsReportedInAnArchivedModel() throws Exception {
        Path archive = writeZip(Map.of("model/public/cat.cqrs", """
                context cm {
                    module categories {
                        import cm.nowhere.*
                        aggregate-id CategoryId identifies Category {
                            slabel "CID"
                            label "Category ID"
                            tooltip "Identifies a category"
                            examples "1"
                        }
                    }
                }
                """));

        PsiFile cat = entry(archive, "model/public/cat.cqrs");
        myFixture.configureFromExistingVirtualFile(cat.getVirtualFile());
        List<HighlightInfo> errors = myFixture.doHighlighting(HighlightSeverity.ERROR);

        assertEmpty("a model that is read, not authored, must report nothing: "
                + errors.stream().map(HighlightInfo::getDescription).toList(), errors);
    }

    /** The archive's declarations stay out of a file of this project, which has its own scope. */
    public void testArchiveDeclarationsDoNotLeakIntoAProjectFile() throws Exception {
        writeZip(Map.of("model/public/types.cqrs", "context cm { module basics { type Leaked } }"));

        PsiFile own = myFixture.configureByText("own.cqrs", """
                context p {
                    module m {
                        value-object V base Leaked {
                            Leaked value
                        }
                    }
                }
                """);
        assertEquals("an archive nobody depends on must not be visible",
                0, resolveFirst(own, "Leaked").length);
    }

    // ---- helpers ---------------------------------------------------------

    /** The PSI of an entry inside the archive - the file the reader opens from the repository. */
    private PsiFile entry(Path archive, String relativePath) {
        VirtualFile local = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(archive);
        assertNotNull("the archive must be visible to the IDE", local);
        VirtualFile root = JarFileSystem.getInstance().getJarRootForLocalFile(local);
        assertNotNull("the zip must mount as an archive", root);
        VirtualFile vf = root.findFileByRelativePath(relativePath);
        assertNotNull("entry " + relativePath, vf);
        PsiFile psiFile = PsiManager.getInstance(getProject()).findFile(vf);
        assertInstanceOf(psiFile, CqrsFile.class);
        return psiFile;
    }

    /** Resolves the first reference of the file that is written as the given name. */
    private static ResolveResult[] resolveFirst(PsiFile file, String name) {
        for (CqrsTypeRef ref : PsiTreeUtil.findChildrenOfType(file, CqrsTypeRef.class)) {
            if (name.equals(ref.getReferencedName())) {
                return ((PsiPolyVariantReference) ref.getReference()).multiResolve(false);
            }
        }
        throw new AssertionError("no reference to '" + name + "' in " + file.getName());
    }

    private Path writeZip(Map<String, String> entries) throws Exception {
        Path archive = workDir.resolve("provider-1.0.0.zip");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(bytes)) {
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                zip.putNextEntry(new ZipEntry(entry.getKey()));
                zip.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
                zip.closeEntry();
            }
        }
        Files.write(archive, bytes.toByteArray());
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(archive);
        return archive;
    }
}
