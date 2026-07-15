package org.fuin.dsl.cqrs.console;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.fuin.dsl.cqrs.CqrsDslStandaloneSetup;
import org.springframework.stereotype.Component;

import com.google.inject.Injector;

/**
 * Parses and validates CQRS DSL ({@code .cqrs}) files using the standalone Xtext runtime.
 *
 * <p>All {@code .cqrs} files found under the given paths are loaded into a single
 * {@link XtextResourceSet} so that references between them resolve, then each is validated with
 * {@link CheckMode#ALL} (which covers both syntax errors and semantic checks).</p>
 */
@Component
public class CqrsDslVerifier {

    private static final String CQRS_EXTENSION = ".cqrs";

    /** Name of the srcgen4j dependency cache directory that may be excluded from scanning. */
    static final String DEPENDENCIES_CACHE_DIR = ".dependencies-cache";

    /**
     * Verifies every {@code .cqrs} file found under the given files/directories.
     *
     * @param paths files or directories to scan; never {@code null}.
     * @param skipDependenciesCache when {@code true}, files inside any {@code .dependencies-cache}
     *            directory are not scanned or reported (they are still used for reference
     *            resolution via the DSL's remote-scope mechanism).
     * @return the collected report; empty when no {@code .cqrs} file was found.
     */
    public VerificationReport verify(final List<Path> paths, final boolean skipDependenciesCache) {
        final VerificationReport report = new VerificationReport();
        final List<Path> files = collectCqrsFiles(paths, skipDependenciesCache);
        if (files.isEmpty()) {
            return report;
        }

        final Injector injector = new CqrsDslStandaloneSetup().createInjectorAndDoEMFRegistration();
        final XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
        final IResourceValidator validator = injector.getInstance(IResourceValidator.class);

        // Load all resources first so that cross-file references can be resolved during validation.
        final List<Resource> resources = new ArrayList<>(files.size());
        for (final Path file : files) {
            final URI uri = URI.createFileURI(file.toAbsolutePath().normalize().toString());
            resources.add(resourceSet.getResource(uri, true));
        }

        for (int i = 0; i < files.size(); i++) {
            final List<Issue> issues =
                    validator.validate(resources.get(i), CheckMode.ALL, CancelIndicator.NullImpl);
            report.add(files.get(i), issues);
        }
        return report;
    }

    private List<Path> collectCqrsFiles(final List<Path> paths, final boolean skipDependenciesCache) {
        final List<Path> result = new ArrayList<>();
        for (final Path path : paths) {
            if (Files.isDirectory(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    walk.filter(Files::isRegularFile)
                        .filter(CqrsDslVerifier::isCqrsFile)
                        .filter(p -> !skipDependenciesCache || !isInDependenciesCache(p))
                        .forEach(result::add);
                } catch (final IOException ex) {
                    throw new UncheckedIOException("Failed to scan directory: " + path, ex);
                }
            } else if (Files.isRegularFile(path) && isCqrsFile(path)) {
                // An explicitly named file is always included, even inside a dependency cache.
                result.add(path);
            }
            // Non-existent paths and non-.cqrs files are silently ignored; an empty result
            // is reported by the caller as a usage error.
        }
        return result.stream().map(p -> p.toAbsolutePath().normalize()).distinct().toList();
    }

    private static boolean isCqrsFile(final Path path) {
        return path.getFileName().toString().endsWith(CQRS_EXTENSION);
    }

    private static boolean isInDependenciesCache(final Path path) {
        for (final Path segment : path) {
            if (DEPENDENCIES_CACHE_DIR.equals(segment.toString())) {
                return true;
            }
        }
        return false;
    }
}
