package me.restonic4.game.core.scenes;

import me.restonic4.citadel.localization.Localizer;
import me.restonic4.citadel.sound.SoundManager;
import me.restonic4.citadel.sound.SoundSource;
import me.restonic4.citadel.util.debug.DebugManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.util.debug.diagnosis.ProfilerManager;
import me.restonic4.citadel.world.Scene;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.citadel.core.Window;
import me.restonic4.citadel.input.KeyListener;
import me.restonic4.citadel.world.object.GameObject;
import me.restonic4.citadel.world.object.Mesh;
import me.restonic4.citadel.world.object.Transform;
import me.restonic4.citadel.world.object.components.ModelRendererComponent;
import me.restonic4.citadel.files.MeshLoader;
import me.restonic4.citadel.render.PerspectiveCamera;
import me.restonic4.citadel.util.Time;
import me.restonic4.citadel.util.math.RandomUtil;
import me.restonic4.game.core.world.sounds.Sounds;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;


import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;

public class WorldScene extends Scene {
    SoundSource music;

    List<GameObject> moving = new ArrayList<>();
    GameObject test2;

    @Override
    public void init() {
        Transform camTransform = new Transform();
        camTransform.setPosition(0, 0, 100);
        camTransform.setScale(1, 1, 1);
        camera = new PerspectiveCamera(camTransform);

        // Music
        music = Sounds.TEMPLATE.createSource(true, true);
        music.setPosition(new Vector3f(0, 0, 0));
        //music.play();

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

        Mesh cameraMesh = MeshLoader.loadMesh("assets/models/camera.fbx");
        /*cameraMesh.setVerticesColors(new Vector4f[] {           // Colors for each vertex
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
        });*/

        Mesh[] list = new Mesh[]{testMesh, testMesh2};

        //int amount = 16;
        int amount = 16;

        /*test2 = new GameObject(false);
        test2.addComponent(new ModelRendererComponent(cameraMesh));
        test2.setName("test:");
        test2.transform.setPosition(-15, -15, -15);
        test2.transform.setScale(10,10,10);
        this.addGameObject(test2);*/

        /*GameObject testDebug = new GameObject(false);
        testDebug.addComponent(new ModelRendererComponent(testMesh));
        testDebug.setName("debug");
        testDebug.transform.setPosition(-15, -15, -15);
        testDebug.transform.setScale(10,10,10);
        this.addGameObject(testDebug);*/

        /*for (int i = 0; i < amount; i++) {
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

                    moving.add(test);

                    this.addGameObject(test);
                }
            }
        }*/

        Mesh citadelMesh = MeshLoader.loadMesh("assets/models/citadel.obj");
        citadelMesh.setVerticesColors(new Vector4f[] {           // Colors for each vertex
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 1, 1, 1), // White
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 1, 1, 1), // White
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 1, 1, 1), // White
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 1, 1, 1), // White
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 1, 1, 1), // White
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 1, 1, 1), // White
                new Vector4f(0, 1, 0, 1), // Green
                new Vector4f(1, 1, 0, 1), // Yellow
                new Vector4f(1, 0, 1, 1), // Magenta
                new Vector4f(0, 1, 1, 1), // Cyan
                new Vector4f(1, 1, 1, 1), // White
                new Vector4f(0, 1, 0, 1), // Green

        });

        GameObject test = new GameObject(false);
        test.addComponent(new ModelRendererComponent(citadelMesh));
        test.setName("test");
        test.transform.setPosition(0, 0, 0);
        test.transform.setScale(1,1,1);
        this.addGameObject(test);

        /*for (int i = 0; i < 4; i++) {
            GameObject test = new GameObject(false);
            test.addComponent(new ModelRendererComponent((RandomUtil.random(0,1) == 0) ? testMesh : testMesh2));
            test.setName("test");
            test.transform.setPosition(5*i, 5, 5);
            test.transform.setScale(1,1,1);
            this.addGameObject(test);
        }*/

        /*GameObject test = new GameObject(false);
        test.addComponent(new ModelRendererComponent(testMesh2));
        test.setName("test");
        test.transform.setPosition(5, 5, 5);
        test.transform.setScale(1,1,1);
        this.addGameObject(test);

        GameObject test2 = new GameObject(false);
        test2.addComponent(new ModelRendererComponent(testMesh));
        test2.setName("test");
        test2.transform.setPosition(10, 5, 5);
        test2.transform.setScale(1,1,1);
        this.addGameObject(test2);

        GameObject test3 = new GameObject(false);
        test3.addComponent(new ModelRendererComponent(testMesh));
        test3.setName("test");
        test3.transform.setPosition(10, 5, 10);
        test3.transform.setScale(1,1,1);
        this.addGameObject(test3);

        GameObject test4 = new GameObject(false);
        test4.addComponent(new ModelRendererComponent(testMesh));
        test4.setName("test");
        test4.transform.setPosition(5, 5, 10);
        test4.transform.setScale(1,1,1);
        this.addGameObject(test4);

        GameObject test5 = new GameObject(false);
        test5.addComponent(new ModelRendererComponent(testMesh));
        test5.setName("test");
        test5.transform.setPosition(5, 10, 5);
        test5.transform.setScale(1,1,1);
        this.addGameObject(test5);

        GameObject test6 = new GameObject(false);
        test6.addComponent(new ModelRendererComponent(testMesh));
        test6.setName("test");
        test6.transform.setPosition(10, 10, 5);
        test6.transform.setScale(1,1,1);
        this.addGameObject(test6);

        GameObject test7 = new GameObject(false);
        test7.addComponent(new ModelRendererComponent(testMesh));
        test7.setName("test");
        test7.transform.setPosition(10, 10, 10);
        test7.transform.setScale(1,1,1);
        this.addGameObject(test7);

        GameObject test8 = new GameObject(false);
        test8.addComponent(new ModelRendererComponent(testMesh2));
        test8.setName("test");
        test8.transform.setPosition(5, 10, 10);
        test8.transform.setScale(1,1,1);
        this.addGameObject(test8);*/

        super.init();
    }

    @Override
    public void update() {
        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_C)) {
            throw new RuntimeException("lol");
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_T)) {
            SoundSource soundSource = Sounds.GLASS.createSource(false, false);
            soundSource.setPosition(new Vector3f(0, 0, 0));
            soundSource.play();
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_K)) {
            music.stop();
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_F6)) {
            ProfilerManager.export();
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_P)) {
            music.setPitch(RandomUtil.randomTiny() + RandomUtil.randomTiny());
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_U)) {
            SceneManager.loadScene(new WorldScene());
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)) {
            camera.transform.addLocalPositionX((float) (-10 * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
            camera.transform.addLocalPositionX((float) (10 * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)) {
            camera.transform.addLocalPositionZ((float) (-10 * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
            camera.transform.addLocalPositionZ((float) (10 * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_Q)) {
            camera.transform.addLocalPositionY((float) (-10 * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_E)) {
            camera.transform.addLocalPositionY((float) (10 * Time.getDeltaTime()));
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

        /*glfwSetWindowTitle(Window.getInstance().getGlfwWindowAddress(),
                "FPS: " + Time.getFPS()
                + ", DrawCalls: " + this.renderer.getDrawCalls()
                + ", Dirty objects modified: " + this.renderer.getDirtyModified()
                + ", Dirty objects skipped: " + this.renderer.getDirtySkipped()
                + ", Game objects: " + this.getGameObjects().size()
                + ", Static objects: " + this.getStaticGameObjects().size()
                + ", Dynamic objects: " + this.getDynamicGameObjects().size()
                + ", AspectRatio: " + Window.getInstance().getAspectRatio()
                + ", w: " + Window.getInstance().getWidth()
                + ", h: " + Window.getInstance().getHeight()
                + ", Pos: (" + camera.transform.getPosition().x + ", " + camera.transform.getPosition().y + ", " + camera.transform.getPosition().z + ")"
                + ", Scene changed: " + SceneManager.changes
        );*/

        SoundManager.getInstance().updateListenerPosition(camera);

        super.update();
    }
}
