package org.fuin.dsl.cqrs.conversion;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.List;
import java.util.Set;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

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
@SuppressWarnings("all")
public class CqrsDslValueConverterService extends DefaultTerminalConverters {
  /**
   * Strips/adds the caret escape per dot separated segment. A {@code *} wildcard segment is never
   * escaped.
   */
  private static class QualifiedNameCaretConverter implements IValueConverter<String> {
    private final Set<String> keywords;

    public QualifiedNameCaretConverter(final Set<String> keywords) {
      this.keywords = keywords;
    }

    @Override
    public String toValue(final String string, final INode node) throws ValueConverterException {
      if ((string == null)) {
        return null;
      }
      return string.replaceAll("(^|\\.)\\^", "$1");
    }

    @Override
    public String toString(final String value) throws ValueConverterException {
      if ((value == null)) {
        throw new ValueConverterException("Qualified name may not be null", null, null);
      }
      final Function1<String, String> _function = (String segment) -> {
        String _xifexpression = null;
        boolean _contains = this.keywords.contains(segment);
        if (_contains) {
          _xifexpression = ("^" + segment);
        } else {
          _xifexpression = segment;
        }
        return _xifexpression;
      };
      return IterableExtensions.join(ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(value.split("\\.", (-1)))), _function), ".");
    }
  }

  @Inject
  private IGrammarAccess grammarAccess;

  private IValueConverter<String> qualifiedNameConverter;

  @ValueConverter(rule = "FQN")
  public IValueConverter<String> getFQNConverter() {
    return this.getQualifiedNameConverter();
  }

  @ValueConverter(rule = "FQNWithWildcard")
  public IValueConverter<String> getFQNWithWildcardConverter() {
    return this.getQualifiedNameConverter();
  }

  private IValueConverter<String> getQualifiedNameConverter() {
    if ((this.qualifiedNameConverter == null)) {
      Set<String> _allKeywords = GrammarUtil.getAllKeywords(this.grammarAccess.getGrammar());
      CqrsDslValueConverterService.QualifiedNameCaretConverter _qualifiedNameCaretConverter = new CqrsDslValueConverterService.QualifiedNameCaretConverter(_allKeywords);
      this.qualifiedNameConverter = _qualifiedNameCaretConverter;
    }
    return this.qualifiedNameConverter;
  }
}
