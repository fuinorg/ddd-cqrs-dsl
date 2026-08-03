package org.fuin.dsl.cqrs.tests;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Loads every <code>*.cqrs</code> file below the repository's <code>dsl-examples</code> directory and
 * asserts that none of them has anything to report - no parse error, no unresolved reference, and no
 * validation issue either.
 * 
 * <p>Parsing alone is not enough. An example is documentation meant to be copied, so one the language
 * itself rejects is worse than no example: it teaches something that does not work. That is not
 * hypothetical - the <code>SrcGen4J</code> hint kept the shape it had before the generator was
 * configured by scripts, and nothing noticed until somebody read the file.</p>
 * 
 * <p>The files are loaded the way the console verifier loads them - all of them into one resource set,
 * addressed by their real path - because they are not independent: one declares a
 * <code>dependency</code> on the models beside it, and a script a hint points at is resolved relative
 * to the file declaring it.</p>
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class DslExamplesParsingTest {
  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  @Inject
  private IResourceValidator validator;

  @Test
  public void allExamplesAreFreeOfIssues() {
    final File dir = DslExamplesParsingTest.findExamplesDir();
    final Iterable<File> files = DslExamplesParsingTest.collectExamples(dir);
    boolean _isEmpty = IterableExtensions.isEmpty(files);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("No .cqrs files found below ");
    _builder.append(dir);
    Assertions.assertFalse(_isEmpty, _builder.toString());
    final XtextResourceSet resourceSet = this.resourceSetProvider.get();
    final ArrayList<Resource> resources = CollectionLiterals.<Resource>newArrayList();
    for (final File file : files) {
      resources.add(resourceSet.getResource(URI.createFileURI(file.getAbsolutePath()), true));
    }
    EcoreUtil.resolveAll(resourceSet);
    final StringBuilder problems = new StringBuilder();
    for (final Resource resource : resources) {
      {
        String _fileString = resource.getURI().toFileString();
        final String name = new File(_fileString).getName();
        EList<Resource.Diagnostic> _errors = resource.getErrors();
        for (final Resource.Diagnostic error : _errors) {
          problems.append(name).append(": ").append(error.getMessage()).append("\n");
        }
        List<Issue> _validate = this.validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
        for (final Issue issue : _validate) {
          problems.append(name).append(":").append(issue.getLineNumber()).append(" ").append(issue.getSeverity()).append(
            " ").append(issue.getMessage()).append("\n");
        }
      }
    }
    int _length = problems.length();
    boolean _equals = (_length == 0);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("Issues in dsl-examples:");
    _builder_1.newLine();
    _builder_1.append(problems);
    Assertions.assertTrue(_equals, _builder_1.toString());
  }

  /**
   * Every {@code .cqrs} below the given directory, sub directories included, by path.
   */
  private static Iterable<File> collectExamples(final File dir) {
    final ArrayList<File> result = new ArrayList<File>();
    final File[] children = dir.listFiles();
    if ((children == null)) {
      return result;
    }
    final Function1<File, String> _function = (File it) -> {
      return it.getName();
    };
    List<File> _sortBy = IterableExtensions.<File, String>sortBy(((Iterable<File>)Conversions.doWrapArray(children)), _function);
    for (final File child : _sortBy) {
      boolean _isDirectory = child.isDirectory();
      if (_isDirectory) {
        Iterables.<File>addAll(result, DslExamplesParsingTest.collectExamples(child));
      } else {
        boolean _endsWith = child.getName().endsWith(".cqrs");
        if (_endsWith) {
          result.add(child);
        }
      }
    }
    return result;
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
