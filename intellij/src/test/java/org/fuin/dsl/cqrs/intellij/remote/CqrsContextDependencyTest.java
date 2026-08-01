package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.CqrsFile;
import org.fuin.dsl.cqrs.intellij.psi.CqrsNamedElement;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

/**
 * A <code>dependency</code> declared on a <code>context</code> applies to every file of that context,
 * not only to the file it is written in - a context may be split across files. This mirrors
 * {@code CqrsDependencies.declared} of the Xtext scoping, so the editor and the SrcGen4J build agree.
 *
 * <p>The models live on the real filesystem and are registered as a content root: resolution works off
 * the declaring file's directory and the cross-file lookup goes through the file-type index. What the
 * dependency provides stays <em>outside</em> that root, like the models inside a jar do, so nothing here
 * can pass by being an ordinary project file. A {@code local} clause keeps the whole test offline.</p>
 */
public class CqrsContextDependencyTest extends BasePlatformTestCase {

    private Path workDir;
    private Path projectDir;
    private VirtualFile contentRoot;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        workDir = Files.createTempDirectory("cqrs-context-dependency");
        projectDir = Files.createDirectories(workDir.resolve("project"));
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            // The light project is reused across tests, so the root must not stay behind.
            if (contentRoot != null) {
                PsiTestUtil.removeContentEntry(getModule(), contentRoot);
            }
            if (workDir != null) {
                try (var paths = Files.walk(workDir)) {
                    paths.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
                }
            }
        } finally {
            super.tearDown();
        }
    }

    /**
     * The dependency sits in 'aaa.cqrs', the import in 'sub/consumer.cqrs' - same context, other file.
     * The 'local' directory is relative to 'aaa.cqrs', not to the importing file.
     */
    public void testContextDependencyAppliesToTheOtherFilesOfTheContext() throws Exception {
        writeProvider();
        write("aaa.cqrs", """
                context cp {
                  dependency "g:a:1" local "../provider-models"
                }
                """);
        PsiFile consumer = write("sub/consumer.cqrs", """
                context cp {
                  module m {
                    import rp.r.*

                    value-object V {
                      Foo x
                    }
                  }
                }
                """);
        mount();

        assertTrue("a dependency of the context must reach its other files: " + names(consumer),
                names(consumer).contains("Foo"));
    }

    /** A dependency declared in the same file keeps working exactly as before. */
    public void testDependencyOfTheSameFileStillApplies() throws Exception {
        writeProvider();
        PsiFile consumer = write("consumer.cqrs", """
                context cp {
                  dependency "g:a:1" local "../provider-models"
                  module m {
                    import rp.r.*

                    value-object V {
                      Foo x
                    }
                  }
                }
                """);
        mount();

        assertTrue("the file's own dependency must apply: " + names(consumer),
                names(consumer).contains("Foo"));
    }

    /** A dependency of a differently named context stays invisible. */
    public void testDependencyOfAnotherContextIsNotApplied() throws Exception {
        writeProvider();
        write("aaa.cqrs", """
                context unrelated {
                  dependency "g:a:1" local "../provider-models"
                }
                """);
        PsiFile consumer = write("sub/consumer.cqrs", """
                context cp {
                  module m {
                    value-object V {
                      Integer x
                    }
                  }
                }
                """);
        mount();

        assertTrue("another context's dependency must not leak: " + names(consumer),
                names(consumer).isEmpty());
    }

    /** A dependency nested in a module belongs to that module, not to the context. */
    public void testModuleDependencyDoesNotReachTheOtherFiles() throws Exception {
        writeProvider();
        write("aaa.cqrs", """
                context cp {
                  module owner {
                    dependency "g:a:1" local "../provider-models"
                  }
                }
                """);
        PsiFile consumer = write("sub/consumer.cqrs", """
                context cp {
                  module m {
                    value-object V {
                      Integer x
                    }
                  }
                }
                """);
        mount();

        assertTrue("a module's dependency must stay in its file: " + names(consumer),
                names(consumer).isEmpty());
    }

    /**
     * Code completion runs against a non-physical copy of the file, which has no location on disk.
     * The dependencies still have to be found - through the original file - or an 'import' would
     * offer only what the project itself declares.
     */
    public void testImportCompletionOffersWhatADependencyProvides() throws Exception {
        writeProvider();
        write("aaa.cqrs", """
                context cp {
                  dependency "g:a:1" local "../provider-models"
                }
                """);
        PsiFile consumer = write("sub/consumer.cqrs", """
                context cp {
                  module m {
                    import\s
                  }
                }
                """);
        mount();

        List<String> lookups = completeAfter(consumer, "import ");
        assertTrue("the dependency's module wildcard must be offered: " + lookups,
                lookups.contains("rp.r.*"));
        assertTrue("the dependency's type must be offered: " + lookups,
                lookups.contains("rp.r.Foo"));
    }

    // ---- helpers -------------------------------------------------------------------------------

    /** The completion proposals at the position right behind {@code marker}. */
    private List<String> completeAfter(PsiFile file, String marker) {
        myFixture.configureFromExistingVirtualFile(file.getVirtualFile());
        int offset = file.getText().indexOf(marker) + marker.length();
        assertTrue("marker '" + marker + "' must exist", offset >= marker.length());
        myFixture.getEditor().getCaretModel().moveToOffset(offset);
        myFixture.complete(CompletionType.BASIC);
        List<String> strings = myFixture.getLookupElementStrings();
        return strings == null ? List.of() : strings;
    }

    /** Names of the declarations the file's dependencies make resolvable. */
    private List<String> names(PsiFile file) {
        return CqrsRemoteScopeResolver.getInstance(getProject()).remoteDeclarations(file).stream()
                .map(CqrsNamedElement::getName)
                .toList();
    }

    /**
     * The provided models live <em>outside</em> the project, like the models inside a dependency's jar
     * do. Inside it they would be indexed as ordinary project files and resolve without any
     * dependency at all, which would make these tests pass for the wrong reason.
     */
    private void writeProvider() throws Exception {
        Path provider = Files.createDirectories(workDir.resolve("provider-models"));
        Path model = provider.resolve("provided.cqrs");
        Files.writeString(model, "context rp { module r { type Foo } }", StandardCharsets.UTF_8);
        assertNotNull("virtual file for " + model,
                LocalFileSystem.getInstance().refreshAndFindFileByNioFile(model));
    }

    private PsiFile write(String relativePath, String content) throws Exception {
        Path file = projectDir.resolve(relativePath);
        Files.createDirectories(file.getParent());
        Files.writeString(file, content, StandardCharsets.UTF_8);
        VirtualFile vf = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(file);
        assertNotNull("virtual file for " + file, vf);
        PsiFile psi = PsiManager.getInstance(getProject()).findFile(vf);
        assertInstanceOf(psi, CqrsFile.class);
        return psi;
    }

    /** Makes the models part of the project, so the cross-file lookup finds them in the index. */
    private void mount() {
        contentRoot = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(projectDir);
        assertNotNull("virtual file for " + projectDir, contentRoot);
        contentRoot.refresh(false, true);
        PsiTestUtil.addContentRoot(getModule(), contentRoot);
    }
}
