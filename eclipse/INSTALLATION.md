# Installation
Here are the steps to install the plugin in an Eclipse IDE:

> [!IMPORTANT]  
> Requires [Eclipse 2026-03](https://www.eclipse.org/downloads/packages/)
> (It was only tested with that version)

1. Install the "Eclipse IDE for Java and DSL Developers": https://www.eclipse.org/downloads/packages/
2. Install the Plugin:
    * Select "Help / Install New Software..." in the top level menu
    * Click on the "Manage" button on the right upper side
    * Press the "Add" button to add a new update site
    * Enter Name "ddd-cqrs-dsl" and Location "https://fuinorg.jfrog.io/artifactory/ddd-cqrs-dsl/cqrs-dsl/latest/"
    * Click "Apply & Close"
    * Select the newly added site in the "Work with" dropdown.
    * Select the "CqrsDsl" shown in the list panel below
    * Click "Next" and "Finish"
    * Select "Trust selected" for "https://fuinorg.jfrog.io" update site
    * Select "Trust selected" for "Trust unsigned content of unknown origin" - This is necessary because the plugin is not signed.
    * Restart the IDE to finalize the installation
3. Open a project with "*.cqrs" files and start editing.
