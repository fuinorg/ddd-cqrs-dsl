package org.fuin.dsl.cqrs.intellij;

import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.ParsingTestCase;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;

/**
 * Parses every {@code *.cqrs} file in the repository's {@code dsl-examples} directory and asserts
 * the parser produces no errors. Nothing else is asserted.
 */
public class DslExamplesParsingTest extends ParsingTestCase {

    public DslExamplesParsingTest() {
        super("", "cqrs", new CqrsParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return ".";
    }

    public void testAllExamplesParseWithoutErrors() throws Exception {
        File dir = findExamplesDir();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".cqrs"));
        assertNotNull("No .cqrs files found in " + dir, files);
        Arrays.sort(files);
        assertTrue("No .cqrs files found in " + dir, files.length > 0);

        StringBuilder problems = new StringBuilder();
        for (File file : files) {
            String text = Files.readString(file.toPath());
            PsiFile psi = createPsiFile(file.getName(), text);
            ensureParsed(psi);
            Collection<PsiErrorElement> errors = PsiTreeUtil.collectElementsOfType(psi, PsiErrorElement.class);
            for (PsiErrorElement error : errors) {
                problems.append(file.getName()).append(": ").append(error.getErrorDescription()).append('\n');
            }
        }
        assertTrue("Parse errors in dsl-examples:\n" + problems, problems.length() == 0);
    }

    /** Locates the {@code dsl-examples} directory by walking up from the working directory. */
    private static File findExamplesDir() {
        for (File dir = new File("").getAbsoluteFile(); dir != null; dir = dir.getParentFile()) {
            File candidate = new File(dir, "dsl-examples");
            if (candidate.isDirectory()) {
                return candidate;
            }
        }
        throw new IllegalStateException(
                "Could not locate 'dsl-examples' directory above " + new File("").getAbsolutePath());
    }
}
