package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.fuin.dsl.cqrs.cqrsDsl.DomainModel
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

/**
 * Parses every <code>*.cqrs</code> file in the repository's <code>dsl-examples</code> directory and
 * asserts the parser produces no errors. Nothing else is asserted.
 */
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class DslExamplesParsingTest {

	@Inject
	ParseHelper<DomainModel> parseHelper

	@Test
	def void allExamplesParseWithoutErrors() {
		val dir = findExamplesDir()
		val files = dir.listFiles[file|file.name.endsWith(".cqrs")].sortBy[name]
		Assertions.assertFalse(files.empty, '''No .cqrs files found in «dir»''')

		val problems = new StringBuilder
		for (file : files) {
			val content = new String(Files.readAllBytes(file.toPath), StandardCharsets.UTF_8)
			val result = parseHelper.parse(content)
			if (result === null) {
				problems.append(file.name).append(": could not be parsed").append("\n")
			} else {
				val errors = result.eResource.errors
				if (!errors.empty) {
					problems.append(file.name).append(": ").append(errors.join(", ")).append("\n")
				}
			}
		}
		Assertions.assertTrue(problems.length == 0, '''Parse errors in dsl-examples:
«problems»''')
	}

	/** Locates the {@code dsl-examples} directory by walking up from the working directory. */
	def private static File findExamplesDir() {
		var dir = new File("").absoluteFile
		while (dir !== null) {
			val candidate = new File(dir, "dsl-examples")
			if (candidate.isDirectory) {
				return candidate
			}
			dir = dir.parentFile
		}
		throw new IllegalStateException("Could not locate 'dsl-examples' directory above " + new File("").absolutePath)
	}
}
