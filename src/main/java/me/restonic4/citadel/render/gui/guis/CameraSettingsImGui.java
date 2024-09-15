package me.restonic4.citadel.render.gui.guis;

import imgui.ImGui;
import me.restonic4.ClientSide;
import me.restonic4.citadel.util.CitadelConstants;
import me.restonic4.citadel.world.Scene;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.citadel.world.object.components.LightComponent;
import org.joml.Vector3f;

@ClientSide
public class CameraSettingsImGui extends ToggleableImGuiScreen {
    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        ImGui.begin("Camera Settings");

        float[] fov = { CitadelConstants.CAMERA_FOV };
        float[] nearClip = { CitadelConstants.CAMERA_NEAR_PLANE };
        float[] farClip = { CitadelConstants.CAMERA_FAR_PLANE };

        if (ImGui.sliderFloat("FOV", fov, 20, 120.0f, "%.1f")) {
            CitadelConstants.CAMERA_FOV = (float) Math.toRadians(fov[0]);
        }

        if (ImGui.sliderFloat("Near Clip", nearClip, 0.1f, 1.0f, "%.2f")) {
            CitadelConstants.CAMERA_NEAR_PLANE = nearClip[0];
        }

        if (ImGui.sliderFloat("Far Clip", farClip, 100.0f, 10000f, "%.1f")) {
            CitadelConstants.CAMERA_FAR_PLANE = farClip[0];
        }

        if (ImGui.button("Reset")) {
            CitadelConstants.CAMERA_FOV = (float) Math.toRadians(60);
            CitadelConstants.CAMERA_NEAR_PLANE = 0.1f;
            CitadelConstants.CAMERA_FAR_PLANE = 10000f;
        }

        ImGui.end();
    }
}
