package org.fuin.dsl.cqrs.intellij.remote;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.fuin.dsl.cqrs.intellij.CqrsFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A dependency that cannot be resolved is reported on its coordinate. The models live on the real
 * filesystem because resolution works off the declaring file's directory, and a {@code local} clause
 * keeps the whole test offline.
 */
public class CqrsDependencyProblemTest extends BasePlatformTestCase {

    private Path workDir;
    private Path projectDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        workDir = Files.createTempDirectory("cqrs-dependency-problem");
        projectDir = Files.createDirectories(workDir.resolve("project"));
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

    /** A 'local' directory that is not there cannot provide anything - the answer is final at once. */
    public void testMissingLocalDirectoryIsReported() throws Exception {
        PsiFile consumer = writeConsumer("""
                context cp {
                  dependency "g:a:1" local "no-such-dir"
                  module m {
                    type Own
                  }
                }
                """);
        RemoteScopeEntry entry = RemoteScopeEntry.parse("g:a:1", "no-such-dir");
        assertNotNull("coordinate must parse", entry);

        String problem = CqrsRemoteScopeResolver.getInstance(getProject()).problem(consumer, entry);
        assertNotNull("a missing local directory must be reported", problem);
        assertTrue("message must name the directory: " + problem, problem.contains("no-such-dir"));
    }

    /** A 'local' directory holding models resolves, so nothing is reported. */
    public void testResolvableLocalDirectoryIsNotReported() throws Exception {
        Path provider = Files.createDirectories(projectDir.resolve("local-models"));
        Files.writeString(provider.resolve("provided.cqrs"),
                "context rp { module r { type Foo } }", StandardCharsets.UTF_8);
        LocalFileSystem.getInstance().refreshAndFindFileByNioFile(provider.resolve("provided.cqrs"));

        PsiFile consumer = writeConsumer("""
                context cp {
                  dependency "g:a:1" local "local-models"
                  module m {
                    import rp.r.*

                    value-object V {
                      Foo x
                    }
                  }
                }
                """);
        RemoteScopeEntry entry = RemoteScopeEntry.parse("g:a:1", "local-models");
        assertNotNull("coordinate must parse", entry);

        assertNull("a resolvable dependency must not be reported",
                CqrsRemoteScopeResolver.getInstance(getProject()).problem(consumer, entry));
    }

    /**
     * A broken coordinate is reported even when the declaring file holds no reference at all.
     * Resolution is otherwise only started while a reference is being resolved, so a context that
     * carries nothing but a 'dependency' used to stay silent forever.
     */
    public void testUnresolvableDependencyIsReportedWithoutAnyReference() throws Exception {
        PsiFile consumer = writeConsumer("""
                context cp {
                  dependency "g:a:1"
                }
                """);
        RemoteScopeEntry entry = RemoteScopeEntry.parse("g:a:1", null);
        assertNotNull("coordinate must parse", entry);

        CqrsModelArchives failing = new CqrsModelArchives(getProject()) {
            @Override
            Path resolveArtifact(RemoteScopeEntry e) throws Exception {
                throw new IllegalStateException("the artifact is not in the local repository");
            }
        };
        CqrsRemoteScopeResolver resolver = new CqrsRemoteScopeResolver(getProject(), failing);

        // The first call only starts the background resolution and answers "not known yet".
        assertNull("nothing can be known before the warming ran", resolver.problem(consumer, entry));
        PlatformTestUtil.waitWithEventsDispatching(
                "the failed resolution must reach the annotator",
                () -> resolver.problem(consumer, entry) != null, 10);

        String problem = resolver.problem(consumer, entry);
        assertTrue("message must explain the failure: " + problem,
                problem.contains("not in the local repository"));
    }

    private PsiFile writeConsumer(String content) throws Exception {
        Path consumer = projectDir.resolve("consumer.cqrs");
        Files.writeString(consumer, content, StandardCharsets.UTF_8);
        VirtualFile vf = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(consumer);
        assertNotNull("virtual file for " + consumer, vf);
        PsiFile psi = PsiManager.getInstance(getProject()).findFile(vf);
        assertInstanceOf(psi, CqrsFile.class);
        return psi;
    }
}
