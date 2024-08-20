package me.restonic4.engine;

import me.restonic4.engine.input.KeyListener;
import me.restonic4.engine.input.MouseListener;
import me.restonic4.shared.SharedConstants;
import me.restonic4.engine.util.Time;
import me.restonic4.engine.util.debug.Logger;
import me.restonic4.game.core.scenes.WorldScene;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private static Window instance = null;

    private int width, height;
    private String title;
    private long glfwWindowAddress; // This is the created window, but it saves as a long because is not an object, it's a Memory address, where C saves it.
    private float aspectRatio;

    // TODO: Change this, a way to set, get and update the scene
    public Scene currentScene = new WorldScene();

    public Window() {
        this.width = 1000;
        this.height = 1000;
        this.title = SharedConstants.WINDOW_TITLE;
    }

    public static Window getInstance() {
        if (Window.instance == null) {
            Window.instance = new Window();
        }
        return Window.instance;
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    public void init() {
        Logger.log("Creating the GLFW window");

        // This redirects errors to sout.err
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // Gets the default GLFW settings (fullscreen, resizeable, ect)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Invisible window at the start, is not created yet
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window                   width        height       tile         monitor      share(idk what is this)
        glfwWindowAddress = glfwCreateWindow(this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (glfwWindowAddress == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        // Resize callback, gets the current width and height and updates it
        glfwSetFramebufferSizeCallback(glfwWindowAddress, (window, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
            updateAspectRatio();
        });

        glfwSetWindowSizeCallback(glfwWindowAddress, (window, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
            updateAspectRatio();
        });


        // Listen to mouse input
        glfwSetCursorPosCallback(glfwWindowAddress, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindowAddress, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindowAddress, MouseListener::mouseScrollCallback);

        // Listen to keyboard input
        glfwSetKeyCallback(glfwWindowAddress, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindowAddress);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindowAddress);

        // OpenGL initialization
        GL.createCapabilities();

        // TODO: Make this better, like a scene manager
        currentScene.init();
        currentScene.activate();
    }

    public void loop() {
        while (!glfwWindowShouldClose(glfwWindowAddress)) {
            // Listen for input events
            glfwPollEvents();

            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            updateAspectRatio();
            currentScene.update();

            glfwSwapBuffers(glfwWindowAddress);

            //glfwSetWindowTitle(glfwWindowAddress, "FPS: " + Time.getFPS());

            Time.onFrameEnded();
        }
    }

    private void cleanup() {
        // Free the memory
        glfwFreeCallbacks(glfwWindowAddress);
        glfwDestroyWindow(glfwWindowAddress);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void updateAspectRatio() {
        this.aspectRatio = (float) this.width / (float) this.height;
    }

    public float getAspectRatio() {
        updateAspectRatio();
        return this.aspectRatio;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public long getGlfwWindowAddress() {
        return glfwWindowAddress;
    }
}