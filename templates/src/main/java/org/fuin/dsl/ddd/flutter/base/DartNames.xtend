package org.fuin.dsl.ddd.flutter.base

import org.fuin.dsl.cqrs.cqrsDsl.Module

/**
 * What a generated Dart artifact is called and where it goes.
 *
 * <p>Dart names files in <code>snake_case</code> and types in <code>PascalCase</code>, and the two are
 * not derivable from a Java package the way the JVM target's paths are. So this target computes its own
 * paths rather than routing through <code>model2JavaPackage</code> - a dotted package name is the wrong
 * shape for a Dart file, and pretending otherwise would put <code>de/fuin/melkheftken/</code> segments
 * into a language that has no packages.
 */
class DartNames {

    private new() {
    }

    /**
     * Turns a type name into the file it lives in: <code>CategoryType</code> becomes
     * <code>category_type</code>, and a run of capitals stays together, so <code>PdfReader</code> and
     * <code>PDFReader</code> both become <code>pdf_reader</code> rather than <code>p_d_f_reader</code>.
     *
     * @param name Type name in PascalCase.
     *
     * @return The same name in snake_case.
     */
    static def String snake(String name) {
        if (name === null || name.empty) {
            return name
        }
        val out = new StringBuilder()
        for (var i = 0; i < name.length; i++) {
            val c = name.charAt(i)
            val upper = Character.isUpperCase(c)
            val startsWord = upper && i > 0
                && (!Character.isUpperCase(name.charAt(i - 1))
                    || (i + 1 < name.length && !Character.isUpperCase(name.charAt(i + 1))))
            if (startsWord) {
                out.append('_')
            }
            out.append(Character.toLowerCase(c))
        }
        return out.toString
    }

    /**
     * The directory a module's artifacts go in, relative to the generated root: the module name with
     * its dots turned into separators, so <code>categories.categoryview</code> becomes
     * <code>categories/categoryview</code>.
     *
     * <p>The bounded context is deliberately not part of it. A Dart package is already a namespace, and
     * repeating <code>de/fuin/melkheftken/</code> inside one buys nothing but depth.
     *
     * @param module Module the artifact belongs to (may be <code>null</code>).
     *
     * @return Directory, or an empty string when the artifact belongs to no module.
     */
    static def String directory(Module module) {
        val name = module?.name
        return if(name === null) "" else name.replace('.', '/')
    }

    /**
     * The full path of one generated file.
     *
     * @param module Module the artifact belongs to (may be <code>null</code>).
     * @param typeName Name of the type in PascalCase.
     *
     * @return Path relative to the generated root, e.g. <code>categories/category_type.dart</code>.
     */
    static def String file(Module module, String typeName) {
        val dir = directory(module)
        val name = snake(typeName) + ".dart"
        return if(dir.empty) name else dir + "/" + name
    }

    /**
     * An import of another generated artifact, by the package the contract is published as.
     *
     * @param packageName Dart package the contract is published as.
     * @param module Module the imported artifact belongs to (may be <code>null</code>).
     * @param typeName Name of the imported type.
     *
     * @return A complete import line's URI.
     */
    static def String importOf(String packageName, Module module, String typeName) {
        return "package:" + packageName + "/src-gen/" + file(module, typeName)
    }

}
