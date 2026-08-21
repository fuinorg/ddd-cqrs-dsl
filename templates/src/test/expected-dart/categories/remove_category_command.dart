import 'package:melkheftken_contract/src-gen/categories/category_id.dart';
import 'package:melkheftken_contract/src/descriptor/command_descriptor.dart';

/// Delete a custom category.
class RemoveCategoryCommand {
  /// Constructor with all data.
  const RemoveCategoryCommand({
    required this.entityIdPath,
    this.aggregateVersion,
  });

  /// Unique name used to store the command, and the last path segment of `POST /cmd/<type>`.
  static const String eventType = 'RemoveCategoryCommand';

  /// What this command is called and what it needs.
  static const CommandDescriptor descriptor = CommandDescriptor(
    type: eventType,
    module: 'categories',
    target: 'Category',
    kind: CommandKind.remove,
    doc: 'Delete a custom category.',
    message: 'Remove category',
  );

  /// Identifier of the aggregate this is directed at.
  final CategoryId entityIdPath;

  /// Version of the aggregate the change was decided on, so the write side can tell whether it
  /// is still current. Absent when the client does not know it.
  final int? aggregateVersion;

  /// Writes the command as the request body of `POST /cmd/RemoveCategoryCommand`.
  Map<String, Object?> toJson() => <String, Object?>{
        'entity-id-path': entityIdPath.typed,
        if (aggregateVersion != null) 'aggregate-version': aggregateVersion,
      };
}
