package me.restonic4.game.core.scenes;

import me.restonic4.engine.Scene;
import me.restonic4.engine.Window;
import me.restonic4.engine.input.KeyListener;
import me.restonic4.engine.object.GameObject;
import me.restonic4.engine.object.Transform;
import me.restonic4.engine.object.components.DebugComponent;
import me.restonic4.engine.object.components.ModelRendererComponent;
import me.restonic4.engine.render.Camera;
import me.restonic4.engine.render.OrthographicCamera;
import me.restonic4.engine.render.PerspectiveCamera;
import me.restonic4.engine.render.Shader;
import me.restonic4.engine.util.Time;
import me.restonic4.engine.util.debug.Logger;
import me.restonic4.shared.SharedMathConstants;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class WorldScene extends Scene {
    @Override
    public void init() {
        Logger.log("Starting the world scene");

        camera = new PerspectiveCamera(new Vector3f(-250, 0, 20), new Vector3f(0, 0, 0));

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;
        float padding = 3;

        for (int x=0; x < 100; x++) {
            for (int y=0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX) + (padding * x);
                float yPos = yOffset + (y * sizeY) + (padding * y);

                GameObject go = new GameObject("Obj" + x + "" + y, new Transform(new Vector3f(xPos, yPos, 0), new Vector3f(sizeX, sizeY, sizeX)));
                go.addComponent(new ModelRendererComponent(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObject(go);
            }
        }
    }

    @Override
    public void update() {
        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_C)) {
            throw new RuntimeException("lol");
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)) {
            camera.position.x += 100 * Time.getDeltaTime();
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
            camera.position.x -= 100 * Time.getDeltaTime();
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)) {
            camera.position.y -= 100 * Time.getDeltaTime();
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
            camera.position.y += 100 * Time.getDeltaTime();
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            camera.addRotation((float) (100 * Time.getDeltaTime()), 0, 0);
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            camera.addRotation((float) (-100 * Time.getDeltaTime()), 0, 0);
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            camera.addRotation(0, (float) (-100 * Time.getDeltaTime()), 0);
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            camera.addRotation(0, (float) (100 * Time.getDeltaTime()), 0);
        }

        glfwSetWindowTitle(Window.getInstance().getGlfwWindowAddress(),
                "FPS: " + Time.getFPS()
                + ", DrawCalls: " + this.renderer.getDrawCalls()
                + ", AspectRatio: " + Window.getInstance().getAspectRatio()
                + ", Pos: (" + camera.getPosition().x + ", " + camera.getPosition().y + ", " + camera.getPosition().z + ")"
                + ", Rot: (" + camera.getRotation().x + ", " + camera.getRotation().y + ")"
        );

        for (GameObject gameObjects : this.gameObjects) {
            gameObjects.update();
        }

        this.renderer.render();
    }
}
