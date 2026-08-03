package org.fuin.dsl.cqrs.tests;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsData;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolver;
import org.fuin.dsl.cqrs.scoping.CqrsArtifactResolvers;
import org.fuin.dsl.cqrs.scoping.CqrsModelArchives;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Resolution has to work with what an IDE actually has: an <em>index</em> that is complete over the
 * project, and a <em>resource set</em> that holds the one file being edited and nothing else.
 * 
 * <p>Every other test here loads the whole model into one resource set, which is what a headless
 * SrcGen4J run does - and there the two pools are the same object, so code that reads the wrong one
 * still gets the right answer. An Xtext editor keeps them far apart: it opens one file, and finds
 * everything else through the index the builder wrote. Anything a model needs that is neither in that
 * one file nor in the index has to be pulled in deliberately.</p>
 * 
 * <p>Three things are, and each of them left every type of a <code>dependency</code> unresolvable in
 * Eclipse while the headless verifier reported no issue at all:</p>
 * 
 * <ul>
 * <li>the <code>dependency</code> itself, when it is declared on the context in <em>another</em> file
 * - nothing exports it, so the files declaring that context have to be read;</li>
 * <li>the models beside a model that was read out of the archive - they are in no index either;</li>
 * <li>a model read out of the archive seeing the elements of its own file - the index cannot answer
 * for a resource it does not contain.</li>
 * </ul>
 * 
 * <p>The index is modelled the way Xtext models it itself: a {@link ResourceDescriptionsData} snapshot
 * installed on the resource set. That is the same object an Eclipse builder installs, so it behaves
 * like the real one - the archive's models are not in it, because the builder never saw them.</p>
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(CqrsDslInjectorProvider.class)
@SuppressWarnings("all")
public class CqrsIdeIndexResolutionTest {
  private static final String COORDINATE = "org.fuin.test:cqrs-model:1.0.0";

  /**
   * Provides the type the other archive model needs, and is a second file of the same archive.
   */
  private static final String REMOTE_TYPES = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context remote {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module com.acme.types {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("type Text");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      return _builder.toString();
    }
  }.apply();

  /**
   * References the model beside it (<code>Text</code>) and an element of its own file
   * (<code>Money</code> from <code>Price</code>).
   */
  private static final String REMOTE_BILLING = new Function0<String>() {
    @Override
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context remote {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("module com.acme.billing {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("import remote.com.acme.types.*");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object Money {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Text amount");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("value-object Price {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Money net");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      return _builder.toString();
    }
  }.apply();

  @Inject
  private Provider<XtextResourceSet> resourceSetProvider;

  @Inject
  private IResourceDescription.Manager descriptionManager;

  @Inject
  private CqrsModelArchives archives;

  @BeforeEach
  public void forgetPreviousResolutions() {
    this.archives.invalidate();
  }

  @AfterEach
  public void restoreResolver() {
    CqrsArtifactResolvers.set(null);
  }

  /**
   * The coordinate is declared on the context in <code>declaration.cqrs</code> and the editor has
   * <code>usage.cqrs</code> open. Nothing but the index says the two belong together, so this is what
   * fails in Eclipse while a headless run - one resource set holding both - never notices.
   */
  @Test
  public void aDependencyDeclaredInAnotherFileIsFound() {
    final Path root = this.twoFileProject();
    final Resource editor = this.open(root, "usage.cqrs");
    this.assertResolves(editor);
    final Function1<Resource, Boolean> _function = (Resource it) -> {
      return Boolean.valueOf(CqrsModelArchives.isArchived(it.getURI()));
    };
    Assertions.assertTrue(IterableExtensions.<Resource>exists(editor.getResourceSet().getResources(), _function), 
      "the artifact must have been resolved, which only the other file\'s \'dependency\' asks for");
  }

  /**
   * A model of the artifact must reach the models beside it and the elements of its own file. It is
   * in no index - the builder never saw it - so neither can come from there.
   */
  @Test
  public void aModelReadOutOfTheArchiveResolvesItsNeighboursAndItself() {
    final Path root = this.twoFileProject();
    final Resource editor = this.open(root, "usage.cqrs");
    this.assertResolves(editor);
    final Function1<Resource, Boolean> _function = (Resource it) -> {
      return Boolean.valueOf(CqrsModelArchives.isArchived(it.getURI()));
    };
    final List<Resource> archived = IterableExtensions.<Resource>toList(IterableExtensions.<Resource>filter(editor.getResourceSet().getResources(), _function));
    Assertions.assertEquals(2, archived.size(), "both models of the archive must have been read");
    for (final Resource model : archived) {
      boolean _isEmpty = model.getErrors().isEmpty();
      StringConcatenation _builder = new StringConcatenation();
      String _lastSegment = model.getURI().lastSegment();
      _builder.append(_lastSegment);
      _builder.append(" must resolve: ");
      String _join = IterableExtensions.join(model.getErrors(), ", ");
      _builder.append(_join);
      Assertions.assertTrue(_isEmpty, _builder.toString());
    }
  }

  /**
   * The same for a <code>local</code> directory outside the project. Its models are plain files, but
   * files the builder never saw, so they are as absent from the index as an entry of a zip - and a
   * work in progress read this way is exactly where a model gets split over several files.
   */
  @Test
  public void aModelReadOutOfALocalDirectoryResolvesItsNeighboursAndItself() {
    try {
      final Path root = Files.createTempDirectory("ide-index-local");
      final Path provider = Files.createDirectories(root.resolve("provider"));
      Files.writeString(provider.resolve("types.cqrs"), CqrsIdeIndexResolutionTest.REMOTE_TYPES.toString());
      Files.writeString(provider.resolve("money.cqrs"), CqrsIdeIndexResolutionTest.REMOTE_BILLING.toString());
      final CqrsArtifactResolver _function = (String groupId, String artifactId, String version) -> {
        throw new IllegalStateException("must not resolve when \'local\' is declared");
      };
      CqrsArtifactResolvers.set(_function);
      final Path project = Files.createDirectories(root.resolve("project"));
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context consumer {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependency \"");
      _builder.append(CqrsIdeIndexResolutionTest.COORDINATE, "\t");
      _builder.append("\" local \"../provider\"");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      Files.writeString(project.resolve("declaration.cqrs"), _builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("context consumer {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("module com.acme.sales {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("import remote.com.acme.billing.*");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("value-object Order {");
      _builder_1.newLine();
      _builder_1.append("\t\t\t");
      _builder_1.append("Price total");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      Files.writeString(project.resolve("usage.cqrs"), _builder_1.toString());
      final Resource editor = this.open(project, "usage.cqrs");
      this.assertResolves(editor);
      final Function1<Resource, Boolean> _function_1 = (Resource it) -> {
        return Boolean.valueOf(it.getURI().toFileString().startsWith(provider.toString()));
      };
      final List<Resource> read = IterableExtensions.<Resource>toList(IterableExtensions.<Resource>filter(editor.getResourceSet().getResources(), _function_1));
      Assertions.assertEquals(2, read.size(), "both models of the local directory must have been read");
      for (final Resource model : read) {
        boolean _isEmpty = model.getErrors().isEmpty();
        StringConcatenation _builder_2 = new StringConcatenation();
        String _lastSegment = model.getURI().lastSegment();
        _builder_2.append(_lastSegment);
        _builder_2.append(" must resolve: ");
        String _join = IterableExtensions.join(model.getErrors(), ", ");
        _builder_2.append(_join);
        Assertions.assertTrue(_isEmpty, _builder_2.toString());
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A project whose context is split over two files: one declaring the <code>dependency</code>, one
   * importing and using what it provides.
   * 
   * @return Directory holding both.
   */
  private Path twoFileProject() {
    try {
      final Path root = Files.createTempDirectory("ide-index");
      final Path archive = root.resolve("cqrs-model-1.0.0.zip");
      String _string = CqrsIdeIndexResolutionTest.REMOTE_TYPES.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("model/public/types.cqrs", _string);
      String _string_1 = CqrsIdeIndexResolutionTest.REMOTE_BILLING.toString();
      Pair<String, String> _mappedTo_1 = Pair.<String, String>of("model/public/money.cqrs", _string_1);
      Files.write(archive, this.zip(
        Collections.<String, String>unmodifiableMap(CollectionLiterals.<String, String>newHashMap(_mappedTo, _mappedTo_1))));
      final CqrsArtifactResolver _function = (String groupId, String artifactId, String version) -> {
        return archive;
      };
      CqrsArtifactResolvers.set(_function);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("context consumer {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependency \"");
      _builder.append(CqrsIdeIndexResolutionTest.COORDINATE, "\t");
      _builder.append("\"");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      Files.writeString(root.resolve("declaration.cqrs"), _builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("context consumer {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("module com.acme.sales {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("import remote.com.acme.billing.*");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("value-object Order {");
      _builder_1.newLine();
      _builder_1.append("\t\t\t");
      _builder_1.append("Price total");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      Files.writeString(root.resolve("usage.cqrs"), _builder_1.toString());
      return root;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Opens one file the way an editor does: a resource set of its own, holding nothing but that file,
   * with an index over every file of the project installed on it.
   * 
   * @param root Project directory.
   * @param fileName File to open.
   * 
   * @return The opened resource, fully resolved.
   */
  private Resource open(final Path root, final String fileName) {
    try {
      final XtextResourceSet workspace = this.resourceSetProvider.get();
      final Predicate<Path> _function = (Path it) -> {
        return it.toString().endsWith(".cqrs");
      };
      final List<Path> files = Files.list(root).filter(_function).sorted().toList();
      for (final Path file : files) {
        workspace.getResource(URI.createFileURI(file.toString()), true);
      }
      EList<Resource> _resources = workspace.getResources();
      final Function1<Resource, IResourceDescription> _function_1 = (Resource it) -> {
        return this.descriptionManager.getResourceDescription(it);
      };
      List<IResourceDescription> _map = ListExtensions.<Resource, IResourceDescription>map(new ArrayList<Resource>(_resources), _function_1);
      final ResourceDescriptionsData index = new ResourceDescriptionsData(_map);
      final XtextResourceSet editor = this.resourceSetProvider.get();
      ResourceDescriptionsData.ResourceSetAdapter.installResourceDescriptionsData(editor, index);
      final Resource opened = editor.getResource(URI.createFileURI(root.resolve(fileName).toString()), true);
      EcoreUtil.resolveAll(editor);
      return opened;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * Nothing in the opened file is left unresolved.
   */
  private void assertResolves(final Resource opened) {
    boolean _isEmpty = opened.getErrors().isEmpty();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Unexpected errors in ");
    String _lastSegment = opened.getURI().lastSegment();
    _builder.append(_lastSegment);
    _builder.append(": ");
    String _join = IterableExtensions.join(opened.getErrors(), ", ");
    _builder.append(_join);
    Assertions.assertTrue(_isEmpty, _builder.toString());
  }

  private byte[] zip(final Map<String, String> entries) {
    try {
      final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      final ZipOutputStream zip = new ZipOutputStream(bytes);
      Set<Map.Entry<String, String>> _entrySet = entries.entrySet();
      for (final Map.Entry<String, String> entry : _entrySet) {
        {
          String _key = entry.getKey();
          ZipEntry _zipEntry = new ZipEntry(_key);
          zip.putNextEntry(_zipEntry);
          zip.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
          zip.closeEntry();
        }
      }
      zip.close();
      return bytes.toByteArray();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
