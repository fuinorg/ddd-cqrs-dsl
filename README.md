# ddd-cqrs-dsl
DSL and tooling for Domain-driven Design (DDD) and Command Query Responsibility Segregation (CQRS).

[![Java Maven Build](https://github.com/fuinorg/ddd-cqrs-dsl/actions/workflows/maven.yml/badge.svg)](https://github.com/fuinorg/ddd-cqrs-dsl/actions/workflows/maven.yml)
[![Eclipse Plugin Build](https://github.com/fuinorg/ddd-cqrs-dsl/actions/workflows/eclipse.yml/badge.svg)](https://github.com/fuinorg/ddd-cqrs-dsl/actions/workflows/eclipse.yml)
[![IntelliJ Plugin Build](https://github.com/fuinorg/ddd-cqrs-dsl/actions/workflows/intellij.yml/badge.svg)](https://github.com/fuinorg/ddd-cqrs-dsl/actions/workflows/intellij.yml)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 25](https://img.shields.io/badge/JDK-25-green.svg)](https://openjdk.java.net/projects/jdk/25/)

Defines a DSL for defining DDD/CQRS things like aggregates, entities or value objects. 
To make editing of these files easier, there is an Eclipse plugin that supports syntax highlighting and code completion.
There are also pre-defined templates for generating Java code based on the DSL files.

The components are:
- [Eclipse Plugin](#eclipse-plugin) - Syntax highlighting and code completion for Eclipse.
- [IntelliJ IDEA Plugin](#intellij-idea-plugin) - Syntax highlighting and code completion for IntelliJ IDEA.
- [Maven Library](#maven-jar-file) - Allows "standalone" parsing of "*.cqrs" files.
- [Templates](#ddd-templates-jar-file) - Xtend-based DDD/CQRS Java code generation templates for [SrcGen4J](https://github.com/fuinorg/srcgen4j).
- [DSL Examples](dsl-examples) - Examples of the DDD/CQRS DSL you can open and edit after installing the above Eclipse or IntelliJ IDEA plugin.

## Status
This is currently heavily work in progress. So don't expect the DSL, the plugins or the code generation templates to stay stable in any way!

<img src="doc/work-in-progress.svg" alt="Work in progress" width="500"/>

## Artifacts
The plugins for IntelliJ IDEA and Eclipse provide several nice features:
- Syntax highlighting
- Reference-aware code completion
- Go-to-definition
- Find-usages
- Rename
- Resolution of remote references via a `dependencies.json` catalog

> [!NOTE]  
> Check out these [dsl-examples](https://github.com/fuinorg/ddd-cqrs-dsl/tree/main/dsl-examples) for DSL files.

### Eclipse Plugin
The Eclipse plugin allows editing "*.cqrs" text files with the DSL and supports syntax highlighting and code completion. 

<a href="doc/eclipse.png"><img src="doc/eclipse.png" width="293" height="279"></a>

[Installation instructions...](eclipse/INSTALLATION.md)

### IntelliJ IDEA Plugin
The Intellij IDA plugin brings the same `*.cqrs` editing experience to IntelliJ IDEA:

<a href="doc/intellij.png"><img src="doc/intellij.png" width="293" height="269"></a>

[Installation instructions...](intellij/INSTALLATION.md)

### Maven JAR file
You can use the Maven artifact to parse files in the DSL format without using Eclipse.
An example of this is the [templates](templates) module in this repository.

The artifact is currently available as a snapshot in https://central.sonatype.com/repository/maven-snapshots/org/fuin/dsl/ddd-cqrs-dsl/
and will, once released, be available in Maven Central at https://central.sonatype.com/artifact/org.fuin.dsl/ddd-cqrs-dsl.

### DDD Templates JAR file
Xtend based domain-driven design (DDD) code generation templates for use with [SrcGen4J](https://github.com/fuinorg/srcgen4j/).
The [templates](templates) module provides several artifact factories that generate Java code (based on the [ddd-4-java](https://github.com/fuinorg/ddd-4-java) utility classes)
from an Xtext based DDD/CQRS DSL model. See the [module README](templates/README.md) for the full list of available factories.

## Projects
The [maven](maven) and [templates](templates) projects form the Maven reactor built from the root `pom.xml`, while
the [eclipse](eclipse) project is built separately.

Aligning the Eclipse and Maven build systems is challenging, so the DSL is intentionally split into two
separate but mirrored projects ([eclipse](eclipse) and [maven](maven)) rather than a single combined one.
Their source folders must always contain the same (duplicated) content: 
- [eclipse/org.fuin.dsl.cqrs/src](eclipse/org.fuin.dsl.cqrs/src) = [maven/org.fuin.dsl.cqrs/src/main/java](maven/org.fuin.dsl.cqrs/src/main/java)

After running the build, the content of the generated folders should also be the same:
- [eclipse/org.fuin.dsl.cqrs/src-gen](eclipse/org.fuin.dsl.cqrs/src-gen) = [maven/org.fuin.dsl.cqrs/src/main/xtext-gen](maven/org.fuin.dsl.cqrs/src/main/xtext-gen)
- [eclipse/org.fuin.dsl.cqrs/xtend-gen](eclipse/org.fuin.dsl.cqrs/xtend-gen) = [maven/org.fuin.dsl.cqrs/src/main/xtend-gen](maven/org.fuin.dsl.cqrs/src/main/xtend-gen)

## Changing the DSL
Changes in the DSL are made **only** in the [eclipse](eclipse) project with the [Eclipse IDE for Java and DSL Developers](https://www.eclipse.org/downloads/packages/release/2026-03/r/eclipse-ide-java-and-dsl-developers).

> [!WARNING]  
> Do not try other IDEs (like IntelliJ IDEA) to open the [eclipse](eclipse) project as it will not work because of missing Xtend, Xtext and Eclipse Plugin support.

You need to keep the main code of the two projects aligned by copying it from [eclipse](eclipse) to [maven](maven) when you make changes (see below).

> [!WARNING]  
> Do not try to open the [maven](maven) directory as a project in Eclipse. It will not work out of the box.

### Steps
1. Open the [eclipse](eclipse) project with  [Eclipse IDE for Java and DSL Developers](https://www.eclipse.org/downloads/packages/release/2026-03/r/eclipse-ide-java-and-dsl-developers) (Version 2026-03 with Xtext 2.42.0)
2. Make changes to the [DSL](eclipse/org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/CqrsDsl.xtext)
3. Generate the code using the Mwe2 Launch called `Generate CqrsDsl (cqrs) Language Infrastructure`
4. Click on `org.fuin.dsl.cqrs` and right click "Run as... Eclipse Application"
5. Test your DSL with any example
6. Mirror the changes from `eclipse` to `maven` by running [`./mirror-eclipse-sources-to-maven.sh`](mirror-eclipse-sources-to-maven.sh)
   in the root directory (use `-n` for a dry run first). It mirrors the hand-written sources
   ([src](eclipse/org.fuin.dsl.cqrs/src/org)) **and** the generated trees
   ([src-gen](eclipse/org.fuin.dsl.cqrs/src-gen), [xtend-gen](eclipse/org.fuin.dsl.cqrs/xtend-gen)),
   automatically keeping the Maven-specific files (`GenerateCqrsDsl.mwe2`, `AbstractCqrsDslValidator.java`,
   and the Eclipse-only `*.xtendbin` / `*._trace` artifacts) untouched.
   <br>(To do it by hand instead: compare [eclipse/org.fuin.dsl.cqrs/src/org](eclipse/org.fuin.dsl.cqrs/src/org)
   with [maven/org.fuin.dsl.cqrs/src/main/java/org](maven/org.fuin.dsl.cqrs/src/main/java/org) and copy all
   changes from `eclipse` to `maven` — but do **NOT** copy `GenerateCqrsDsl.mwe2`!)
7. On the console run `./mvnw clean verify` in the [maven](maven) directory
8. Commit and push changes to Git

> [!WARNING]  
> The [IntelliJ IDEA plugin](intellij) is **not** updated automatically in any way.
> Changes in the Eclipse DSL must be added manually to IntelliJ!
> The same applies to changes in Eclipse [CqrsDslValidator](eclipse/org.fuin.dsl.cqrs/src/org/fuin/dsl/cqrs/validation/CqrsDslValidator.xtend). 
