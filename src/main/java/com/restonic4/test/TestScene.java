package com.restonic4.test;

import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.files.parsers.mesh.MeshLoader;
import com.restonic4.citadel.input.KeyListener;
import com.restonic4.citadel.input.MouseListener;
import com.restonic4.citadel.registries.built_in.managers.KeyBinds;
import com.restonic4.citadel.registries.built_in.managers.Sounds;
import com.restonic4.citadel.render.cameras.PerspectiveCamera;
import com.restonic4.citadel.sound.SoundManager;
import com.restonic4.citadel.sound.SoundSource;
import com.restonic4.citadel.util.Time;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.world.object.Mesh;
import com.restonic4.citadel.world.object.Transform;
import com.restonic4.citadel.world.object.components.ColliderComponent;
import com.restonic4.citadel.world.object.components.LightComponent;
import com.restonic4.citadel.world.object.components.ModelRendererComponent;
import com.restonic4.citadel.world.object.components.RigidBodyComponent;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class TestScene extends Scene {
    SoundSource music;
    public GameObject sun;
    public List<GameObject> yes = new ArrayList<>();
    public List<GameObject> rot = new ArrayList<>();

    @Override
    public void init() {
        Transform camTransform = new Transform();
        camTransform.setPosition(0, 10, 100);
        camTransform.setScale(1, 1, 1);

        camera = new PerspectiveCamera(camTransform);
        camera.load();

        music = Sounds.CRASH.createSource(true, true);
        music.setPosition(new Vector3f(0, 0, 0));
        //music.play();

        int planeSize = 5;

        GameObject plane = new GameObject("Base plate", false);
        plane.transform.setPosition(0, 0, 0);
        plane.transform.setScale(planeSize, 1, planeSize);
        Mesh planeMesh = MeshLoader.loadMesh("assets/models/persus_cubo.obj");
        planeMesh.setTint(new Vector4f(0, 1, 0, 1));
        plane.addComponent(new ModelRendererComponent(planeMesh));
        this.addGameObject(plane);

        for (int i = 0; i < planeSize; i++) {
            GameObject cube = new GameObject(false);
            cube.setName("debugCascadePoint");
            cube.transform.setParent(plane.transform);

            Mesh cubeMesh = MeshLoader.loadMesh("assets/models/persus_cubo.obj");
            //cubeMesh.setTint(new Vector4f(0, 1, 1, 1));

            cube.addComponent(new ModelRendererComponent(cubeMesh));

            //cube.transform.setPosition(RandomUtil.random(-planeSize, planeSize), 1, RandomUtil.random(-planeSize, planeSize));
            //cube.transform.setScale(1, 2, 1);

            yes.add(cube);
            this.addGameObject(cube);
        }

        Transform last = getTransform();
        for (int i = 0; i < planeSize; i++) {
            GameObject cube = new GameObject(false);
            cube.setName("rot");
            cube.transform.setParent(last);

            last = cube.transform;

            Mesh cubeMesh = MeshLoader.loadMesh("assets/models/persus_cubo.obj");
            //cubeMesh.setTint(new Vector4f(0, 1, 1, 1));

            cube.addComponent(new ModelRendererComponent(cubeMesh));

            //cube.transform.setPosition(RandomUtil.random(-planeSize, planeSize), 1, RandomUtil.random(-planeSize, planeSize));
            //cube.transform.setScale(1, 2, 1);

            rot.add(cube);
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

        super.init();
    }

    @Override
    public void update() {
        float speed = 0.5f;
        float radius = 75;
        float x = (float) Math.sin(Time.getRunningTime() * speed);
        float y = (float) Math.cos(Time.getRunningTime() * speed);
        float offsetYippie = 3;
        for (int i = 0; i < yes.size(); i++) {
            yes.get(i).transform.setPosition(x * i * offsetYippie, y * i * offsetYippie, 0);
            yes.get(i).transform.setRotationEuler(x, y, 0);
        }

        float xr = (float) Math.sin(Time.getRunningTime() * speed * 2);
        float yr = (float) Math.cos(Time.getRunningTime() * speed * 2);
        for (int i = 0; i < rot.size(); i++) {
            rot.get(i).transform.setPosition(i * offsetYippie, i * offsetYippie, 0);
            rot.get(i).transform.setRotationEuler(xr, yr, xr);
        }

        if (KeyBinds.CRASH.isPressed()) {
            Logger.log("Size: " + renderer.getByteSize());
            throw new RuntimeException("Manual crash triggered by using ESC + F9");
        }

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_T)) {
            SoundSource soundSource = Sounds.CRASH.createSource(false, false);
            soundSource.setPosition(new Vector3f(0, 0, 0));
            soundSource.play();
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

        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_X)) {
            Window.getInstance().setCursorLocked(!Window.getInstance().isCursorLocked());
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

        SoundManager.getInstance().updateListenerPosition(camera);

        super.update();
    }
}
