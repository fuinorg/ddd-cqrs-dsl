package org.fuin.dsl.cqrs.console;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot command line application that verifies one or more CQRS DSL ({@code .cqrs}) files.
 *
 * <p>Usage: {@code java -jar ddd-cqrs-dsl-console.jar [--skip-dependencies-cache] <file-or-directory> [more ...]}.
 * Every {@code .cqrs} file found under the given paths is parsed and validated; syntax and
 * validation issues are printed and the process exits with a non-zero code if any error is found.</p>
 *
 * <p>Exit codes: {@code 0} = all files valid, {@code 1} = validation/syntax errors found,
 * {@code 2} = usage error (no/unknown arguments or no {@code .cqrs} files).</p>
 */
@SpringBootApplication(proxyBeanMethods = false)
public class ConsoleApplication implements CommandLineRunner, ExitCodeGenerator {

    private static final String SKIP_DEPENDENCIES_CACHE_FLAG = "--skip-dependencies-cache";

    private final CqrsDslVerifier verifier;

    private int exitCode = 0;

    public ConsoleApplication(final CqrsDslVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public void run(final String... args) {
        boolean skipDependenciesCache = false;
        final List<Path> paths = new ArrayList<>();
        for (final String arg : args) {
            if (SKIP_DEPENDENCIES_CACHE_FLAG.equals(arg)) {
                skipDependenciesCache = true;
            } else if (arg.startsWith("-")) {
                System.err.println("Unknown option: " + arg);
                printUsage();
                exitCode = 2;
                return;
            } else {
                paths.add(Path.of(arg));
            }
        }

        if (paths.isEmpty()) {
            printUsage();
            exitCode = 2;
            return;
        }

        final VerificationReport report = verifier.verify(paths, skipDependenciesCache);
        report.printTo(System.out);
        if (report.isEmpty()) {
            exitCode = 2;
        } else {
            exitCode = report.hasErrors() ? 1 : 0;
        }
    }

    private static void printUsage() {
        System.err.println("Usage: java -jar ddd-cqrs-dsl-console.jar [--skip-dependencies-cache] <file-or-directory> [more ...]");
        System.err.println("Verifies CQRS DSL (.cqrs) files and reports syntax and validation issues.");
        System.err.println("Pass a directory to validate a whole model so cross-file references resolve.");
        System.err.println("  --skip-dependencies-cache  ignore files under any '.dependencies-cache' directory");
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    public static void main(final String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ConsoleApplication.class, args)));
    }


}
