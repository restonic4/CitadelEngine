package com.restonic4.citadel.render.gui;

import com.restonic4.citadel.registries.built_in.managers.KeyBinds;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.flag.*;
import imgui.type.ImString;
import org.joml.Vector4f;

public class ImGuiHelper {
    private static final Vector4f toolTipColor = new Vector4f(0.5f, 0.5f, 0.5f, 1);

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

    public static void coloredText(String text, Vector4f color) {
        ImGui.pushStyleColor(ImGuiCol.Text, color.x, color.y, color.z, color.w);
        ImGui.text(text);
        ImGui.popStyleColor();
    }

    public static Vector4f getTooltipColor() {
        return toolTipColor;
    }
}
