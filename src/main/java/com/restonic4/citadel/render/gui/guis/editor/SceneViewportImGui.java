package com.restonic4.citadel.render.gui.guis.editor;

import com.restonic4.ClientSide;
import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.platform.PlatformManager;
import com.restonic4.citadel.registries.built_in.managers.FrameBuffers;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import com.restonic4.citadel.util.helpers.StringBuilderHelper;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiWindowFlags;

@ClientSide
public class SceneViewportImGui extends ToggleableImGuiScreen {
    private boolean isFocused;
    private boolean isRightClick;

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        ImGui.begin("Scene Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        isFocused = ImGui.isWindowFocused();
        isRightClick = ImGui.isWindowHovered() && ImGui.isMouseDown(ImGuiMouseButton.Right);

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);
        int textureId = FrameBuffers.SCENE_VIEWPORT.getTextureId();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        if (!CitadelLauncher.getInstance().getSettings().shouldGenerateBindlessTextures()) {
            renderErrorMessage(StringBuilderHelper.concatenate("Could not render due to an incompatibility", PlatformManager.getEndOfLine(), "with your graphics card."), windowPos.x, windowPos.y, windowSize.x, windowSize.y);
        }

        ImGui.end();
    }

    private void renderErrorMessage(String message, float x, float y, float width, float height) {
        ImVec2 textSize = ImGui.calcTextSize(message);

        float textPosX = x + (width - textSize.x) / 2;
        float textPosY = y + (height - textSize.y) / 2;

        ImGui.setCursorPos(textPosX, textPosY);

        ImGui.pushStyleColor(ImGuiCol.Text, 1.0f, 0.5f, 0.0f, 1.0f);
        ImGui.text(message);
        ImGui.popStyleColor();
    }

    private static ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getEditorAspectRatio();
        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getEditorAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private static ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    public boolean isWindowFocused() {
        return isFocused;
    }

    public boolean isRightClickDetected() {
        return isRightClick;
    }
}