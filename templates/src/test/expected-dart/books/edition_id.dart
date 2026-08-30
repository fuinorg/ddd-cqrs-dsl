import 'package:melkheftken_contract/src-gen/books/book_id.dart';
import 'package:melkheftken_contract/src/json/json.dart';

/// A run of one book, identified by the book and the day it was printed.
///
/// A natural key rather than a surrogate, so no client can mint one - the identifier follows from
/// what the command already carries.
///
/// On the wire an identifier is **typed** - `EDITION <string>` - and that is what both the
/// `entity-id-path` of a command and the `id` of a read-model row carry. The type is held here
/// so that no caller ever spells it out.
class EditionId {
  /// Constructor with mandatory data.
  const EditionId(this.value);

  /// Builds the identifier from its parts, the way the write side composes it.
  ///
  /// A natural key rather than a surrogate, so there is nothing to mint: the identifier follows
  /// from the values themselves.
  factory EditionId.of(BookId bookId, DateTime printedOn) =>
      EditionId('${bookId.value}-${wireDate(printedOn)!}');

  /// Reads an identifier off the wire, in either the typed or the bare form.
  factory EditionId.fromWire(String wire) {
    final space = wire.indexOf(' ');
    final id = space < 0 ? wire : wire.substring(space + 1);
    return EditionId(id);
  }

  /// The entity type, as the model declares it.
  static const String type = 'EDITION';

  /// The identifier itself.
  final String value;

  /// The form that travels on the wire.
  String get typed => '$type $value';

  @override
  bool operator ==(Object other) =>
      identical(this, other) || other is EditionId && other.value == value;

  @override
  int get hashCode => value.hashCode;

  @override
  String toString() => typed;
}
