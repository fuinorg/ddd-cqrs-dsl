package org.fuin.dsl.cqrs.tests;

import eu.maveniverse.maven.mima.context.ContextOverrides;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.fuin.dsl.cqrs.scoping.MimaArtifactResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * Verifies that a coordinate is really resolved with Maven - the half {@link RemoteScopeResolutionTest}
 * stubs out.
 * 
 * <p>The remote repository is a <code>file:</code> directory in real Maven layout rather than an
 * embedded HTTP server: Maven Resolver speaks <code>file:</code> natively, so this is a more faithful
 * fixture than a hand written HTTP fake and needs no port. The local repository is a temp directory
 * too, so the real <code>~/.m2</code> is never touched.</p>
 */
@SuppressWarnings("all")
public class MimaArtifactResolverTest {
  /**
   * A published artifact is downloaded into the local repository and its path returned.
   */
  @Test
  public void resolvesFromARemoteRepository() {
    try {
      final Path root = Files.createTempDirectory("mima-resolver");
      this.publish(root, "1.0.0");
      final Path resolved = this.resolver(root).resolve("org.fuin.test", "cqrs-model", "1.0.0");
      Assertions.assertTrue(Files.isRegularFile(resolved), ("must resolve to a file: " + resolved));
      Assertions.assertTrue(resolved.startsWith(root.resolve("local-repo")), 
        ("must land in the local repository: " + resolved));
      Assertions.assertEquals("cqrs-model-1.0.0.jar", resolved.getFileName().toString());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  /**
   * A coordinate that is not in the repository fails with Maven's own diagnosis.
   */
  @Test
  public void reportsAnUnknownCoordinate() {
    try {
      final Path root = Files.createTempDirectory("mima-resolver-missing");
      this.publish(root, "1.0.0");
      final Executable _function = () -> {
        this.resolver(root).resolve("org.fuin.test", "cqrs-model", "9.9.9");
      };
      final Exception ex = Assertions.<Exception>assertThrows(Exception.class, _function);
      boolean _contains = ex.getMessage().contains("cqrs-model");
      String _message = ex.getMessage();
      String _plus = ("the message must name the artifact: " + _message);
      Assertions.assertTrue(_contains, _plus);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private MimaArtifactResolver resolver(final Path root) {
    String _string = root.resolve("remote-repo").toUri().toString();
    final RemoteRepository repository = new RemoteRepository.Builder("test", "default", _string).build();
    ContextOverrides _build = ContextOverrides.create().repositories(Collections.<RemoteRepository>unmodifiableList(CollectionLiterals.<RemoteRepository>newArrayList(repository))).withLocalRepositoryOverride(root.resolve("local-repo")).build();
    return new MimaArtifactResolver(_build);
  }

  /**
   * Lays out org/fuin/test/cqrs-model/<version>/ in real Maven layout.
   */
  private void publish(final Path root, final String version) {
    try {
      final Path dir = Files.createDirectories(
        root.resolve(("remote-repo/org/fuin/test/cqrs-model/" + version)));
      final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      final ZipOutputStream zip = new ZipOutputStream(bytes);
      ZipEntry _zipEntry = new ZipEntry("model/types.cqrs");
      zip.putNextEntry(_zipEntry);
      zip.write("context r { module m { type Money } }".getBytes(StandardCharsets.UTF_8));
      zip.closeEntry();
      zip.close();
      Files.write(dir.resolve((("cqrs-model-" + version) + ".jar")), bytes.toByteArray());
      Path _resolve = dir.resolve((("cqrs-model-" + version) + ".pom"));
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<project>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<modelVersion>4.0.0</modelVersion>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<groupId>org.fuin.test</groupId>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<artifactId>cqrs-model</artifactId>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<version>");
      _builder.append(version, "\t");
      _builder.append("</version>");
      _builder.newLineIfNotEmpty();
      _builder.append("</project>");
      _builder.newLine();
      Files.writeString(_resolve, _builder);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
