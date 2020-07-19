package ancurio.duyguji.client.input.ext;

import ancurio.duyguji.client.input.AutocompleteWindow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.TextFieldWidget;

public interface ExtTextFieldWidget {
    static ExtTextFieldWidget from(final Element self) {
        assert(self instanceof ExtTextFieldWidget);
        return (ExtTextFieldWidget) self;
    }

    void enableAutocomplete(final MinecraftClient client);
    void setAutocompleteOffset(final int x, final int y);
    void setAutocompletePosition(final AutocompleteWindow.Position pos);
    // Returns true if the key event was consumed
    boolean onTabPressed();
}
