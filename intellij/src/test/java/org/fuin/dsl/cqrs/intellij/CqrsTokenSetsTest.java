package org.fuin.dsl.cqrs.intellij;

import com.intellij.psi.tree.IElementType;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Guards that every keyword token ({@code KW_*}) generated from the grammar is registered in
 * {@link CqrsTokenSets#KEYWORDS}. A keyword that is missing from that set still parses, but the
 * syntax highlighter never colours it - so this test fails whenever a new keyword is added to the
 * grammar without also being added to the highlighted keyword set.
 */
public class CqrsTokenSetsTest {

    @Test
    public void everyKeywordTokenIsHighlighted() throws IllegalAccessException {
        List<String> missing = new ArrayList<>();
        for (Field field : CqrsTypes.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers())
                    && field.getName().startsWith("KW_")
                    && IElementType.class.isAssignableFrom(field.getType())) {
                IElementType token = (IElementType) field.get(null);
                if (!CqrsTokenSets.KEYWORDS.contains(token)) {
                    missing.add(field.getName());
                }
            }
        }
        assertTrue("Keyword tokens missing from CqrsTokenSets.KEYWORDS (they would not be highlighted): "
                + missing, missing.isEmpty());
    }
}
