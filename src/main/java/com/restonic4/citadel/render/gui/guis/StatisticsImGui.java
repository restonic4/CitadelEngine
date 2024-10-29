package com.restonic4.citadel.render.gui.guis;

import com.restonic4.citadel.registries.RegistryObject;
import com.restonic4.citadel.registries.built_in.types.KeyBind;
import com.restonic4.citadel.registries.built_in.types.Locale;
import com.restonic4.citadel.render.Shader;
import com.restonic4.citadel.util.StringHelper;
import com.restonic4.citadel.world.object.components.CameraComponent;
import imgui.ImGui;
import com.restonic4.ClientSide;
import imgui.extension.implot.ImPlot;
import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.RegistryKey;
import com.restonic4.citadel.render.Renderer;
import com.restonic4.citadel.render.gui.LineGraphImGui;
import com.restonic4.citadel.sound.SoundManager;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.Time;
import com.restonic4.citadel.util.math.UnitConverter;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.SceneManager;

import java.util.Map;

@ClientSide
public class StatisticsImGui extends ToggleableImGuiScreen {
    int graphWidth = 500;
    int graphHeight = 200;

    LineGraphImGui fpsGraph = new LineGraphImGui("FPS", 1000, graphWidth, graphHeight, 1000, true);
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
        CameraComponent camera = scene.getMainCamera();
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
            ImGui.text(StringBuilderHelper.concatenate("Camera position: (", camera.gameObject.transform.getPosition().x, ", ", camera.gameObject.transform.getPosition().y, ", ", camera.gameObject.transform.getPosition().z, ")"));

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.spacing();

        if (ImGui.collapsingHeader("Sounds")) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            ImGui.text(StringBuilderHelper.concatenate("Sound sources: ", soundManager.getSourcesAmount()));

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.spacing();

        if (ImGui.collapsingHeader("Registries")) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            Map<RegistryKey<?>, Map<AssetLocation, ?>> registries = Registry.getRegistries();
            for (Map.Entry<RegistryKey<?>, Map<AssetLocation, ?>> data : registries.entrySet()) {
                Map<AssetLocation, ?> map = data.getValue();

                if (ImGui.collapsingHeader(data.getKey().getKey())) {
                    ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                    for (Map.Entry<AssetLocation, ?> registryData : map.entrySet()) {
                        AssetLocation assetLocation = registryData.getKey();
                        RegistryObject registryObject = (RegistryObject) registryData.getValue();

                        if (registryObject instanceof KeyBind keyBind) {
                            if (ImGui.collapsingHeader(assetLocation.toString())) {
                                ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                                ImGui.text(keyBind.getKeyString());

                                ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
                            }
                        } else if (registryObject instanceof Shader shader) {
                            if (ImGui.collapsingHeader(assetLocation.toString())) {
                                ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                                if (ImGui.collapsingHeader(StringBuilderHelper.concatenate("Vertex shader source ##", assetLocation.toString()))) {
                                    ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                                    ImGui.text(shader.getVertexShaderSource());

                                    ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
                                }

                                if (ImGui.collapsingHeader(StringBuilderHelper.concatenate("Fragment shader source ##", assetLocation.toString()))) {
                                    ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                                    ImGui.text(shader.getFragmentShaderSource());

                                    ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
                                }

                                ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
                            }
                        } else if (registryObject instanceof Locale locale) {
                            if (ImGui.collapsingHeader(assetLocation.toString())) {
                                ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                                ImGui.text(StringHelper.formatJson(locale.getData().toString()));

                                ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
                            }
                        } else {
                            ImGui.text(assetLocation.toString());
                        }
                    }

                    ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
                }

            }

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.end();
    }
}