package com.restonic4.citadel.render.gui.guis;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImString;
import com.restonic4.ClientSide;
import com.restonic4.citadel.exceptions.NetworkException;
import com.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import com.restonic4.citadel.util.ColorHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

import java.util.List;

import static imgui.flag.ImGuiWindowFlags.NoCollapse;
import static imgui.flag.ImGuiWindowFlags.NoDocking;

@ClientSide
public class ServerConsoleImGui extends ToggleableImGuiScreen {
    private final ImString inputBuffer = new ImString(256);
    private boolean autoScroll = true;

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        ImGui.begin("Server console", NoDocking | NoCollapse);

        final float controlHeight = (ImGui.getTextLineHeight() * 2 + ImGui.getStyle().getFramePaddingY() * 2) * 3;
        final float windowHeight = ImGui.getWindowHeight();

        List<String> buffer = Logger.getPersistentLogger().getCompleteLogBuffer();

        ImGui.beginChild("LogArea", -1, windowHeight - controlHeight, true);
        for (int i = 0; i < buffer.size(); i++) {
            String line = buffer.get(i);

            if (line.contains("[DEBUG]")) {
                drawColoredText(line, "[DEBUG]", 0xFF00FF); // Purple for DEBUG
            } else if (line.contains("[ERROR]")) {
                drawColoredText(line, "[ERROR]", 0xFF0000); // Red for ERROR
            } else if (line.contains("[WARNING]")) {
                drawColoredText(line, "[WARNING]", 0xFFFF00); // Yellow for WARNING
            } else {
                ImGui.text(line); // Default color
            }
        }

        if (autoScroll) {
            ImGui.setScrollHereY(1.0f);
        }
        ImGui.endChild();

        ImGui.separator();

        ImGui.inputText("##Command", inputBuffer);
        ImGui.sameLine();
        if (ImGui.button("Send", -1, ImGui.getTextLineHeight() * 2 - ImGui.getStyle().getFramePaddingY() * 2)) {
            String command = inputBuffer.get();
            processCommand(command);
            inputBuffer.set("");
        }

        if (ImGui.button(autoScroll ? "Unlock Scroll" : "Lock Scroll")) {
            autoScroll = !autoScroll;
        }

        ImGui.sameLine();
        if (ImGui.button("Stop server")) {
            processCommand("stop");
        }

        ImGui.end();
    }

    private void drawColoredText(String line, String tag, int color) {
        int tagLength = tag.length();
        int tagIndex = line.indexOf(tag);

        if (tagIndex >= 0) {
            ImGui.pushStyleColor(ImGuiCol.Text, ColorHelper.hexToARGB(color));
            ImGui.text(line.substring(tagIndex, tagIndex + tagLength));
            ImGui.popStyleColor();

            ImGui.sameLine();

            if (tagIndex + tagLength < line.length()) {
                ImGui.text(line.substring(tagIndex + tagLength));
            }
        } else {
            ImGui.text(line);
        }
    }

    private void processCommand(String command) {
        if (command == null || command.isEmpty()) {
            return;
        }

        if (command.equals("stop")) {
            throw new NetworkException("Server stopped");
        }
        else if (command.equals("hide")) {
            ImGuiScreens.SERVER_CONSOLE.hide();
        }
        else {
            Logger.log("Unknown command!");
        }
    }
}
