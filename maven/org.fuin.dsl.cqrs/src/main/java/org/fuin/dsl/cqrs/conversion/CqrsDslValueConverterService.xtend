package org.fuin.dsl.cqrs.conversion

import com.google.inject.Inject
import com.google.inject.Singleton
import java.util.Set
import org.eclipse.xtext.GrammarUtil
import org.eclipse.xtext.IGrammarAccess
import org.eclipse.xtext.conversion.IValueConverter
import org.eclipse.xtext.conversion.ValueConverter
import org.eclipse.xtext.conversion.ValueConverterException
import org.eclipse.xtext.common.services.DefaultTerminalConverters
import org.eclipse.xtext.nodemodel.INode

/**
 * Adds caret ('^') escaping for qualified names so that a name segment that equals a keyword
 * (e.g. {@code ^event}, {@code ^type}, {@code ^context}) can be used as a plain identifier.
 * <p>
 * The simple {@code ID} rule is already handled by the inherited default {@code IDValueConverter}
 * (it strips a leading {@code ^} and re-adds it for keywords on serialization). Only the custom
 * {@code FQN} and {@code FQNWithWildcard} datatype rules need their own converter, because a
 * datatype rule does not apply the {@code ID} converter to its individual segments.
 */
@Singleton
class CqrsDslValueConverterService extends DefaultTerminalConverters {

	@Inject IGrammarAccess grammarAccess

	IValueConverter<String> qualifiedNameConverter

	@ValueConverter(rule="FQN")
	def IValueConverter<String> getFQNConverter() {
		return getQualifiedNameConverter()
	}

	@ValueConverter(rule="FQNWithWildcard")
	def IValueConverter<String> getFQNWithWildcardConverter() {
		return getQualifiedNameConverter()
	}

	private def IValueConverter<String> getQualifiedNameConverter() {
		if (qualifiedNameConverter === null) {
			qualifiedNameConverter = new QualifiedNameCaretConverter(GrammarUtil.getAllKeywords(grammarAccess.grammar))
		}
		return qualifiedNameConverter
	}

	/**
	 * Strips/adds the caret escape per dot separated segment. A {@code *} wildcard segment is never
	 * escaped.
	 */
	private static class QualifiedNameCaretConverter implements IValueConverter<String> {

		val Set<String> keywords

		new(Set<String> keywords) {
			this.keywords = keywords
		}

		override String toValue(String string, INode node) throws ValueConverterException {
			if (string === null) {
				return null
			}
			// Remove a caret that escapes a segment: at the very start or directly after a dot.
			return string.replaceAll("(^|\\.)\\^", "$1")
		}

		override String toString(String value) throws ValueConverterException {
			if (value === null) {
				throw new ValueConverterException("Qualified name may not be null", null, null)
			}
			return value.split("\\.", -1).map[segment|if (keywords.contains(segment)) "^" + segment else segment].join(".")
		}
	}
}
