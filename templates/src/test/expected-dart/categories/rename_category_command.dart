import 'package:melkheftken_contract/src-gen/categories/category_id.dart';
import 'package:melkheftken_contract/src-gen/categories/category_name.dart';
import 'package:melkheftken_contract/src/descriptor/attribute_descriptor.dart';
import 'package:melkheftken_contract/src/descriptor/command_descriptor.dart';
import 'package:melkheftken_contract/src/descriptor/model_text.dart';

/// Rename a custom category.
class RenameCategoryCommand {
  /// Constructor with all data.
  const RenameCategoryCommand({
    required this.aggregateId,
    required this.newName,
    this.aggregateVersion,
  });

  /// Unique name used to store the command, and the last path segment of `POST /cmd/<type>`.
  static const String eventType = 'RenameCategoryCommand';

  /// What this command is called and what it needs.
  static const CommandDescriptor descriptor = CommandDescriptor(
    type: eventType,
    module: 'categories',
    target: 'Category',
    targetType: 'CATEGORY',
    targetOrigin: CommandTargetOrigin.row,
    kind: CommandKind.modify,
    doc: 'Rename a custom category.',
    message: r"Rename category to '${newName}'",
    text: ModelText(
      bundle: 'Categories',
      key: 'RenameCategoryCommand',
      shortLabel: 'Rename',
      label: 'Rename this category',
      tooltip: 'Gives the category another name',
    ),
    rejections: <String, String>{
      'DuplicateCategoryNameException': 'newName',
    },
    attributes: <AttributeDescriptor>[
      AttributeDescriptor(
        name: 'newName',
        kind: ValueKind.text,
        modelType: 'CategoryName',
        text: ModelText(
          bundle: 'Categories',
          key: 'newName',
          shortLabel: 'Name',
          label: 'Name',
          tooltip: 'What this category is called in lists and pickers',
          prompt: 'Office and administration',
        ),
        constraints: CategoryName.constraints,
      ),
    ],
  );

  /// Identifier of the aggregate this is directed at.
  final CategoryId aggregateId;

  /// Path from the aggregate root down to the entity this is directed at, in the form the
  /// wire carries: typed segments separated by a slash.
  String get entityIdPath => aggregateId.typed;

  /// Version of the aggregate the change was decided on, so the write side can tell whether it
  /// is still current. Absent when the client does not know it.
  final int? aggregateVersion;

  final CategoryName newName;

  /// Writes the command as the request body of `POST /cmd/RenameCategoryCommand`.
  Map<String, Object?> toJson() => <String, Object?>{
        'entity-id-path': entityIdPath,
        if (aggregateVersion != null) 'aggregate-version': aggregateVersion,
        'newName': newName.value,
      };
}
