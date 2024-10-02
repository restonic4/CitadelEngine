package com.restonic4.citadel.render.gui;

import com.restonic4.citadel.util.StringBuilderHelper;
import imgui.ImGui;
import imgui.ImVec2;

public class ImGuiHelper {
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
}
