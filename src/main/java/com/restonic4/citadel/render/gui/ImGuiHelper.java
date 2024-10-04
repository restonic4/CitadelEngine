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
    private static final int windowHeight = 100;
    private static final float padding = 10.0f;
    private static final ImString inputBuffer = new ImString(128);

    public static void drawCenteredWindow(String defaultText) {
        ImGuiIO io = ImGui.getIO();
        float screenWidth = io.getDisplaySizeX();
        float screenHeight = io.getDisplaySizeY();

        // Calcular la posición de la ventana centrada
        float windowPosX = (screenWidth - windowWidth) / 2.0f;
        float windowPosY = (screenHeight - windowHeight) / 2.0f;

        ImGui.setNextWindowPos(windowPosX, windowPosY, ImGuiCond.Always);
        ImGui.setNextWindowSize(windowWidth, windowHeight);

        // Iniciar la ventana ImGui
        ImGui.begin("Renaming");

        // Calcular el tamaño del campo de texto teniendo en cuenta el padding
        float inputWidth = windowWidth - 2 * padding;
        float inputHeight = ImGui.getTextLineHeight() + padding * 2;  // Ajustar a la altura del texto con padding

        // Calcular la posición para centrar verticalmente el campo de texto
        float inputPosX = padding;  // Ya está considerado en setCursorPos
        float inputPosY = (windowHeight - inputHeight) / 2.0f;  // Centrar en altura

        ImGui.setCursorPos(inputPosX, inputPosY);  // Colocar el cursor en la posición calculada

        // Si el buffer está vacío, usar el texto por defecto
        if (inputBuffer.isEmpty()) {
            inputBuffer.set(defaultText);
        }

        // Ajustar el ancho del campo de texto y dibujarlo
        ImGui.pushItemWidth(inputWidth);
        ImGui.inputText("##input", inputBuffer, ImGuiInputTextFlags.AutoSelectAll);
        ImGui.popItemWidth();

        ImGui.end();  // Terminar la ventana ImGui
    }
}
