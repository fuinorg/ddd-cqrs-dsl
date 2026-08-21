/// Uniquely identifies a category.
///
/// On the wire an identifier is **typed** - `CATEGORY <uuid>` - and that is what both the
/// `entity-id-path` of a command and the `id` of a read-model row carry. The type is held here
/// so that no caller ever spells it out.
class CategoryId {
  /// Constructor with mandatory data.
  const CategoryId(this.value);

  /// Reads an identifier off the wire, in either the typed or the bare form.
  factory CategoryId.fromWire(String wire) {
    final space = wire.indexOf(' ');
    final id = space < 0 ? wire : wire.substring(space + 1);
    return CategoryId(id);
  }

  /// The entity type, as the model declares it.
  static const String type = 'CATEGORY';

  /// The identifier itself.
  final String value;

  /// The form that travels on the wire.
  String get typed => '$type $value';

  @override
  bool operator ==(Object other) =>
      identical(this, other) || other is CategoryId && other.value == value;

  @override
  int get hashCode => value.hashCode;

  @override
  String toString() => typed;
}
