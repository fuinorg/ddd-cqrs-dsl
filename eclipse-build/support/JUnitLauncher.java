/*
 * Minimal JUnit 5 launcher for the headless Eclipse/PDE build.
 *
 * Uses only the JUnit Platform Launcher API that ships inside the Eclipse SDK
 * (junit-platform-launcher + the jupiter engine), so no external test runner has
 * to be downloaded -- keeping the build pure-Eclipse. It scans a directory of
 * compiled test classes, runs every JUnit 5 test found, prints a summary, writes a
 * JUnit-style XML report, and exits non-zero if anything failed.
 *
 *   java -cp "<test-classes>:<deps>" JUnitLauncher <test-classes-dir> <report.xml>
 */
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class JUnitLauncher {

    public static void main(String[] args) throws Exception {
        Path classesDir = Paths.get(args[0]);
        Path reportXml = Paths.get(args[1]);

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClasspathRoots(Set.of(classesDir)))
                .build();

        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        PrintWriter out = new PrintWriter(System.out, true);
        summary.printTo(out);
        summary.printFailuresTo(out);

        long found = summary.getTestsFoundCount();
        long failed = summary.getTestsFailedCount();
        long skipped = summary.getTestsSkippedCount();
        double seconds = (summary.getTimeFinished() - summary.getTimeStarted()) / 1000.0;

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append(String.format(Locale.ROOT,
                "<testsuite name=\"eclipse-pde\" tests=\"%d\" failures=\"%d\" skipped=\"%d\" time=\"%.3f\">%n",
                found, failed, skipped, seconds));
        for (TestExecutionSummary.Failure f : summary.getFailures()) {
            xml.append(String.format(Locale.ROOT, "  <testcase name=\"%s\">%n",
                    escape(f.getTestIdentifier().getDisplayName())));
            xml.append("    <failure>").append(escape(String.valueOf(f.getException())))
                    .append("</failure>\n");
            xml.append("  </testcase>\n");
        }
        xml.append("</testsuite>\n");
        Files.createDirectories(reportXml.toAbsolutePath().getParent());
        Files.writeString(reportXml, xml.toString());

        if (found == 0) {
            out.println("WARNING: no JUnit tests were discovered in " + classesDir);
        }
        System.exit(failed > 0 ? 1 : 0);
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
