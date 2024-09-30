package com.restonic4.test;

import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.files.parsers.mesh.MeshLoader;
import com.restonic4.citadel.input.KeyListener;
import com.restonic4.citadel.input.MouseListener;
import com.restonic4.citadel.render.cameras.PerspectiveCamera;
import com.restonic4.citadel.sound.SoundManager;
import com.restonic4.citadel.sound.SoundSource;
import com.restonic4.citadel.util.Time;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.world.object.Mesh;
import com.restonic4.citadel.world.object.Transform;
import com.restonic4.citadel.world.object.components.ColliderComponent;
import com.restonic4.citadel.world.object.components.LightComponent;
import com.restonic4.citadel.world.object.components.ModelRendererComponent;
import com.restonic4.citadel.world.object.components.RigidBodyComponent;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class TestScene extends Scene {
    public Mesh cubeMesh;
    public GameObject player;

    @Override
    public void init() {
        Transform camTransform = new Transform();
        camTransform.setPosition(0, 0, 100);
        camTransform.setScale(1, 1, 1);

        camera = new PerspectiveCamera(camTransform);
        camera.load();

        cubeMesh = MeshLoader.loadMesh("assets/models/cube.obj");

        float vel = 0.5f;

        player = new GameObject(false);
        player.transform.setPosition(0, 3, 0);
        player.addComponent(new ModelRendererComponent(cubeMesh));
        player.addComponent(new ColliderComponent(ColliderComponent.ColliderType.COMPLEX_BOX));
        RigidBodyComponent rigidBodyComponentA = new RigidBodyComponent(1);
        //rigidBodyComponentA.applyForce(new Vector3f(vel, 0, 0));
        player.addComponent(rigidBodyComponentA);
        player.transform.setScale(1, 1, 1);
        this.addGameObject(player);

        GameObject floor = new GameObject(false);
        floor.transform.setPosition(0, 0, 0);
        floor.transform.setScale(10, 1, 10);
        floor.addComponent(new ModelRendererComponent(cubeMesh));
        floor.addComponent(new ColliderComponent(ColliderComponent.ColliderType.COMPLEX_BOX));
        this.addGameObject(floor);

        GameObject wall1 = new GameObject(false);
        wall1.transform.setPosition(10, 5, 0);
        wall1.transform.setScale(1, 10, 10);
        wall1.addComponent(new ModelRendererComponent(cubeMesh));
        wall1.addComponent(new ColliderComponent(ColliderComponent.ColliderType.COMPLEX_BOX));
        this.addGameObject(wall1);

        GameObject wall2 = new GameObject(false);
        wall2.transform.setPosition(0, 5, 10);
        wall2.transform.setScale(10, 10, 1);
        wall2.addComponent(new ModelRendererComponent(cubeMesh));
        wall2.addComponent(new ColliderComponent(ColliderComponent.ColliderType.COMPLEX_BOX));
        this.addGameObject(wall2);

        GameObject sun = new GameObject(false);
        sun.transform.setPosition(0, 1, 0);
        sun.addComponent(new LightComponent(LightComponent.LightType.DIRECTIONAL));
        this.addGameObject(sun);

        super.init();
    }

    @Override
    public void update() {
        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_X)) {
            Window.getInstance().setCursorLocked(!Window.getInstance().isCursorLocked());
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

        if (Window.getInstance().isCursorLocked()) {
            float sensitivity = 0.005f;

            float xMouseDelta = MouseListener.getDy() * sensitivity;
            float yMouseDelta = MouseListener.getDx() * sensitivity;

            Quaternionf pitchRotation = new Quaternionf().rotateX(xMouseDelta);
            Quaternionf yawRotation = new Quaternionf().rotateY(yMouseDelta);

            camera.transform.addRotationQuaternion(yawRotation);
            camera.transform.addRotationQuaternion(pitchRotation);
        }

        float velP = 20;
        RigidBodyComponent rb =  player.getComponent(RigidBodyComponent.class);
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            rb.setVelocity(rb.getVelocity().add(new Vector3f(0, 0, (float) (Time.getDeltaTime() * velP))));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            rb.setVelocity(rb.getVelocity().add(new Vector3f(0, 0, (float) (Time.getDeltaTime() * -velP))));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            rb.setVelocity(rb.getVelocity().add(new Vector3f((float) (Time.getDeltaTime() * -velP), 0, 0)));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            rb.setVelocity(rb.getVelocity().add(new Vector3f((float) (Time.getDeltaTime() * velP), 0, 0)));
        }

        /*objectA.transform.addLocalRotationEuler((float) Time.getDeltaTime(), (float) Time.getDeltaTime(), (float) Time.getDeltaTime());
        objectB.transform.addLocalRotationEuler((float) Time.getDeltaTime(), (float) Time.getDeltaTime(), (float) Time.getDeltaTime());

        float speed = 5;
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_I)) {
            objectA.transform.addPositionX((float) (-Time.getDeltaTime() * speed));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_O)) {
            objectA.transform.addPositionX((float) (Time.getDeltaTime() * speed));
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_K)) {
            objectB.transform.addPositionX((float) (-Time.getDeltaTime() * speed));
        }
        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_L)) {
            objectB.transform.addPositionX((float) (Time.getDeltaTime() * speed));
        }*/

        //Logger.log(objectA.getComponent(RigidBodyComponent.class).getVelocity());

        SoundManager.getInstance().updateListenerPosition(camera);

        super.update();
    }
}
