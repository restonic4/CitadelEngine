package me.restonic4.citadel.render.gui.guis;

import me.restonic4.ClientSide;
import me.restonic4.citadel.registries.built_in.types.ImGuiScreen;

@ClientSide
public class ToggleableImGuiScreen extends ImGuiScreen {
    private boolean isVisible = false;

    public void show() {
        isVisible = true;
    }

    public void hide() {
        isVisible = false;
    }

    public void toggle() {
        this.isVisible = !this.isVisible;
    }

    public boolean isVisible() {
        return this.isVisible;
    }
}
