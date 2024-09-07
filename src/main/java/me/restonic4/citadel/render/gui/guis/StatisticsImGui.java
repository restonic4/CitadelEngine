package me.restonic4.citadel.render.gui.guis;

import imgui.ImGui;
import me.restonic4.citadel.core.Window;
import me.restonic4.citadel.registries.built_in.types.ImGuiScreen;
import me.restonic4.citadel.render.Camera;
import me.restonic4.citadel.render.Renderer;
import me.restonic4.citadel.util.Time;
import me.restonic4.citadel.world.Scene;
import me.restonic4.citadel.world.SceneManager;

public class StatisticsImGui extends ImGuiScreen {
    @Override
    public void render() {
        ImGui.begin("Statistics");

        Scene scene = SceneManager.getCurrentScene();
        Window window = Window.getInstance();
        Camera camera = scene.getCamera();
        Renderer renderer = scene.getRenderer();

        ImGui.text("FPS: " + Time.getFPS());
        ImGui.text("DrawCalls: " + renderer.getDrawCalls());
        ImGui.text("DrawCalls skipped: " + renderer.getDrawCallsSkipped());
        ImGui.text("Dirty objects modified: " + renderer.getDirtyModified());
        ImGui.text("Dirty objects skipped: " + renderer.getDirtySkipped());
        ImGui.text("Game objects: " + scene.getGameObjects().size());
        ImGui.text("Static objects: " + scene.getStaticGameObjects().size());
        ImGui.text("Dynamic objects: " + scene.getDynamicGameObjects().size());
        ImGui.text("AspectRatio: " + window.getAspectRatio());
        ImGui.text("Width: " + window.getWidth());
        ImGui.text("Height: " + window.getHeight());
        ImGui.text("Camera position: (" + camera.transform.getPosition().x + ", " + camera.transform.getPosition().y + ", " +camera.transform.getPosition().z + ")");

        ImGui.end();
    }
}
