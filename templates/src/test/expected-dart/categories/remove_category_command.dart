import 'package:melkheftken_contract/src-gen/categories/category_id.dart';
import 'package:melkheftken_contract/src/descriptor/command_descriptor.dart';
import 'package:melkheftken_contract/src/descriptor/model_text.dart';

/// Delete a custom category.
class RemoveCategoryCommand {
  /// Constructor with all data.
  const RemoveCategoryCommand({
    required this.aggregateId,
    this.aggregateVersion,
  });

  /// Unique name used to store the command, and the last path segment of `POST /cmd/<type>`.
  static const String eventType = 'RemoveCategoryCommand';

  /// What this command is called and what it needs.
  static const CommandDescriptor descriptor = CommandDescriptor(
    type: eventType,
    module: 'categories',
    target: 'Category',
    targetType: 'CATEGORY',
    targetOrigin: CommandTargetOrigin.row,
    kind: CommandKind.remove,
    doc: 'Delete a custom category.',
    message: 'Remove category',
    text: ModelText(
      bundle: 'Categories',
      key: 'RemoveCategoryCommand',
    ),
  );

  /// Identifier of the aggregate this is directed at.
  final CategoryId aggregateId;

  /// Path from the aggregate root down to the entity this is directed at, in the form the
  /// wire carries: typed segments separated by a slash.
  String get entityIdPath => aggregateId.typed;

  /// Version of the aggregate the change was decided on, so the write side can tell whether it
  /// is still current. Absent when the client does not know it.
  final int? aggregateVersion;

  /// Writes the command as the request body of `POST /cmd/RemoveCategoryCommand`.
  Map<String, Object?> toJson() => <String, Object?>{
        'entity-id-path': entityIdPath,
        if (aggregateVersion != null) 'aggregate-version': aggregateVersion,
      };
}
