package org.fuin.dsl.cqrs.console;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.validation.Issue;

/**
 * Collects the validation issues per verified file and renders a human-readable report.
 */
public class VerificationReport {

    /** Issues found in a single file. */
    public record FileResult(Path file, List<Issue> issues) {
    }

    private final List<FileResult> results = new ArrayList<>();

    /** Adds the issues found for one file. */
    public void add(final Path file, final List<Issue> issues) {
        results.add(new FileResult(file, List.copyOf(issues)));
    }

    /** @return {@code true} when no file was verified. */
    public boolean isEmpty() {
        return results.isEmpty();
    }

    /** @return {@code true} when at least one file has an error-severity issue. */
    public boolean hasErrors() {
        return countBySeverity(Severity.ERROR) > 0;
    }

    private long countBySeverity(final Severity severity) {
        return results.stream()
                .flatMap(r -> r.issues().stream())
                .filter(i -> i.getSeverity() == severity)
                .count();
    }

    /** Prints the full report (one section per file plus a summary line) to the given stream. */
    public void printTo(final PrintStream out) {
        if (results.isEmpty()) {
            out.println("No .cqrs files found to verify.");
            return;
        }

        for (final FileResult result : results) {
            out.println("Verifying: " + result.file());
            if (result.issues().isEmpty()) {
                out.println("  OK - no issues");
            } else {
                result.issues().stream()
                        .sorted(Comparator.comparingInt(i -> nullSafe(i.getLineNumber())))
                        .forEach(issue -> out.println("  " + format(issue)));
            }
        }

        final long errors = countBySeverity(Severity.ERROR);
        final long warnings = countBySeverity(Severity.WARNING);
        out.println();
        out.printf("Result: %d error(s), %d warning(s) across %d file(s) -> %s%n",
                errors, warnings, results.size(), errors > 0 ? "FAILED" : "PASSED");
    }

    private static String format(final Issue issue) {
        final Integer line = issue.getLineNumber();
        final Integer column = issue.getColumn();
        final String location = line == null ? "-" : line + (column == null ? "" : ":" + column);
        return String.format("%-7s line %-7s %s", issue.getSeverity(), location, issue.getMessage());
    }

    private static int nullSafe(final Integer value) {
        return value == null ? 0 : value;
    }
}
