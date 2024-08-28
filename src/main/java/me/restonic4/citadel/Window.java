package me.restonic4.citadel;

import me.restonic4.citadel.input.KeyListener;
import me.restonic4.citadel.input.MouseListener;
import me.restonic4.citadel.sound.SoundManager;
import me.restonic4.citadel.util.debug.diagnosis.OpenGLDebugOutput;
import me.restonic4.citadel.world.Scene;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.game.Game;
import me.restonic4.game.core.scenes.WorldScene;
import me.restonic4.shared.SharedConstants;
import me.restonic4.citadel.util.Time;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS;

public class Window {
    private static Window instance = null;

    private int width, height;
    private String title;
    private long glfwWindowAddress; // This is the created window, but it saves as a long because is not an object, it's a Memory address, where C saves it.
    private float aspectRatio;

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

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.width = vidMode.width();
        this.height = vidMode.height();

        // Create the window                   width        height       tile         monitor      share(idk what is this)
        glfwWindowAddress = glfwCreateWindow(this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (glfwWindowAddress == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        // Resize callback, gets the current width and height and updates it
        glfwSetFramebufferSizeCallback(glfwWindowAddress, (window, newWidth, newHeight) -> {
            updateWindowSize(newWidth, newHeight);
        });

        glfwSetWindowSizeCallback(glfwWindowAddress, (window, newWidth, newHeight) -> {
            updateWindowSize(newWidth, newHeight);
        });

        glfwSetWindowMaximizeCallback(glfwWindowAddress, (window, maximized) -> {
            if (maximized) {
                Logger.log("Window maximized");

                int[] newWidth = new int[1];
                int[] newHeight = new int[1];

                glfwGetWindowSize(glfwWindowAddress, newWidth, newHeight);

                updateWindowSize(newWidth[0], newHeight[0]);
            }
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
        if (SharedConstants.VSYNC) {
            glfwSwapInterval(1);
        }

        // Make the window visible
        glfwShowWindow(glfwWindowAddress);

        // OpenGL initialization
        GL.createCapabilities();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        // Enables OpenGL advanced logs
        /*glEnable(GL_DEBUG_OUTPUT);
        glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS);
        OpenGLDebugOutput debugOutput = new OpenGLDebugOutput();
        debugOutput.setupDebugMessageCallback();*/

        // Starts the game logic
        Game.start();
    }

    public void loop() {
        while (!glfwWindowShouldClose(glfwWindowAddress)) {
            // Listen for input events
            glfwPollEvents();

            updateAspectRatio();

            Scene scene = SceneManager.getCurrentScene();
            if (scene != null) {
                scene.update();
            }

            KeyListener.endFrame();
            MouseListener.endFrame();

            glfwSwapBuffers(glfwWindowAddress);

            Time.onFrameEnded();

            // Use the FPS cap
            if (!SharedConstants.VSYNC) {
                if (SharedConstants.FPS_CAP <= 0) {
                    continue;
                }

                double desiredDuration = (double) 1 / SharedConstants.FPS_CAP;

                Logger.log(Time.getDeltaTime() + " < " + desiredDuration);

                double timeToSleep = desiredDuration - Time.getDeltaTime();
                if (timeToSleep > 0) {
                    try {
                        Thread.sleep((long) (timeToSleep * 1000.0));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void cleanup() {
        SceneManager.unLoadCurrentScene();
        SoundManager.getInstance().cleanup();

        // Free the memory
        glfwFreeCallbacks(glfwWindowAddress);
        glfwDestroyWindow(glfwWindowAddress);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void updateWindowSize(int width, int height) {
        this.width = width;
        this.height = height;
        updateAspectRatio();
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