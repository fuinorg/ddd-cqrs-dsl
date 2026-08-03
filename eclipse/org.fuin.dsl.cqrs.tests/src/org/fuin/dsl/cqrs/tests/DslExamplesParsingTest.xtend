package org.fuin.dsl.cqrs.tests

import com.google.inject.Inject
import com.google.inject.Provider
import java.io.File
import java.util.ArrayList
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.util.CancelIndicator
import org.eclipse.xtext.validation.CheckMode
import org.eclipse.xtext.validation.IResourceValidator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

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
@ExtendWith(InjectionExtension)
@InjectWith(CqrsDslInjectorProvider)
class DslExamplesParsingTest {

	@Inject Provider<XtextResourceSet> resourceSetProvider

	@Inject IResourceValidator validator

	@Test
	def void allExamplesAreFreeOfIssues() {
		val dir = findExamplesDir()
		val files = collectExamples(dir)
		Assertions.assertFalse(files.empty, '''No .cqrs files found below «dir»''')

		val resourceSet = resourceSetProvider.get
		val resources = <Resource>newArrayList
		for (file : files) {
			resources.add(resourceSet.getResource(URI.createFileURI(file.absolutePath), true))
		}
		EcoreUtil.resolveAll(resourceSet)

		val problems = new StringBuilder
		for (resource : resources) {
			val name = new File(resource.URI.toFileString).name
			for (error : resource.errors) {
				problems.append(name).append(": ").append(error.message).append("\n")
			}
			for (issue : validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl)) {
				problems.append(name).append(":").append(issue.lineNumber).append(" ").append(issue.severity).append(
					" ").append(issue.message).append("\n")
			}
		}
		Assertions.assertTrue(problems.length == 0, '''Issues in dsl-examples:
«problems»''')
	}

	/** Every {@code .cqrs} below the given directory, sub directories included, by path. */
	private def static Iterable<File> collectExamples(File dir) {
		val result = new ArrayList<File>
		val children = dir.listFiles
		if(children === null) return result
		for (child : children.sortBy[name]) {
			if (child.directory) {
				result.addAll(collectExamples(child))
			} else if (child.name.endsWith(".cqrs")) {
				result.add(child)
			}
		}
		return result
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
