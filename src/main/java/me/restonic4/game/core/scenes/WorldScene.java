package me.restonic4.game.core.scenes;

import me.restonic4.engine.Scene;
import me.restonic4.engine.SceneManager;
import me.restonic4.engine.Window;
import me.restonic4.engine.input.KeyListener;
import me.restonic4.engine.object.GameObject;
import me.restonic4.engine.object.Mesh;
import me.restonic4.engine.object.Transform;
import me.restonic4.engine.object.components.DebugComponent;
import me.restonic4.engine.object.components.ModelRendererComponent;
import me.restonic4.engine.render.Camera;
import me.restonic4.engine.render.OrthographicCamera;
import me.restonic4.engine.render.PerspectiveCamera;
import me.restonic4.engine.render.Shader;
import me.restonic4.engine.util.Time;
import me.restonic4.engine.util.debug.Logger;
import me.restonic4.engine.util.math.RandomUtil;
import me.restonic4.shared.SharedMathConstants;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class WorldScene extends Scene {
    GameObject player;

    Mesh mesh = new Mesh(
            new Vector3f[] {
                    new Vector3f(-1, 1, -1),  // 0
                    new Vector3f( 1, 1, -1),  // 1
                    new Vector3f( -1, 1,  1),  // 2
                    new Vector3f(1, 1,  1),  // 3

                    new Vector3f(-1, -1, -1),  // 4
                    new Vector3f( 1, -1, -1),  // 5
                    new Vector3f( -1, -1,  1),  // 6
                    new Vector3f(1, -1,  1)   // 7
            },
            new int[] {
                    0, 3, 1, 0, 2, 3,
                    5, 7, 4, 7, 6, 4,
                    3, 7, 1, 7, 5, 1,
                    6, 0, 4, 6, 2, 0,
                    6, 7, 3, 6, 3, 2,
                    0, 1, 5, 0, 5, 4
            },
            new Vector4f[] {           // Colors for each vertex
                    new Vector4f(1, 0, 0, 1), // Red
                    new Vector4f(0, 1, 0, 1), // Green
                    new Vector4f(0, 0, 1, 1), // Blue
                    new Vector4f(1, 1, 0, 1), // Yellow
                    new Vector4f(1, 0, 1, 1), // Magenta
                    new Vector4f(0, 1, 1, 1), // Cyan
                    new Vector4f(0.5f, 0.5f, 0.5f, 1), // Gray
                    new Vector4f(1, 1, 1, 1)  // White
            }
    );

    Mesh pyramidMesh = new Mesh(
            new Vector3f[] {
                    // Base
                    new Vector3f(-1, -1, -1), // Vértice 0
                    new Vector3f( 1, -1, -1), // Vértice 1
                    new Vector3f( 1, -1,  1), // Vértice 2
                    new Vector3f(-1, -1,  1), // Vértice 3

                    // Cima
                    new Vector3f(0, 1, 0)     // Vértice 4
            },
            new int[] {
                    // Base
                    1, 2, 0,
                    2, 3, 0,

                    // Caras laterales
                    0, 4, 1,
                    1, 4, 2,
                    2, 4, 3,
                    3, 4, 0
            },
            new Vector4f[] {
                    new Vector4f(1, 0, 0, 1), // Color para Vértice 0
                    new Vector4f(0, 1, 0, 1), // Color para Vértice 1
                    new Vector4f(0, 0, 1, 1), // Color para Vértice 2
                    new Vector4f(1, 1, 0, 1), // Color para Vértice 3
                    new Vector4f(1, 0, 1, 1)  // Color para Vértice 4
            }
    );

    Mesh quad = new Mesh(
            new Vector3f[] {
                    // Base
                    new Vector3f(-1, -1, 0), // Vértice 0
                    new Vector3f( 1, -1, 0), // Vértice 1
                    new Vector3f( 1, 1,  0), // Vértice 2
                    new Vector3f(-1, 1,  0), // Vértice 3
            },
            new int[] {
                    // Base
                    0, 1, 2,
                    2, 3, 0
            },
            new Vector4f[] {
                    new Vector4f(1, 0, 0, 1), // Color para Vértice 0
                    new Vector4f(0, 1, 0, 1), // Color para Vértice 1
                    new Vector4f(0, 0, 1, 1), // Color para Vértice 2
                    new Vector4f(1, 1, 0, 1), // Color para Vértice 3
            }
    );

    @Override
    public void init() {
        Logger.log("Starting the world scene");

        Transform camTransform = new Transform();
        camTransform.setPosition(0, 0, 100);
        camTransform.setScale(1, 1, 1);
        camera = new PerspectiveCamera(camTransform);



        /*player = new GameObject("player", false, new Transform(new Vector3f(0, 0, 0), new Vector3f(1,1,1)));
        player.addComponent(new ModelRendererComponent(pyramidMesh));
        this.addGameObject(player);*/

        /*GameObject test2 = new GameObject("test2", true, new Transform(new Vector3f(0, 0, 0), new Vector3f(1,1,1)));
        test2.addComponent(new ModelRendererComponent(pyramidMesh));
        this.addGameObject(test2);

        GameObject test3 = new GameObject("test3", true, new Transform(new Vector3f(3, 0, 0), new Vector3f(1,1,1)));
        test3.addComponent(new ModelRendererComponent(pyramidMesh));
        this.addGameObject(test3);*/



        /*GameObject player = new GameObject(false);
        player.transform.setScale(10, 10, 10);
        player.addComponent(new ModelRendererComponent(pyramidMesh));
        //player.transform.setPosition(0, 100, 100);
        player.transform.setPosition(0, 0, 0);
        //this.addGameObject(player);

        GameObject player2 = new GameObject(false);
        player2.transform.setScale(10, 10, 10);
        player2.addComponent(new ModelRendererComponent(pyramidMesh));
        //player.transform.setPosition(0, 100, 100);
        player2.transform.setPosition(0, 50, 0);

        int in = 0;

        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                for (int w = 0; w < amount; w++) {
                    if (in == 25) {
                        this.addGameObject(player2);
                    }

                    GameObject test = new GameObject("test:" + i + ":" + j + ":" + w, false, new Transform(new Vector3f(i * 20, w * 20, j * 20), new Vector3f(1, 1, 1)));
                    test.addComponent(new ModelRendererComponent(mesh));
                    //this.addGameObject(test);

                    if (i == 5 && j == 5 && w == 5) {
                        test.transform.setScale(5,5,5);
                        test.transform.addPositionY(50);
                        this.addGameObject(player);
                    }

                    this.addGameObject(test);

                    in++;
                }
            }
        }*/

        generate();

        /*for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                for (int w = 0; w < amount; w++) {
                    GameObject test = new GameObject("testxd:" + i + ":" + j + ":" + w, true, new Transform(new Vector3f(i * 2, w * 2, j * 2), new Vector3f(1, 1, 1)));
                    test.addComponent(new ModelRendererComponent(mesh));
                    this.addGameObject(test);
                }
            }
        }*/

        /*for (int i = 0; i < 1000; i++) {
            Transform objectTransform = new Transform(new Vector3f(0, 0, -i * 11), new Vector3f(10, 10, 1));
            GameObject object = new GameObject("object" + i, true, objectTransform);
            mesh = new Mesh(null, null, new Vector4f());
            mesh.setColor(new Vector4f(RandomUtil.randomTiny(), RandomUtil.randomTiny(), RandomUtil.randomTiny(), 1));
            object.addComponent(new ModelRendererComponent(mesh));
            this.addGameObject(object);
        }

        for (int i = 0; i < 1000; i++) {
            Transform objectTransform = new Transform(new Vector3f(20, 0, -i * 11), new Vector3f(10, 10, 1));
            GameObject object = new GameObject("objectA" + i, false, objectTransform);
            mesh = new Mesh(null, null, new Vector4f());
            mesh.setColor(new Vector4f(RandomUtil.randomTiny(), RandomUtil.randomTiny(), RandomUtil.randomTiny(), 1));
            object.addComponent(new ModelRendererComponent(mesh));
            this.addGameObject(object);
        }*/

        //genWall();

        /*GameObject gameObjectA = new GameObject("a", new Transform(new Vector3f(0, 0, -30), new Vector3f(10,10,1)));
        gameObjectA.addComponent(new ModelRendererComponent(new Vector4f(1,0,0,1)));
        this.addGameObject(gameObjectA);

        GameObject gameObjectB = new GameObject("b", new Transform(new Vector3f(0, 0, -40), new Vector3f(10,10,1)));
        gameObjectB.addComponent(new ModelRendererComponent(new Vector4f(0,1,0,1)));
        this.addGameObject(gameObjectB);

        GameObject gameObjectC = new GameObject("c", new Transform(new Vector3f(0, 0, -50), new Vector3f(10,10,1)));
        gameObjectC.addComponent(new ModelRendererComponent(new Vector4f(0,0,1,1)));
        this.addGameObject(gameObjectC);*/
    }

    /*public void genWall() {
        int size = 10;

        int width = 10;
        int height = width;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                GameObject gameObject = new GameObject("i" + i + "j" + j, new Transform(new Vector3f(i * size, j * size, -20), new Vector3f(size,size,1)));
                gameObject.addComponent(new ModelRendererComponent(new Vector4f(RandomUtil.randomTiny(),RandomUtil.randomTiny(),RandomUtil.randomTiny(),1)));
                this.addGameObject(gameObject);
            }
        }
    }*/


    /*public void generateSphere(float radius, int density) {
        // Radio de la esfera
        float radiusSquared = radius * radius;

        // Centro de la esfera
        Vector3f center = new Vector3f(200, 200, 0); // Puedes cambiarlo si es necesario

        // Densidad de la esfera (cantidad de objetos por eje)
        float step = radius / density;

        for (float x = -radius; x <= radius; x += step) {
            for (float y = -radius; y <= radius; y += step) {
                for (float z = -radius; z <= radius; z += step) {
                    // Verificar si el punto está dentro de la esfera
                    float distanceSquared = x * x + y * y + z * z;
                    if (distanceSquared <= radiusSquared) {
                        // Posición del objeto
                        float xPos = center.x + x;
                        float yPos = center.y + y;
                        float zPos = center.z + z;

                        // Calcular el color
                        float distanceFromCenter = (float) Math.sqrt(distanceSquared);
                        float colorFactor = distanceFromCenter / radius;
                        Vector4f color = new Vector4f(colorFactor, 0, 1 - colorFactor, 1);

                        // Calcular la dirección hacia el centro
                        Vector3f directionToCenter = new Vector3f(center).sub(xPos, yPos, zPos).normalize();

                        // Calcular la rotación que alinea el objeto hacia el centro
                        Quaternionf rotation = new Quaternionf().lookAlong(directionToCenter, new Vector3f(0, 1, 0));

                        // Crear y agregar el objeto
                        GameObject go = new GameObject(
                                "Obj" + x + "_" + y + "_" + z,
                                new Transform(new Vector3f(xPos, yPos, zPos), new Vector3f(step, step, step))
                        );

                        // Aplicar la rotación al objeto
                        go.transform.setRotation(rotation);

                        go.addComponent(new ModelRendererComponent(color));
                        this.addGameObject(go);

                        gameObjects.add(go);
                    }
                }
            }
        }
    }*/

    public void generate() {
        Mesh[] list = new Mesh[]{mesh, pyramidMesh};

        int amount = 16;

        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                for (int w = 0; w < amount; w++) {
                    Mesh selected = RandomUtil.getRandom(list);

                    if (i == 0 && j == 0 && w == 0) {
                        selected = mesh;
                    }

                    if (i == amount - 1 && j == amount - 1 && w == amount - 1) {
                        selected = pyramidMesh;
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

    int index = 0;

    @Override
    public void update() {
        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_C)) {
            throw new RuntimeException("lol");
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_U)) {
            SceneManager.getInstance().loadScene(new WorldScene());
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_R)) {
            Logger.log("reset");
            camera.transform.setPosition(0, 0, 0);
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



        //player.transform.setPosition(offset, offset, offset);

        /*for (GameObject gameObject : this.getGameObjects()) {
            int extraOffsetX, extraOffsetY, extraOffsetZ;

            extraOffsetX = Integer.parseInt(gameObject.getName().split(":")[1]);
            extraOffsetY = Integer.parseInt(gameObject.getName().split(":")[2]);
            extraOffsetZ = Integer.parseInt(gameObject.getName().split(":")[3]);

            float offset = (float) Math.sin((Time.getRunningTime()) * (float) ((extraOffsetX + extraOffsetY + extraOffsetZ) )/ 10);

            gameObject.transform.setPosition(offset + extraOffsetX, offset + extraOffsetY, offset+ extraOffsetZ);
            //gameObject.transform.addPositionY(RandomUtil.random(-10, 10));
        }*/

        super.update();
    }
}
