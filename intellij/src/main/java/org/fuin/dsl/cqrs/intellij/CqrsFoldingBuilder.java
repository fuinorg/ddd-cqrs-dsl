package org.fuin.dsl.cqrs.intellij;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SyntaxTraverser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import org.fuin.dsl.cqrs.intellij.psi.CqrsTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Makes every multi-line {@code { ... }} block and every multi-line block/doc comment collapsible.
 * <p>
 * Regions are derived by matching the {@code {} } <em>tokens</em> with a stack rather than by
 * inspecting the block-bearing PSI rules. A rule may contain more than one brace pair &ndash;
 * {@code enum_object} wraps both its body and its {@code instances} list &ndash; so pairing the
 * first {@code LBRACE} child with the last {@code RBRACE} child would collapse the two into a
 * single region. Token matching also keeps this builder independent of the grammar and tolerates
 * the unbalanced braces that occur while the user is typing.
 */
public final class CqrsFoldingBuilder extends FoldingBuilderEx implements DumbAware {

    private static final String BLOCK_PLACEHOLDER = "{...}";

    private static final String DOC_COMMENT_PLACEHOLDER = "/**...*/";

    private static final String BLOCK_COMMENT_PLACEHOLDER = "/*...*/";

    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root,
                                                          @NotNull Document document,
                                                          boolean quick) {
        final List<FoldingDescriptor> descriptors = new ArrayList<>();
        final Deque<PsiElement> openBraces = new ArrayDeque<>();

        for (PsiElement element : SyntaxTraverser.psiTraverser(root)) {
            if (element.getFirstChild() != null) {
                // Only leaf tokens carry the braces and comments.
                continue;
            }
            final IElementType type = PsiUtilCore.getElementType(element);
            if (type == CqrsTypes.LBRACE) {
                openBraces.push(element);
            } else if (type == CqrsTypes.RBRACE) {
                if (openBraces.isEmpty()) {
                    // Stray closing brace while typing.
                    continue;
                }
                addRegion(descriptors, document, openBraces.pop(), element, BLOCK_PLACEHOLDER);
            } else if (type == CqrsTypes.DOC_COMMENT) {
                addRegion(descriptors, document, element, element, DOC_COMMENT_PLACEHOLDER);
            } else if (type == CqrsTypes.BLOCK_COMMENT) {
                addRegion(descriptors, document, element, element, BLOCK_COMMENT_PLACEHOLDER);
            }
        }

        // Opening braces left on the stack have no partner yet and simply do not fold.
        return descriptors.toArray(FoldingDescriptor.EMPTY_ARRAY);
    }

    /** Adds a descriptor unless the range is empty, out of bounds or confined to a single line. */
    private static void addRegion(List<FoldingDescriptor> descriptors, Document document,
                                  PsiElement start, PsiElement end, String placeholder) {
        final int startOffset = start.getTextRange().getStartOffset();
        final int endOffset = end.getTextRange().getEndOffset();
        if (startOffset >= endOffset || endOffset > document.getTextLength()) {
            return;
        }
        if (document.getLineNumber(startOffset) == document.getLineNumber(endOffset - 1)) {
            return;
        }
        // The anchor's own range must contain the folded range, so a brace pair is anchored on the
        // element enclosing both braces -- never on the one-character '{' leaf, whose range the
        // platform would reject.
        final PsiElement anchor = start == end ? start : PsiTreeUtil.findCommonParent(start, end);
        if (anchor == null) {
            return;
        }
        descriptors.add(new FoldingDescriptor(anchor, startOffset, endOffset, null, placeholder));
    }

    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        // Every descriptor above carries its own placeholder; this only serves as a fallback.
        final IElementType type = node.getElementType();
        if (type == CqrsTypes.DOC_COMMENT) {
            return DOC_COMMENT_PLACEHOLDER;
        }
        if (type == CqrsTypes.BLOCK_COMMENT) {
            return BLOCK_COMMENT_PLACEHOLDER;
        }
        return BLOCK_PLACEHOLDER;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}
