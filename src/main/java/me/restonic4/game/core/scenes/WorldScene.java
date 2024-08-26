package me.restonic4.game.core.scenes;

import me.restonic4.engine.Scene;
import me.restonic4.engine.SceneManager;
import me.restonic4.engine.Window;
import me.restonic4.engine.input.KeyListener;
import me.restonic4.engine.object.GameObject;
import me.restonic4.engine.object.Mesh;
import me.restonic4.engine.object.Transform;
import me.restonic4.engine.object.components.ModelRendererComponent;
import me.restonic4.engine.files.meshes.MeshLoader;
import me.restonic4.engine.render.PerspectiveCamera;
import me.restonic4.engine.util.Time;
import me.restonic4.engine.util.debug.diagnosis.Logger;
import me.restonic4.engine.util.math.RandomUtil;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;


import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;

public class WorldScene extends Scene {
    @Override
    public void init() {
        Logger.log("Starting the world scene");

        Transform camTransform = new Transform();
        camTransform.setPosition(0, 0, 100);
        camTransform.setScale(1, 1, 1);
        camera = new PerspectiveCamera(camTransform);

        Mesh testMesh = MeshLoader.loadMesh("assets/models/test.obj");
        testMesh.setVerticesColors(new Vector4f[] {           // Colors for each vertex
                new Vector4f(1, 0, 0, 1), // Red
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(0, 0, 1, 1), // Blue
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 0, 0, 1), // Red
                new Vector4f(1, 0, 1, 1),  // Magenta
                new Vector4f(1, 0, 0, 1), // Red
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(0, 0, 1, 1), // Blue
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 0, 0, 1), // Red
                new Vector4f(0, 1, 0, 1)  // Green
        });

        Mesh testMesh2 = MeshLoader.loadMesh("assets/models/test2.obj");
        testMesh2.setVerticesColors(new Vector4f[] {           // Colors for each vertex
                new Vector4f(1, 0, 0, 1), // Red
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(0, 0, 1, 1), // Blue
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 0, 0, 1), // Red
                new Vector4f(1, 0, 1, 1),  // Magenta
                new Vector4f(1, 0, 0, 1), // Red
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(0, 0, 1, 1), // Blue
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 0, 0, 1), // Red
                new Vector4f(0, 1, 0, 1),  // Green
                new Vector4f(1, 0, 0, 1), // Red
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(0, 0, 1, 1), // Blue
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 0, 0, 1), // Red
                new Vector4f(1, 0, 1, 1),  // Magenta
        });

        Mesh[] list = new Mesh[]{testMesh, testMesh2};

        int amount = 16;

        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                for (int w = 0; w < amount; w++) {
                    Mesh selected = RandomUtil.getRandom(list);

                    if (i == 0 && j == 0 && w == 0) {
                        selected = testMesh;
                    }

                    if (i == amount - 1 && j == amount - 1 && w == amount - 1) {
                        selected = testMesh2;
                    }

                    GameObject test = new GameObject(false);
                    test.addComponent(new ModelRendererComponent(selected));
                    test.setName("test:"+i+":"+j+":"+w);
                    test.transform.setPosition(i*5, j*5, w*5);
                    test.transform.setScale(1,1,1);

                    this.addGameObject(test);
                }
            }
        }
    }

    @Override
    public void update() {
        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_C)) {
            throw new RuntimeException("lol");
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_U)) {
            SceneManager.loadScene(new WorldScene());
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)) {
            camera.transform.addLocalPositionX((float) (-100 * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
            camera.transform.addLocalPositionX((float) (100 * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)) {
            camera.transform.addLocalPositionZ((float) (-100 * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
            camera.transform.addLocalPositionZ((float) (100 * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_Q)) {
            camera.transform.addLocalPositionY((float) (-100 * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_E)) {
            camera.transform.addLocalPositionY((float) (100 * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            camera.transform.addLocalRotationEuler((float) (2 * Time.getDeltaTime()), 0, 0);
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            camera.transform.addLocalRotationEuler((float) (-2 * Time.getDeltaTime()), 0, 0);
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            camera.transform.addLocalRotationEuler(0, (float) (-2 * Time.getDeltaTime()), 0);
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            camera.transform.addLocalRotationEuler(0, (float) (2 * Time.getDeltaTime()), 0);
        }

        glfwSetWindowTitle(Window.getInstance().getGlfwWindowAddress(),
                "FPS: " + Time.getFPS()
                + ", DrawCalls: " + this.renderer.getDrawCalls()
                + ", Dirty objects: " + this.renderer.getDirtyModified()
                + ", Game objects: " + this.getGameObjects().size()
                + ", Static objects: " + this.getStaticGameObjects().size()
                + ", Dynamic objects: " + this.getDynamicGameObjects().size()
                + ", AspectRatio: " + Window.getInstance().getAspectRatio()
                + ", w: " + Window.getInstance().getWidth()
                + ", h: " + Window.getInstance().getHeight()
                + ", Pos: (" + camera.transform.getPosition().x + ", " + camera.transform.getPosition().y + ", " + camera.transform.getPosition().z + ")"
        );

        super.update();
    }
}
