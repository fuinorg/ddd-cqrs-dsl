package org.fuin.dsl.cqrs.tests

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import org.eclipse.aether.repository.RemoteRepository
import org.fuin.dsl.cqrs.scoping.MimaArtifactResolver
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import eu.maveniverse.maven.mima.context.ContextOverrides

/**
 * Verifies that a coordinate is really resolved with Maven - the half {@link RemoteScopeResolutionTest}
 * stubs out.
 *
 * <p>The remote repository is a <code>file:</code> directory in real Maven layout rather than an
 * embedded HTTP server: Maven Resolver speaks <code>file:</code> natively, so this is a more faithful
 * fixture than a hand written HTTP fake and needs no port. The local repository is a temp directory
 * too, so the real <code>~/.m2</code> is never touched.</p>
 */
class MimaArtifactResolverTest {

	/** A published artifact is downloaded into the local repository and its path returned. */
	@Test
	def void resolvesFromARemoteRepository() {
		val root = Files.createTempDirectory("mima-resolver")
		publish(root, "1.0.0")

		val resolved = resolver(root).resolve("org.fuin.test", "cqrs-model", "1.0.0")

		Assertions.assertTrue(Files.isRegularFile(resolved), "must resolve to a file: " + resolved)
		Assertions.assertTrue(resolved.startsWith(root.resolve("local-repo")),
			"must land in the local repository: " + resolved)
		Assertions.assertEquals("cqrs-model-1.0.0.zip", resolved.fileName.toString)
	}

	/** A coordinate that is not in the repository fails with Maven's own diagnosis. */
	@Test
	def void reportsAnUnknownCoordinate() {
		val root = Files.createTempDirectory("mima-resolver-missing")
		publish(root, "1.0.0")

		val ex = Assertions.assertThrows(Exception, [
			resolver(root).resolve("org.fuin.test", "cqrs-model", "9.9.9")
		])
		Assertions.assertTrue(ex.message.contains("cqrs-model"),
			"the message must name the artifact: " + ex.message)
	}

	private def MimaArtifactResolver resolver(Path root) {
		val repository = new RemoteRepository.Builder("test", "default",
			root.resolve("remote-repo").toUri.toString).build
		return new MimaArtifactResolver(ContextOverrides.create.repositories(#[repository]).
			withLocalRepositoryOverride(root.resolve("local-repo")).build)
	}

	/** Lays out org/fuin/test/cqrs-model/<version>/ in real Maven layout. */
	private def void publish(Path root, String version) {
		val dir = Files.createDirectories(
			root.resolve("remote-repo/org/fuin/test/cqrs-model/" + version))

		val bytes = new ByteArrayOutputStream
		val zip = new ZipOutputStream(bytes)
		zip.putNextEntry(new ZipEntry("model/public/types.cqrs"))
		zip.write("context r { module m { type Money } }".getBytes(StandardCharsets.UTF_8))
		zip.closeEntry
		zip.close
		Files.write(dir.resolve("cqrs-model-" + version + ".zip"), bytes.toByteArray)

		Files.writeString(dir.resolve("cqrs-model-" + version + ".pom"), '''
			<project>
				<modelVersion>4.0.0</modelVersion>
				<groupId>org.fuin.test</groupId>
				<artifactId>cqrs-model</artifactId>
				<version>«version»</version>
			</project>
		''')
	}
}
