import 'package:melkheftken_contract/src-gen/books/acquisition.dart';
import 'package:melkheftken_contract/src/json/json.dart';

/// A batch of copies that came in one way on one day.
///
/// The counterpart of EditionId: its parts are an enum and a date, both of which a round trip can
/// read back out of the joined string form, so both targets generate the encoding. EditionId's
/// leading BookId is a UUID full of separators and cannot be split back out, which is why neither
/// target generates one for it.
///
/// On the wire an identifier is **typed** - `INTAKE <string>` - and that is what both the
/// `entity-id-path` of a command and the `id` of a read-model row carry. The type is held here
/// so that no caller ever spells it out.
class IntakeId {
  /// Constructor with mandatory data.
  const IntakeId(this.value);

  /// Builds the identifier from its parts, the way the write side composes it.
  ///
  /// A natural key rather than a surrogate, so there is nothing to mint: the identifier follows
  /// from the values themselves.
  factory IntakeId.of(Acquisition acquisition, DateTime arrivedOn) =>
      IntakeId('${_escaped(acquisition.wireName)}-${wireDate(arrivedOn)!}');

  /// Reads an identifier off the wire, in either the typed or the bare form.
  factory IntakeId.fromWire(String wire) {
    final space = wire.indexOf(' ');
    final id = space < 0 ? wire : wire.substring(space + 1);
    return IntakeId(id);
  }

  /// The entity type, as the model declares it.
  static const String type = 'INTAKE';

  /// The identifier itself.
  final String value;

  /// The form that travels on the wire.
  String get typed => '$type $value';

  @override
  bool operator ==(Object other) =>
      identical(this, other) || other is IntakeId && other.value == value;

  @override
  int get hashCode => value.hashCode;

  @override
  String toString() => typed;
}

/// Escapes one part of a composite identifier so that it cannot be mistaken for two.
///
/// The write side joins the parts with `-` and splits them back on it, letting only the last
/// part carry one - so every earlier part has its separators escaped, and the escape character
/// with them. This has to produce exactly what the JVM's `escape` produces, down to doing the
/// escape character first: reverse the two and an escaped separator comes back double-escaped.
String _escaped(String value) => value.replaceAll('%', '%25').replaceAll('-', '%2D');
