# Installation
To install it in IntelliJ IDEA, add the custom plugin repository under
*Settings | Plugins | ⚙ | Manage Plugin Repositories…*:

> [!IMPORTANT]  
> Requires IntelliJ IDEA 2026.1.x
> (With 2025.x the plugin will not be found)

```
https://fuinorg.jfrog.io/artifactory/ddd-cqrs-dsl/cqrs-dsl-intellij/latest/updatePlugins.xml
```

then install **CQRS DSL** from the Marketplace tab. 

Alternatively build it yourself with `./gradlew buildPlugin` inside the `intellij` folder
and install the zip from disk. See the [README](README.md) for details.
