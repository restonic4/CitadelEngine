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

        StringBuilderHelper.getBuilder().setLength(0);

        for (int i = 0; i < text.length(); i++) {
            String nextText = StringBuilderHelper.getBuilder().toString() + text.charAt(i);

            ImVec2 nextTextSize = ImGui.calcTextSize(nextText);

            if (nextTextSize.x + ellipsisSize.x > availableWidth) {
                return StringBuilderHelper.getBuilder().append(ellipsis).toString();
            }

            StringBuilderHelper.getBuilder().append(text.charAt(i));
        }

        return text;
    }

}
