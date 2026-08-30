package org.fuin.dsl.ddd.gen.base

import java.util.List
import org.fuin.dsl.cqrs.cqrsDsl.EnumObject
import org.fuin.dsl.cqrs.cqrsDsl.Variable
import org.fuin.srcgen4j.core.emf.CodeSnippet
import org.fuin.srcgen4j.core.emf.CodeSnippetContext

import static extension org.fuin.dsl.cqrs.extensions.CqrsCollectionExtensions.*
import static extension org.fuin.dsl.ddd.gen.extensions.TypeExtensions.*

/**
 * Creates the {@code valueOf}/{@code isValid} pair and the annotations that advertise them, for an
 * identifier built from more than one attribute.
 * <p>
 * An identifier without these is <b>invisible to the runtime</b>: {@code JandexEntityIdFactory} registers
 * a type only when it carries {@code @HasEntityTypeConstant}, {@code @HasPublicStaticIsValidMethod} and
 * {@code @HasPublicStaticValueOfMethod} together, and silently skips it otherwise - after which every
 * command carrying such an identifier fails to deserialize with "Unknown type: X". A single-attribute
 * identifier inherits the pair from its base type ({@code AggregateRootUuid} and friends); a composite one
 * has no base, which is the gap this fills.
 * <p>
 * <b>The whole string form is generated, and is not a per-project choice.</b> {@link #abstractPart} emits
 * the {@code SEPARATOR}, {@code asString()}, and the reading and checking that undo them, into the
 * regenerated abstract class; {@link #toString} emits two one-line delegators into the generate-once final
 * class.
 * <p>
 * It used to be the other way round: the separator and {@code asString()} were emitted into the final class
 * under a comment inviting an override, so that changing the encoding was an edit a developer kept. That was
 * defensible while every client shared the jar. It stopped being defensible when a second target began
 * composing the same identifier from the same declaration - a hand-edited separator would leave the two
 * languages disagreeing about what a valid identifier is, with nothing on either side able to notice.
 * {@code asString()} is emitted {@code final} for that reason. A different string form is now a change to
 * this generator, or to the model it reads.
 * <p>
 * <b>The annotations are only emitted together with the methods.</b> The factory resolves the method
 * eagerly from the annotation, so advertising one that does not exist turns a silent skip into a startup
 * failure - which is worse. When a part cannot be parsed back from its string form, nothing is emitted and
 * a TODO explains what to add by hand.
 * <p>
 * <b>Validating and converting are separate, and neither uses exceptions for control flow.</b>
 * {@code isValid} asks non-throwing predicates - an enum compared against its constants, a date through
 * {@code parseUnresolved}, a number by accumulating digits - and returns a boolean; nothing is caught.
 * {@code valueOf} asks the same predicates first and then <b>throws a precise
 * {@code IllegalArgumentException}</b> naming the value it refused, because a converter that answered
 * {@code null} instead would only defer the problem to a misleading {@code NullPointerException} later.
 * The conversion itself runs after the check, so it cannot fail.
 */
class SrcIdStringMethods implements CodeSnippet {

    val CodeSnippetContext ctx

    val String className

    val String abstractClassName

    val List<? extends Variable> attributes

    /**
     * Constructor with all mandatory data.
     *
     * @param ctx Context.
     * @param className Name of the final identifier class - the type that is created and named in messages.
     * @param abstractClassName Name of the abstract class holding the machinery.
     * @param attributes Attributes the identifier is composed of.
     */
    new(CodeSnippetContext ctx, String className, String abstractClassName,
        List<? extends Variable> attributes) {
        this.ctx = ctx
        this.className = className
        this.abstractClassName = abstractClassName
        this.attributes = attributes
    }

    /**
     * Says whether every part can be converted back from its string form, and the methods can therefore
     * be generated.
     *
     * @return TRUE if the pair is generated.
     */
    def boolean supported() {
        if (attributes.nullSafe.size < 2) {
            // A single-attribute identifier gets the pair from its base type.
            return false
        }
        return attributes.forall[kindOf(it) !== null]
    }

    /**
     * Returns the annotations that go on the final class, or "" when the methods cannot be generated.
     *
     * @return Annotation lines.
     */
    def String annotations() {
        if (!supported) {
            return ""
        }
        ctx.requiresImport("org.fuin.ddd4j.core.HasEntityTypeConstant")
        ctx.requiresImport("org.fuin.objects4j.common.HasPublicStaticIsValidMethod")
        ctx.requiresImport("org.fuin.objects4j.common.HasPublicStaticValueOfMethod")
        '''
        @HasEntityTypeConstant
        @HasPublicStaticIsValidMethod
        @HasPublicStaticValueOfMethod'''
    }

    /**
     * Returns the machinery for the abstract class: the factory interface, the reading and the checks.
     *
     * @return Source code, or "" when this identifier needs none.
     */
    def String abstractPart() {
        if (!supported) {
            return ""
        }
        ctx.requiresImport("java.util.regex.Pattern")
        ctx.requiresImport("org.jspecify.annotations.Nullable")
        val count = attributes.size
        '''

        /** Separates the parts in the string form of this identifier. */
        public static final String SEPARATOR = "-";

        /**
         * Returns the parts joined by {@link #SEPARATOR}, which is the form the identifier travels in and
         * the form {@link #valueOf(String, Factory)} reads back.
         *
         * @return String form of this identifier.
         */
        @Override
        public final String asString() {
            return «FOR a : attributes SEPARATOR ' + SEPARATOR + '»get«a.name.toFirstUpper»()«ENDFOR»;
        }

        /**
         * Creates the identifier from its parts - the concrete class passes its own constructor, which is
         * what lets the reading below live here instead of being repeated in every identifier.
         *
         * @param <T> Type that is created.
         */
        @FunctionalInterface
        public interface Factory<T> {

            /**
             * Creates the identifier.
             *
             «FOR attr : attributes»
             * @param «attr.name» Part of the identifier.
             «ENDFOR»
             *
             * @return New instance.
             */
            T create(«FOR attr : attributes SEPARATOR ', '»«attr.type.simpleName(ctx)» «attr.name»«ENDFOR»);

        }

        /**
         * Converts a string form back into an identifier.
         *
         * @param value String to convert. A {@literal null} value returns {@literal null}.
         * @param factory Creates the identifier from the converted parts.
         * @param <T> Type that is created.
         *
         * @return Converted value.
         *
         * @throws IllegalArgumentException The value is no valid «className». Refusing loudly is the point:
         *                                  a converter that answered null would turn a bad string into a
         *                                  {@code NullPointerException} somewhere else entirely.
         */
        @Nullable
        public static <T> T valueOf(@Nullable final String value, final Factory<T> factory) {
            if (value == null) {
                return null;
            }
            final String[] parts = split(value);
            if (!validParts(parts)) {
                throw new IllegalArgumentException("Not a valid «className»: " + value);
            }
            // Every part was checked above, so none of these conversions can fail.
            return factory.create(«FOR i : 0 ..< count SEPARATOR ', '»«conversionOf(attributes.get(i), i)»«ENDFOR»);
        }

        /**
         * Verifies that a given string can be converted into the type.
         *
         * @param value Value to validate.
         *
         * @return Returns {@literal true} if it's a valid type else {@literal false}.
         */
        public static boolean isValid(@Nullable final String value) {
            return value == null || validParts(split(value));
        }

        /**
         * Splits a string form into its parts. Limited to the number of parts and applied left to right, so
         * only the last one may contain the separator itself - which is what makes a trailing date work.
         *
         * @param value Value to split.
         *
         * @return Parts, as many as the split found.
         */
        private static String[] split(final String value) {
            return value.split(Pattern.quote(SEPARATOR), «count»);
        }

        /**
         * Says whether the parts can all be converted. Every check is non-throwing, which is what lets
         * {@code isValid} answer without catching anything.
         *
         * @param parts Parts to check.
         *
         * @return TRUE if the parts make up a valid «className».
         */
        private static boolean validParts(final String[] parts) {
            if (parts.length != «count») {
                return false;
            }
            return «FOR i : 0 ..< count SEPARATOR "\n    && "»«checkOf(attributes.get(i), i)»«ENDFOR»;
        }
        «helpers»'''
    }

    /**
     * Returns what the final class declares: the two methods the runtime looks for, each delegating to the
     * abstract class, which is where the encoding they undo is generated.
     *
     * @return Source code, or a TODO when the pair cannot be generated.
     */
    override toString() {
        if (!supported) {
            if (attributes.nullSafe.size < 2) {
                return ""
            }
            return '''
            // TODO A composite identifier needs a public static valueOf(String)/isValid(String) pair plus
            // @HasEntityTypeConstant, @HasPublicStaticIsValidMethod and @HasPublicStaticValueOfMethod, or
            // JandexEntityIdFactory skips this type and commands carrying it fail to deserialize. The
            // generator could not derive the parsing for every part of this identifier - add both methods
            // and all three annotations by hand, and an asString() to match, since none was generated.
            // Whatever is written here a non-JVM client cannot know, so such an identifier cannot be
            // composed by one.
            '''
        }
        ctx.requiresImport("org.jspecify.annotations.Nullable")
        '''
        /**
         * Converts the string form produced by {@link #asString()} back into an identifier.
         *
         * @param value String to convert. A {@literal null} value returns {@literal null}.
         *
         * @return Converted value.
         *
         * @throws IllegalArgumentException The value is no valid «className».
         */
        @Nullable
        public static «className» valueOf(@Nullable final String value) {
            return «abstractClassName».valueOf(value, «className»::new);
        }

        /**
         * Verifies that a given string can be converted into the type.
         *
         * @param value Value to validate.
         *
         * @return Returns {@literal true} if it's a valid type else {@literal false}.
         */
        public static boolean isValid(@Nullable final String value) {
            return «abstractClassName».isValid(value);
        }
        '''
    }

    /**
     * Returns the expression that converts one checked part into the attribute's type. Safe to call the
     * throwing JDK converters here: {@code validParts} has already ruled out everything they would reject.
     */
    private def String conversionOf(Variable attribute, int index) {
        val name = attribute.type.simpleName(ctx)
        switch (kindOf(attribute)) {
            case "enum": name + ".valueOf(parts[" + index + "])"
            case "String": "parts[" + index + "]"
            case "Integer": "Integer.valueOf(parts[" + index + "])"
            case "Long": "Long.valueOf(parts[" + index + "])"
            case "UUID": "UUID.fromString(parts[" + index + "])"
            case "LocalDate": "LocalDate.parse(parts[" + index + "])"
            default: null
        }
    }

    /**
     * Returns the non-throwing check for one part. Every kind rejects an empty part: it carries no value,
     * and an identifier built from one would not survive a round trip through {@code asString()}.
     */
    private def String checkOf(Variable attribute, int index) {
        val name = attribute.type.simpleName(ctx)
        switch (kindOf(attribute)) {
            case "enum": "isValidName(" + name + ".values(), parts[" + index + "])"
            case "String": "!parts[" + index + "].isEmpty()"
            case "Integer": "isValidNumber(parts[" + index + "], Integer.MAX_VALUE)"
            case "Long": "isValidNumber(parts[" + index + "], Long.MAX_VALUE)"
            case "UUID": "UUID_PATTERN.matcher(parts[" + index + "]).matches()"
            case "LocalDate": "isValidIsoDate(parts[" + index + "])"
            default: null
        }
    }

    /**
     * Returns the private checks the predicates above need - only those actually used.
     */
    private def String helpers() {
        val kinds = attributes.map[kindOf(it)].toSet
        val result = new StringBuilder
        if (kinds.contains("UUID")) {
            ctx.requiresImport("java.util.UUID")
            result.append('''

            /** Form UUID.fromString accepts - checked first so that call cannot fail. */
            private static final Pattern UUID_PATTERN = Pattern.compile(
                "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
            ''')
        }
        if (kinds.contains("enum")) {
            result.append('''

            /**
             * Says whether a string names one of the constants - the non-throwing counterpart of
             * {@code Enum.valueOf}.
             *
             * @param values Constants to compare against.
             * @param value Value to check.
             *
             * @return TRUE if a constant has that name.
             */
            private static boolean isValidName(final Enum<?>[] values, final String value) {
                for (final Enum<?> candidate : values) {
                    if (candidate.name().equals(value)) {
                        return true;
                    }
                }
                return false;
            }
            ''')
        }
        if (kinds.contains("Integer") || kinds.contains("Long")) {
            result.append('''

            /**
             * Says whether a string is a whole number that fits: the digits are accumulated and the range is
             * checked as it goes, so neither a stray character nor an overflow raises anything.
             *
             * @param value Value to check.
             * @param max Largest value the target type holds.
             *
             * @return TRUE if the value converts without failing.
             */
            private static boolean isValidNumber(final String value, final long max) {
                if (value.isEmpty()) {
                    return false;
                }
                final boolean negative = value.charAt(0) == '-';
                if (negative && value.length() == 1) {
                    return false;
                }
                long result = 0;
                for (int i = negative ? 1 : 0; i < value.length(); i++) {
                    final int digit = Character.digit(value.charAt(i), 10);
                    if (digit < 0 || result > (max - digit) / 10) {
                        return false;
                    }
                    result = result * 10 + digit;
                }
                return true;
            }
            ''')
        }
        if (kinds.contains("LocalDate")) {
            ctx.requiresImport("java.text.ParsePosition")
            ctx.requiresImport("java.time.format.DateTimeFormatter")
            ctx.requiresImport("java.time.temporal.ChronoField")
            ctx.requiresImport("java.time.temporal.TemporalAccessor")
            result.append('''

            /**
             * Says whether a string is an ISO date - {@code parseUnresolved} reports a failure by returning
             * null instead of raising, unlike {@code LocalDate.parse}.
             *
             * @param value Value to check.
             *
             * @return TRUE if the value converts without failing.
             */
            private static boolean isValidIsoDate(final String value) {
                final ParsePosition position = new ParsePosition(0);
                final TemporalAccessor parsed =
                    DateTimeFormatter.ISO_LOCAL_DATE.parseUnresolved(value, position);
                return parsed != null
                    && position.getIndex() == value.length()
                    && parsed.isSupported(ChronoField.YEAR)
                    && parsed.isSupported(ChronoField.MONTH_OF_YEAR)
                    && parsed.isSupported(ChronoField.DAY_OF_MONTH);
            }
            ''')
        }
        return result.toString
    }

    /**
     * Returns a token naming how a part is read back, or null when the generator does not know how.
     */
    private def String kindOf(Variable attribute) {
        val type = attribute.type
        if (type === null) {
            return null
        }
        if (type instanceof EnumObject) {
            return "enum"
        }
        switch (type.simpleName(ctx)) {
            case "String": "String"
            case "Integer": "Integer"
            case "Long": "Long"
            case "UUID": "UUID"
            case "LocalDate": "LocalDate"
            default: null
        }
    }

}
