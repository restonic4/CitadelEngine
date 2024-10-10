package com.restonic4.citadel.render.gui.guis.editor;

import com.restonic4.ClientSide;
import com.restonic4.citadel.input.KeyListener;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.lwjgl.glfw.GLFW;

@ClientSide
public class EditorRenameImGui extends ToggleableImGuiScreen {
    private static final int windowWidth = 300;
    private static final float padding = 10.0f;
    private static final ImString inputBuffer = new ImString(128);

    private String defaultText;
    private Runnable runnable;

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        draw();
        handleAction();
    }

    public void draw() {
        ImGuiIO io = ImGui.getIO();
        float screenWidth = io.getDisplaySizeX();
        float screenHeight = io.getDisplaySizeY();

        float titleBarHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePadding().y * 2.0f;

        float inputHeight = ImGui.getTextLineHeightWithSpacing();
        float windowHeight = titleBarHeight + inputHeight + 2 * padding;

        float windowPosX = (screenWidth - windowWidth) / 2.0f;
        float windowPosY = (screenHeight - windowHeight) / 2.0f;

        ImGui.setNextWindowPos(windowPosX, windowPosY, ImGuiCond.Always);
        ImGui.setNextWindowSize(windowWidth, windowHeight);

        ImGui.begin("Renaming", ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoCollapse);
        ImGui.setWindowFocus();

        if (inputBuffer.isEmpty()) {
            inputBuffer.set(defaultText);
            ImGui.setKeyboardFocusHere();
        }

        ImGui.setCursorPos(padding, padding + titleBarHeight);

        float inputWidth = windowWidth - 2 * padding;
        ImGui.pushItemWidth(inputWidth);
        ImGui.inputText("##Input", inputBuffer, ImGuiInputTextFlags.None);
        ImGui.popItemWidth();

        ImGui.end();
    }

    public void handleAction() {
        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_ESCAPE)) {
            this.hide();
            reset();
        }
        else if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_ENTER)) {
            this.hide();

            runnable.run();

            reset();
        }
    }

    public String getResult() {
        return inputBuffer.get();
    }

    public void reset() {
        inputBuffer.clear();
    }

    public void setDefaultText(String text) {
        defaultText = text;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}