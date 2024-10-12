package com.restonic4.citadel.core.editor;

import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.input.KeyListener;
import com.restonic4.citadel.input.MouseListener;
import com.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import com.restonic4.citadel.util.Time;
import com.restonic4.citadel.world.object.Transform;
import com.restonic4.citadel.world.object.components.CameraComponent;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

public class HardcodedCamera extends CameraComponent {
    private double originalX = -1;
    private double originalY = -1;

    private boolean beingControlled = false;

    public HardcodedCamera(Transform transform) {
        super(CameraType.PERSPECTIVE, transform);

        adjustProjection();
    }

    @Override
    public void update() {
        float velocity = 50;

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            velocity = 200;
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)) {
            transform.addLocalPositionX((float) (-velocity * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
           transform.addLocalPositionX((float) (velocity * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)) {
            transform.addLocalPositionZ((float) (-velocity * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
            transform.addLocalPositionZ((float) (velocity * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_Q)) {
            transform.addLocalPositionY((float) (-velocity * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_E)) {
            transform.addLocalPositionY((float) (velocity * Time.getDeltaTime()));
        }

        if (ImGuiScreens.SCENE_VIEWPORT.isRightClickDetected()) {
            float sensitivity = 0.005f;

            /*if () {

            }*/

            float xMouseDelta = MouseListener.getDy() * sensitivity;
            float yMouseDelta = MouseListener.getDx() * sensitivity;

            Quaternionf pitchRotation = new Quaternionf().rotateX(xMouseDelta);
            Quaternionf yawRotation = new Quaternionf().rotateY(yMouseDelta);

           transform.addRotationQuaternion(yawRotation);
           transform.addRotationQuaternion(pitchRotation);

           if (originalX == -1 && originalY == -1) {
               originalX = MouseListener.getX();
               originalY = MouseListener.getY();
               beingControlled = true;
           }

           Window.getInstance().setCursorPosition(originalX, originalY);
        }
        else {
            originalX = -1;
            originalY = -1;
            beingControlled = false;
        }
    }

    public boolean isBeingControlled() {
        return this.beingControlled;
    }
}
