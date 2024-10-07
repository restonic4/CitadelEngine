package com.restonic4.citadel.render.gui;

import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

public class ImGuiHelper {
    public static void textTruncated(String fullText) {
        float availableWidth = ImGui.getContentRegionAvail().x;
        ImVec2 textSize = ImGui.calcTextSize(fullText);

        String displayText = fullText;
        if (textSize.x > availableWidth) {
            displayText = truncateText(fullText, availableWidth);
        }

        ImGui.text(displayText);
    }

    public static boolean selectableTruncated(String fullText, boolean isSelected) {
        float availableWidth = ImGui.getContentRegionAvail().x;

        ImVec2 textSize = ImGui.calcTextSize(fullText);

        String displayText = fullText;
        if (textSize.x > availableWidth) {
            displayText = truncateText(fullText, availableWidth);
        }

        return ImGui.selectable(displayText, isSelected);
    }

    private static String truncateText(String text, float availableWidth) {
        final String ellipsis = "...";

        ImVec2 ellipsisSize = ImGui.calcTextSize(ellipsis);

        StringBuilder builder = StringBuilderHelper.getBuilder();
        builder.setLength(0);

        ImVec2 textSize = new ImVec2();

        for (int i = 0; i < text.length(); i++) {
            builder.append(text.charAt(i));

            ImGui.calcTextSize(textSize, builder.toString());

            if (textSize.x + ellipsisSize.x > availableWidth) {
                return builder.append(ellipsis).toString();
            }
        }

        return text;
    }

    public static void renderPropertyRow(String label, Runnable controlRenderer) {
        float textWidth = ImGui.calcTextSize(label).x + CitadelConstants.IM_GUI_INFO_COLUMN_PADDING;

        ImGui.columns(2, null, false);
        ImGui.setColumnWidth(0, Math.max(textWidth, CitadelConstants.IM_GUI_INFO_COLUMN_WIDTH)); // Ajuste dinámico para la columna del texto

        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.setNextItemWidth(-1);
        controlRenderer.run();

        ImGui.columns(1);
    }

    private static final int windowWidth = 300;
    private static final float padding = 10.0f;
    private static final ImString inputBuffer = new ImString(128);

    public static void renameBox(String defaultText) {
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

    public static String getRenameBoxResult() {
        return inputBuffer.get();
    }

    public static void resetRenameBox() {
        inputBuffer.clear();
    }

    public static void setCentered(float elementWidth) {
        float windowWidth = ImGui.getWindowSizeX();
        float cursorPosX = (windowWidth - elementWidth) / 2.0f;

        if (cursorPosX > 0) {
            ImGui.setCursorPosX(cursorPosX);
        }
    }

    public static void setCenteredText(String text) {
        float windowWidth = ImGui.getWindowSizeX();
        float cursorPosX = (windowWidth - ImGui.calcTextSize(text).x) / 2.0f;

        if (cursorPosX > 0) {
            ImGui.setCursorPosX(cursorPosX);
        }
    }
}
