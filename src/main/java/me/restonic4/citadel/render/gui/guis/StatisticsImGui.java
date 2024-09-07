package me.restonic4.citadel.render.gui.guis;

import imgui.ImGui;
import imgui.extension.implot.ImPlot;
import me.restonic4.citadel.core.Window;
import me.restonic4.citadel.registries.built_in.types.ImGuiScreen;
import me.restonic4.citadel.render.Camera;
import me.restonic4.citadel.render.Renderer;
import me.restonic4.citadel.render.gui.LineGraphImGui;
import me.restonic4.citadel.sound.SoundManager;
import me.restonic4.citadel.util.CitadelConstants;
import me.restonic4.citadel.util.Time;
import me.restonic4.citadel.world.Scene;
import me.restonic4.citadel.world.SceneManager;

public class StatisticsImGui extends ToggleableImGuiScreen {
    int graphWidth = 500;
    int graphHeight = 200;

    LineGraphImGui fpsGraph = new LineGraphImGui("FPS", 1000, graphWidth, graphHeight, 1000, 300);
    LineGraphImGui renderingGraph = new LineGraphImGui("Rendering", 1000, graphWidth, graphHeight, 1000, true);

    static {
        ImPlot.createContext();
    }

    @Override
    public void start() {
        fpsGraph.addData("FPS", CitadelConstants.HEX_COLOR_BLUE);

        renderingGraph.addData("Draw calls", CitadelConstants.HEX_COLOR_BLUE);
        renderingGraph.addData("Game objects", CitadelConstants.HEX_COLOR_RED);
        renderingGraph.addData("Dirty objects modified", CitadelConstants.HEX_COLOR_GREEN);
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        ImGui.begin("Statistics");

        Scene scene = SceneManager.getCurrentScene();
        Window window = Window.getInstance();
        Camera camera = scene.getCamera();
        Renderer renderer = scene.getRenderer();
        SoundManager soundManager = SoundManager.getInstance();

        if (ImGui.collapsingHeader("Performance")) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            ImGui.text("FPS: " + Time.getFPS());

            if (ImGui.collapsingHeader("FPS Graph")) {
                ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                fpsGraph.updateGraphData("FPS", Time.getFPS());
                fpsGraph.render();

                ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
            }

            ImGui.separator();

            ImGui.text("DrawCalls: " + renderer.getDrawCalls());
            ImGui.text("DrawCalls skipped: " + renderer.getDrawCallsSkipped());
            ImGui.text("Dirty objects modified: " + renderer.getDirtyModified());
            ImGui.text("Dirty objects skipped: " + renderer.getDirtySkipped());
            ImGui.text("Game objects: " + scene.getGameObjects().size());
            ImGui.text("Static objects: " + scene.getStaticGameObjects().size());
            ImGui.text("Dynamic objects: " + scene.getDynamicGameObjects().size());

            if (ImGui.collapsingHeader("Render Graph")) {
                ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                renderingGraph.updateGraphData("Draw calls", renderer.getDrawCalls());
                renderingGraph.updateGraphData("Game objects", scene.getGameObjects().size());
                renderingGraph.updateGraphData("Dirty objects modified", renderer.getDirtyModified());
                renderingGraph.render();

                ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
            }

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.spacing();

        if (ImGui.collapsingHeader("Window & Camera Info")) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            ImGui.text("AspectRatio: " + window.getAspectRatio());
            ImGui.text("Width: " + window.getWidth());
            ImGui.text("Height: " + window.getHeight());
            ImGui.text("Camera position: (" + camera.transform.getPosition().x + ", " + camera.transform.getPosition().y + ", " +camera.transform.getPosition().z + ")");

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.spacing();

        if (ImGui.collapsingHeader("Sounds")) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            ImGui.text("Sound sources: " + soundManager.getSourcesAmount());

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.end();
    }
}