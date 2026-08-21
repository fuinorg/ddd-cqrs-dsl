import 'package:melkheftken_contract/src-gen/categories/categoryview/category_view_descriptor.dart';
import 'package:melkheftken_contract/src-gen/categories/create_category_command.dart';
import 'package:melkheftken_contract/src-gen/categories/remove_category_command.dart';
import 'package:melkheftken_contract/src-gen/categories/rename_category_command.dart';
import 'package:melkheftken_contract/src/descriptor/command_descriptor.dart';
import 'package:melkheftken_contract/src/descriptor/model_text.dart';
import 'package:melkheftken_contract/src/descriptor/module_catalogue.dart';
import 'package:melkheftken_contract/src/descriptor/view_descriptor.dart';

/// Everything this release of the model offers - the value navigation is built from.
///
/// Adding a bounded context is a change here and no new screen anywhere: a module is offered the
/// moment its model compiles and the installation switches it on.
const ModuleCatalogue modules = ModuleCatalogue(
  context: 'de.fuin.melkheftken',
  modules: <ModuleDescriptor>[
    ModuleDescriptor(
      group: 'categories',
      modules: <String>['categories', 'categories.categoryview'],
      dependsOn: <String>[],
      text: ModelText(
        bundle: 'Categories',
        key: 'categories',
        shortLabel: 'Categories',
        label: 'Categories',
        tooltip: 'The income and expense buckets every record is classified into',
      ),
      views: <ViewDescriptor>[categoryView],
      commands: <CommandDescriptor>[
        CreateCategoryCommand.descriptor,
        RenameCategoryCommand.descriptor,
        RemoveCategoryCommand.descriptor,
      ],
    ),
  ],
);
