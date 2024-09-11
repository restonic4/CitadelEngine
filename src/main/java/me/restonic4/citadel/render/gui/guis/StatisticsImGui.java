package me.restonic4.citadel.render.gui.guis;

import imgui.ImGui;
import imgui.extension.implot.ImPlot;
import me.restonic4.citadel.core.Window;
import me.restonic4.citadel.render.Camera;
import me.restonic4.citadel.render.Renderer;
import me.restonic4.citadel.render.gui.LineGraphImGui;
import me.restonic4.citadel.sound.SoundManager;
import me.restonic4.citadel.util.CitadelConstants;
import me.restonic4.citadel.util.StringBuilderHelper;
import me.restonic4.citadel.util.Time;
import me.restonic4.citadel.util.math.UnitConverter;
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

            ImGui.text(StringBuilderHelper.concatenate("FPS: ", Time.getFPS()));


            if (ImGui.collapsingHeader("FPS Graph")) {
                ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                fpsGraph.updateGraphData("FPS", Time.getFPS());
                fpsGraph.render();

                ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
            }

            ImGui.separator();

            ImGui.text(StringBuilderHelper.concatenate("DrawCalls: ", renderer.getDrawCalls()));
            ImGui.text(StringBuilderHelper.concatenate("DrawCalls skipped: ", renderer.getDrawCallsSkipped()));
            ImGui.text(StringBuilderHelper.concatenate("Dirty objects modified: ", renderer.getDirtyModified()));
            ImGui.text(StringBuilderHelper.concatenate("Dirty objects skipped: ", renderer.getDirtySkipped()));
            ImGui.text(StringBuilderHelper.concatenate("Game objects: ", scene.getGameObjects().size()));
            ImGui.text(StringBuilderHelper.concatenate("Static objects: ", scene.getStaticGameObjects().size()));
            ImGui.text(StringBuilderHelper.concatenate("Dynamic objects: " , scene.getDynamicGameObjects().size()));
            ImGui.text(StringBuilderHelper.concatenate("Lights: " , scene.getLightsAmount()));

            if (ImGui.collapsingHeader("Render Graph")) {
                ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                renderingGraph.updateGraphData("Draw calls", renderer.getDrawCalls());
                renderingGraph.updateGraphData("Game objects", scene.getGameObjects().size());
                renderingGraph.updateGraphData("Dirty objects modified", renderer.getDirtyModified());
                renderingGraph.render();

                ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
            }

            ImGui.separator();

            ImGui.text(StringBuilderHelper.concatenate("Vertices: ", renderer.getVerticesAmount()));
            ImGui.text(StringBuilderHelper.concatenate("Vertices bytes: ", renderer.getByteSize(), " -> ", UnitConverter.bytesToKilobytes(renderer.getByteSize()), "KB -> ", UnitConverter.bytesToMegabytes(renderer.getByteSize()), "MB"));

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.spacing();

        if (ImGui.collapsingHeader("Window & Camera Info")) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            ImGui.text(StringBuilderHelper.concatenate("AspectRatio: ", window.getAspectRatio()));
            ImGui.text(StringBuilderHelper.concatenate("Width: ", window.getWidth()));
            ImGui.text(StringBuilderHelper.concatenate("Height: ", window.getHeight()));
            ImGui.text(StringBuilderHelper.concatenate("Camera position: (", camera.transform.getPosition().x, ", ", camera.transform.getPosition().y, ", ", camera.transform.getPosition().z, ")"));

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.spacing();

        if (ImGui.collapsingHeader("Sounds")) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            ImGui.text(StringBuilderHelper.concatenate("Sound sources: ", soundManager.getSourcesAmount()));

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.end();
    }
}