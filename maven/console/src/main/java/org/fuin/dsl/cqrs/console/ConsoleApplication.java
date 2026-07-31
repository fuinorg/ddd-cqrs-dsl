package org.fuin.dsl.cqrs.console;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolvers;
import org.fuin.dsl.cqrs.scoping.MimaArtifactResolver;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import eu.maveniverse.maven.mima.context.ContextOverrides;

/**
 * Spring Boot command line application that verifies one or more CQRS DSL ({@code .cqrs}) files.
 *
 * <p>Usage: {@code java -jar ddd-cqrs-dsl-console.jar [--settings <file>] [--offline] <file-or-directory> [more ...]}.
 * Every {@code .cqrs} file found under the given paths is parsed and validated; syntax and
 * validation issues are printed and the process exits with a non-zero code if any error is found.</p>
 *
 * <p>A {@code dependency} declared in a model is resolved with Maven. By default that uses the user's
 * {@code ~/.m2/settings.xml} - local repository, remote repositories, mirrors, servers and proxies all
 * come from there; {@code --settings} points at a different one and {@code --offline} forbids
 * downloading.</p>
 *
 * <p>Exit codes: {@code 0} = all files valid, {@code 1} = validation/syntax errors found,
 * {@code 2} = usage error (no/unknown arguments or no {@code .cqrs} files).</p>
 */
@SpringBootApplication(proxyBeanMethods = false)
public class ConsoleApplication implements CommandLineRunner, ExitCodeGenerator {

    private static final String SETTINGS_OPTION = "--settings";

    private static final String OFFLINE_FLAG = "--offline";

    private final CqrsDslVerifier verifier;

    private int exitCode = 0;

    public ConsoleApplication(final CqrsDslVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public void run(final String... args) {
        final List<Path> paths = new ArrayList<>();
        Path settings = null;
        boolean offline = false;

        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];
            if (SETTINGS_OPTION.equals(arg)) {
                if (i + 1 >= args.length) {
                    System.err.println("Missing file after " + SETTINGS_OPTION);
                    printUsage();
                    exitCode = 2;
                    return;
                }
                settings = Path.of(args[++i]);
            } else if (OFFLINE_FLAG.equals(arg)) {
                offline = true;
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

        CqrsArtifactResolvers.set(new MimaArtifactResolver(overrides(settings, offline)));

        final VerificationReport report = verifier.verify(paths);
        report.printTo(System.out);
        if (report.isEmpty()) {
            exitCode = 2;
        } else {
            exitCode = report.hasErrors() ? 1 : 0;
        }
    }

    /** Maven configuration for the run: the user settings unless a different file is given. */
    private static ContextOverrides overrides(final Path settings, final boolean offline) {
        final ContextOverrides.Builder builder = ContextOverrides.create().withUserSettings(true);
        if (settings != null) {
            builder.withUserSettingsXmlOverride(settings);
        }
        if (offline) {
            builder.offline(true);
        }
        return builder.build();
    }

    private static void printUsage() {
        System.err.println("Usage: java -jar ddd-cqrs-dsl-console.jar [--settings <file>] [--offline] <file-or-directory> [more ...]");
        System.err.println("Verifies CQRS DSL (.cqrs) files and reports syntax and validation issues.");
        System.err.println("Pass a directory to validate a whole model so cross-file references resolve.");
        System.err.println("  --settings <file>  Maven settings.xml to resolve a 'dependency' with (default: ~/.m2/settings.xml)");
        System.err.println("  --offline          never download; resolve only from the local repository");
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    public static void main(final String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ConsoleApplication.class, args)));
    }


}
