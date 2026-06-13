package org.fuin.dsl.cqrs.tests;

import com.google.inject.Inject;
import java.io.File;
import java.io.FileFilter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Parses every <code>*.cqrs</code> file in the repository's <code>dsl-examples</code> directory and
 * asserts the parser produces no errors. Nothing else is asserted.
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class DslExamplesParsingTest {
  @Inject
  private ParseHelper<DomainModel> parseHelper;

  @Test
  public void allExamplesParseWithoutErrors() {
    try {
      final File dir = DslExamplesParsingTest.findExamplesDir();
      final FileFilter _function = (File file) -> {
        return file.getName().endsWith(".cqrs");
      };
      final Function1<File, String> _function_1 = (File it) -> {
        return it.getName();
      };
      final List<File> files = IterableExtensions.<File, String>sortBy(((Iterable<File>)Conversions.doWrapArray(dir.listFiles(_function))), _function_1);
      boolean _isEmpty = files.isEmpty();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("No .cqrs files found in ");
      _builder.append(dir);
      Assertions.assertFalse(_isEmpty, _builder.toString());
      final StringBuilder problems = new StringBuilder();
      for (final File file : files) {
        {
          byte[] _readAllBytes = Files.readAllBytes(file.toPath());
          final String content = new String(_readAllBytes, StandardCharsets.UTF_8);
          final DomainModel result = this.parseHelper.parse(content);
          if ((result == null)) {
            problems.append(file.getName()).append(": could not be parsed").append("\n");
          } else {
            final EList<Resource.Diagnostic> errors = result.eResource().getErrors();
            boolean _isEmpty_1 = errors.isEmpty();
            boolean _not = (!_isEmpty_1);
            if (_not) {
              problems.append(file.getName()).append(": ").append(IterableExtensions.join(errors, ", ")).append("\n");
            }
          }
        }
      }
      int _length = problems.length();
      boolean _equals = (_length == 0);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("Parse errors in dsl-examples:");
      _builder_1.newLine();
      _builder_1.append(problems);
      Assertions.assertTrue(_equals, _builder_1.toString());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Locates the {@code dsl-examples} directory by walking up from the working directory.
   */
  private static File findExamplesDir() {
    File dir = new File("").getAbsoluteFile();
    while ((dir != null)) {
      {
        final File candidate = new File(dir, "dsl-examples");
        boolean _isDirectory = candidate.isDirectory();
        if (_isDirectory) {
          return candidate;
        }
        dir = dir.getParentFile();
      }
    }
    String _absolutePath = new File("").getAbsolutePath();
    String _plus = ("Could not locate \'dsl-examples\' directory above " + _absolutePath);
    throw new IllegalStateException(_plus);
  }
}
