package me.restonic4.game.core.scenes;

import me.restonic4.engine.Scene;
import me.restonic4.engine.Window;
import me.restonic4.engine.input.KeyListener;
import me.restonic4.engine.object.GameObject;
import me.restonic4.engine.object.components.DebugComponent;
import me.restonic4.engine.render.Camera;
import me.restonic4.engine.render.Shader;
import me.restonic4.engine.util.Time;
import me.restonic4.engine.util.debug.Logger;
import me.restonic4.shared.SharedMathConstants;
import org.joml.Vector2f;
import org.joml.Vector3f;
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
    private int vertexID, fragmentID, shaderProgram;

    /*private float[] vertexArray = {
            // position               // color
            100f,   0f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            0f, 100f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            100f, 100f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            0f,   0f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,  // Bottom left  3

            100f,   0f, -100.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            0f, 100f, -100.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            100f, 100f, -100.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            0f,   0f, -100.0f,       1.0f, 1.0f, 0.0f, 1.0f  // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3, // bottom left triangle

            6, 5, 4, // Top right triangle
            4, 5, 7, // bottom left triangle

            3, 1, 5, // Top right triangle
            3, 5, 7, // bottom left triangle

            4, 6, 2, // Top right triangle
            4, 2, 0, // bottom left triangle

            2, 6, 5, // Top right triangle
            2, 5, 1, // bottom left triangle

            0, 4, 7, // Top right triangle
            0, 7, 3 // bottom left triangle
    };*/

    private float[] vertexArray = {
            // position               // color
            100f,   0f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            0f, 100f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            100f, 100f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            0f,   0f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,  // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3, // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader shader;

    @Override
    public void init() {
        GameObject gameObject = new GameObject("xd");
        gameObject.addComponent(new DebugComponent());
        this.addGameObject(gameObject);

        camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));

        shader = new Shader("shaders/default.glsl");
        shader.compile();

        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update() {
        shader.use();

        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());
        shader.uploadFloat("uTime", (float) Time.getRunningTime());

        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        //glEnableVertexAttribArray(0);
        //glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        //glDisableVertexAttribArray(0);
        //glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        shader.detach();

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
                + ", Pos: (" + camera.getPosition().x + ", " + camera.getPosition().y + ", " + camera.getPosition().z + ")"
                + ", Rot: (" + camera.getRotation().x + ", " + camera.getRotation().y + ", " + camera.getRotation().z + ")"
        );

        for (GameObject gameObjects : this.gameObjects) {
            gameObjects.update();
        }
    }
}
