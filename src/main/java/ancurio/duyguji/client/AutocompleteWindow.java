package ancurio.duyguji.client;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class AutocompleteWindow extends DrawableHelper {
    private final TextRenderer textRenderer;
    private int bgColor;
    private int bgSelectionColor;

    public enum Position {
        ABOVE,
        BELOW
    }

    public AutocompleteWindow(final TextRenderer textRenderer, final int bgColor, final int bgSelectionColor) {
        this.textRenderer = textRenderer;
        this.bgColor = bgColor;
        this.bgSelectionColor = bgSelectionColor;
    }

    public final static class Data {
        public List<Map.Entry<String, String>> suggestions = Collections.emptyList();
        public int selectionIndex = -1;

        public static final Data EMPTY = new Data();

        public void moveSelection(final int delta) {
            if (suggestions.size() == 0) {
                return;
            }

            selectionIndex += (delta + suggestions.size());
            selectionIndex %= suggestions.size();
        }

        public String selectedSymbol() {
            return suggestions.get(selectionIndex).getValue();
        }
    };

    public void render(final MatrixStack matrices, final Data data, final int windowX, final int windowYLower) {
        if (data.suggestions.size() == 0) {
            return;
        }

        final int textHeight = textRenderer.fontHeight;
        final int verticalPadding = 1;
        final int horizontalPadding = 2;
        final int symbolAreaPadding = 4;

        int maxSymbolWidth = 10;
        int maxMnemonicWidth = 0;

        for (final Map.Entry<String, String> entry : data.suggestions) {
            final String mnemonic = entry.getKey();
            final String symbol = entry.getValue();
            maxMnemonicWidth = Math.max(maxMnemonicWidth, textRenderer.getWidth(mnemonic));
            maxSymbolWidth = Math.max(maxSymbolWidth, textRenderer.getWidth(symbol));
        }

        // ______________________
        // |0|1+2|     3      |0|  <- entryWidth
        //
        // 0: horizontalPadding
        // 1: maxSymbolWidth
        // 2: symbolAreaPadding
        // 3: maxMnemonicWidth
        //
        // ----------------------|
        //    verticalPadding    |
        // ----------------------|
        //                       |
        //       textHeight      | <- entryHeight
        //                       |
        // ----------------------|
        //    verticalPadding    |
        // ----------------------|

        final int symbolAreaWidth = symbolAreaPadding + maxSymbolWidth;
        final int entryWidth = horizontalPadding*2 + symbolAreaWidth + maxMnemonicWidth;
        final int entryHeight = verticalPadding*2 + textHeight;
        final int count = data.suggestions.size();
        final int windowY = windowYLower - count*entryHeight;

        // Background
        fill(matrices, windowX, windowY, windowX + entryWidth, windowYLower, bgColor);

        // Selection
        final int y0 = windowY + data.selectionIndex*entryHeight;
        fill(matrices, windowX, y0 + entryHeight, windowX + entryWidth, y0, bgSelectionColor);

        final int symbolX = windowX + horizontalPadding;
        final int mnemonicX = symbolX + symbolAreaWidth;
        final int symbolColor = 0xFFFFFFFF;
        final int mnemonicColor = symbolColor;
        int i = 0;

        // Suggestions
        for (final Map.Entry<String, String> entry : data.suggestions) {
            final String symbol = entry.getValue();
            final String mnemonic = entry.getKey();
            final int y = windowY + i*entryHeight + verticalPadding;
            final int symbolWidth = textRenderer.getWidth(symbol);
            final float symbolOffset = (symbolAreaWidth - symbolWidth) / 2.0f;

            textRenderer.draw(matrices, symbol, symbolX + symbolOffset, y, symbolColor);
            textRenderer.draw(matrices, mnemonic, mnemonicX, y, mnemonicColor);

            i++;
        }
    }
}
