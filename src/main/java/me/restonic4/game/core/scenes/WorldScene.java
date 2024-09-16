package me.restonic4.game.core.scenes;

import me.restonic4.citadel.input.MouseListener;
import me.restonic4.citadel.networking.PacketData;
import me.restonic4.citadel.networking.PacketType;
import me.restonic4.citadel.registries.built_in.managers.FrameBuffers;
import me.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import me.restonic4.citadel.registries.built_in.managers.KeyBinds;
import me.restonic4.citadel.registries.built_in.managers.Packets;
import me.restonic4.citadel.render.Texture;
import me.restonic4.citadel.render.cameras.OrthographicCamera;
import me.restonic4.citadel.sound.SoundManager;
import me.restonic4.citadel.sound.SoundSource;
import me.restonic4.citadel.util.debug.DebugManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.world.Scene;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.citadel.core.Window;
import me.restonic4.citadel.input.KeyListener;
import me.restonic4.citadel.world.object.GameObject;
import me.restonic4.citadel.world.object.Mesh;
import me.restonic4.citadel.world.object.Transform;
import me.restonic4.citadel.world.object.components.LightComponent;
import me.restonic4.citadel.world.object.components.ModelRendererComponent;
import me.restonic4.citadel.files.parsers.mesh.MeshLoader;
import me.restonic4.citadel.render.cameras.PerspectiveCamera;
import me.restonic4.citadel.util.Time;
import me.restonic4.citadel.util.math.RandomUtil;
import me.restonic4.game.core.world.sounds.Sounds;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;


import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;

public class WorldScene extends Scene {
    SoundSource music;

    List<GameObject> debugCascadePoints = new ArrayList<>();
    List<GameObject> tornado = new ArrayList<>();
    public GameObject test2;
    public OrthographicCamera shadowMapCamera;
    public GameObject sun;

    @Override
    public void init() {
        Transform camTransform = new Transform();
        camTransform.setPosition(0, 0, 100);
        camTransform.setScale(1, 1, 1);
        camera = new PerspectiveCamera(camTransform);
        camera.load();

        Transform shadowMapCameraTransform = new Transform();
        shadowMapCameraTransform.setPosition(0, 0, 0);
        //Quaternionf quaternion = new Quaternionf().rotateX((float) Math.PI);
        Quaternionf quaternionf = new Quaternionf().lookAlong(new Vector3f(0, -1, 0), new Vector3f(1, 0, 0));
        shadowMapCameraTransform.setRotation(quaternionf);
        shadowMapCamera = new OrthographicCamera(shadowMapCameraTransform);

        // Music
        music = Sounds.TEMPLATE.createSource(true, true);
        music.setPosition(new Vector3f(0, 0, 0));
        //music.play();

        Mesh testMesh = MeshLoader.loadMesh("assets/models/test.obj");
        testMesh.setTexture(new Texture("assets/textures/test.png"));

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

        int amount = 20;

        for (int i = -(amount / 2); i < amount / 2; i++) {
            for (int j = -(amount / 2); j < amount / 2; j++) {
                for (int w = -(amount / 2); w < amount / 2; w++) {
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

                    tornado.add(test);

                    this.addGameObject(test);
                }
            }
        }

        Mesh citadelMesh = MeshLoader.loadMesh("assets/models/persus_cubo.obj");
        citadelMesh.setTexture(new Texture("assets/textures/persus.png"));

        GameObject test = new GameObject(false);
        LightComponent lightComponent = new LightComponent(LightComponent.LightType.POINT);
        lightComponent.getLightType().adjustAttenuationByRange(10);
        test.addComponent(lightComponent);
        test.addComponent(new ModelRendererComponent(citadelMesh));
        test.setName("test");
        test.transform.setPosition(0, -100, 10);
        test.transform.setScale(0.1f,0.1f,0.1f);
        this.addGameObject(test);

        test2 = test;

        GameObject fboView = new GameObject(false);
        fboView.transform.setPosition(0, -100, 0);
        fboView.transform.setScale(10, 10, 1);
        Mesh fboMesh = MeshLoader.loadMesh("assets/models/persus_cubo.obj");
        //fboMesh.setTexture(new Texture(FrameBuffers.SHADOWS.getTextureHandlerId())); // error
        //fboMesh.setTexture(new Texture(teest.getBindlessHandle()));
        fboView.addComponent(new ModelRendererComponent(fboMesh));
        this.addGameObject(fboView);

        int planeSize = 50;

        GameObject plane = new GameObject(false);
        plane.transform.setPosition(0, 0, 0);
        plane.transform.setScale(planeSize, 1, planeSize);
        Mesh planeMesh = MeshLoader.loadMesh("assets/models/persus_cubo.obj");
        planeMesh.setTint(new Vector4f(0, 1, 0, 1));
        plane.addComponent(new ModelRendererComponent(planeMesh));
        this.addGameObject(plane);

        for (int i = 0; i < planeSize * 2; i++) {
            GameObject cube = new GameObject(false);
            cube.transform.setPosition(RandomUtil.random(-planeSize, planeSize), 1, RandomUtil.random(-planeSize, planeSize));
            cube.transform.setScale(1, 2, 1);
            Mesh cubeMesh = MeshLoader.loadMesh("assets/models/persus_cubo.obj");
            cubeMesh.setTint(new Vector4f(0, 1, 1, 1));
            cube.addComponent(new ModelRendererComponent(cubeMesh));
            this.addGameObject(cube);
        }

        GameObject light = new GameObject(false);
        //light.transform.setPosition(0, 100, 0);
        light.transform.setPosition(0, 1, 1);
        light.addComponent(new ModelRendererComponent(planeMesh));
        LightComponent lightComponent1 = new LightComponent(LightComponent.LightType.DIRECTIONAL);
        //lightComponent1.getLightType().adjustAttenuationByRange(50);
        light.addComponent(lightComponent1);
        this.addGameObject(light);

        sun = light;

        //DebugManager.setVerticesMode(true);
        //DebugManager.setWireFrameMode(true);

        for (int i = 0; i < 8; i++) {
            GameObject debugCascadePoint = new GameObject(false);
            debugCascadePoint.setName("debugCascadePoint");

            Mesh debugMesh = MeshLoader.loadMesh("assets/models/persus_cubo.obj");

            debugCascadePoint.addComponent(new ModelRendererComponent(debugMesh));

            debugCascadePoint.transform.setPosition(-100, i * 2, -100);

            this.addGameObject(debugCascadePoint);
            debugCascadePoints.add(debugCascadePoint);
        }

        super.init();
    }

    @Override
    public void update() {
        float speed = 0.5f;
        float radius = 75;
        float x = (float) Math.sin(Time.getRunningTime() * speed);
        float y = (float) Math.cos(Time.getRunningTime() * speed);
        //test2.transform.setPosition(x * radius, y * radius, 0);

        /*
        up.Normalize();
		var v =  dir + up * -Vector3.Dot(up, dir);
		var q = Quaternion.FromToRotation(Vector3.forward, v);
		return Quaternion.FromToRotation(v, dir) * q;
         */
        float factor = (float) Time.getRunningTime();
        sun.transform.setPosition(0, Math.cos(factor), Math.sin(factor));

        Vector3f[] vertices = DebugManager.getCascadeShadowFrustumVertices();
        if (vertices != null && vertices.length > 0) {
            for (int i = 0; i < debugCascadePoints.size(); i++) {
                GameObject gameObject = debugCascadePoints.get(i);
                gameObject.transform.setPosition(vertices[i].x, vertices[i].y, vertices[i].z);

                float distance = getCamera().transform.getPosition().distance(gameObject.transform.getPosition());
                float smoothed = Math.min(Math.max(distance, 0.5f), 10);

                gameObject.transform.setScale(smoothed, smoothed, smoothed);
            }
        }

        if (KeyBinds.CRASH.isPressed()) {
            Logger.log("Size: " + renderer.getByteSize());
            throw new RuntimeException("Manual crash triggered by using ESC + F9");
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_F)) {
            if (camera.isSimulated()) {
                camera.setSimulated(false);
            }
            else {
                camera.setSimulated(true);
            }
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_T)) {
            SoundSource soundSource = Sounds.GLASS.createSource(false, false);
            soundSource.setPosition(new Vector3f(0, 0, 0));
            soundSource.play();
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_K)) {
            Packets.DEFAULT.send(PacketType.CLIENT_TO_SERVER,
                    new PacketData("a", "b", "c", Time.getRunningTime(), new Vector3f(10, 11, 12))
            );
        }


        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_P)) {
            music.setPitch(RandomUtil.randomTiny() + RandomUtil.randomTiny());
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_U)) {
            SceneManager.loadScene(new WorldScene());
        }

        float velocity = 50;

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            velocity = 200;
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)) {
            camera.transform.addLocalPositionX((float) (-velocity * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
            camera.transform.addLocalPositionX((float) (velocity * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)) {
            camera.transform.addLocalPositionZ((float) (-velocity * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
            camera.transform.addLocalPositionZ((float) (velocity * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_Q)) {
            camera.transform.addLocalPositionY((float) (-velocity * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_E)) {
            camera.transform.addLocalPositionY((float) (velocity * Time.getDeltaTime()));
        }

        /*if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            test2.transform.addPositionZ((float) (10 * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            test2.transform.addPositionZ((float) (-10 * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            test2.transform.addPositionX((float) (10 * Time.getDeltaTime()));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            test2.transform.addPositionX((float) (-10 * Time.getDeltaTime()));
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_R)) {
            test2.transform.setPosition(RandomUtil.random(-20, 20), RandomUtil.random(-20, 20), RandomUtil.random(-20, 20));
        }*/

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_X)) {
            Window.getInstance().setCursorLocked(!Window.getInstance().isCursorLocked());
        }

        if (KeyBinds.TOGGLE_STATISTICS_GUI.isPressedOnce()) {
            ImGuiScreens.RENDER_STATISTICS.toggle();
        }

        if (KeyBinds.TOGGLE_DEV_GUI.isPressedOnce()) {
            ImGuiScreens.CAMERA_SETTINGS.toggle();
        }

        if (Window.getInstance().isCursorLocked()) {
            float sensitivity = 0.005f;

            float xMouseDelta = MouseListener.getDy() * sensitivity;
            float yMouseDelta = MouseListener.getDx() * sensitivity;

            Quaternionf pitchRotation = new Quaternionf().rotateX(xMouseDelta);
            Quaternionf yawRotation = new Quaternionf().rotateY(yMouseDelta);

            camera.transform.addRotationQuaternion(yawRotation);
            camera.transform.addRotationQuaternion(pitchRotation);
        }

        float mult = 0.2F;
        for (int i = 0; i < tornado.size(); i++) {
            if (tornado.get(i) == test2) {
                continue;
            }

            float multR = mult * i;

            float offset = (float) Math.sin(Time.getRunningTime() + multR);
            float offset2 = (float) Math.cos(Time.getRunningTime() + multR);

            GameObject gameObject = tornado.get(i);
            gameObject.transform.setPosition(multR * offset, multR + offset, multR * offset2);
        }

        //camera.transform.addLocalRotationEuler(xMouseDelta, yMouseDelta, 0);

        SoundManager.getInstance().updateListenerPosition(camera);

        super.update();
    }
}
