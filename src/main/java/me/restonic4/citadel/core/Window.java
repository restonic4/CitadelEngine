package me.restonic4.citadel.core;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import me.restonic4.citadel.events.EventResult;
import me.restonic4.citadel.events.types.CitadelLifecycleEvents;
import me.restonic4.citadel.events.types.WindowEvents;
import me.restonic4.citadel.files.FileManager;
import me.restonic4.citadel.input.KeyListener;
import me.restonic4.citadel.input.MouseListener;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.ImGuiScreen;
import me.restonic4.citadel.render.Renderer;
import me.restonic4.citadel.render.Shader;
import me.restonic4.citadel.sound.SoundManager;
import me.restonic4.citadel.util.GradleUtil;
import me.restonic4.citadel.util.debug.diagnosis.ProfilerManager;
import me.restonic4.citadel.world.Scene;
import me.restonic4.citadel.world.SceneManager;
import me.restonic4.citadel.util.CitadelConstants;
import me.restonic4.citadel.util.Time;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private static Window instance = null;

    private int width, height;
    private String title;
    private long glfwWindowAddress; // This is the created window, but it saves as a long because is not an object, it's a Memory address, where C saves it.
    private float aspectRatio;
    private boolean isCursorLocked;

    private double lastTimeTitleChange = Time.getRunningTime();

    protected ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    protected ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = "#version 330 core";

    public Window() {
        this.width = 1000;
        this.height = 1000;
        this.title = CitadelConstants.DEFAULT_WINDOW_TITLE + " | " + GradleUtil.VERSION;
    }

    public static Window getInstance() {
        if (Window.instance == null) {
            Window.instance = new Window();
        }
        return Window.instance;
    }

    public void run(IGameLogic iGameLogic) {
        init();
        iGameLogic.start(); // Starts your game logic
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

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.width = vidMode.width();
        this.height = vidMode.height();

        // Create the window                   width        height       tile         monitor      share(idk what is this)
        glfwWindowAddress = glfwCreateWindow(this.width, this.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (glfwWindowAddress == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        setWindowIcon(glfwWindowAddress, "assets/textures/icons/icon.png");

        WindowEvents.CREATED.invoker().onWindowCreated(this);

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
        if (CitadelConstants.VSYNC) {
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

        if (!GL.getCapabilities().GL_ARB_bindless_texture) {
            throw new IllegalStateException("Bindless textures not compatible with your graphics card. Tell the devs pls!");
        }

        ImGui.createContext();

        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        imGuiGlfw.init(glfwWindowAddress, true);
        imGuiGl3.init(glslVersion);

        setCursorLocked(true);

        Map<AssetLocation, ImGuiScreen> guis = Registry.getRegistry(Registries.IM_GUI_SCREEN);
        for (Map.Entry<AssetLocation, ImGuiScreen> entry : guis.entrySet()) {
            ImGuiScreen screen = entry.getValue();
            screen.start();
        }

        CitadelLifecycleEvents.CITADEL_STARTED.invoker().onCitadelStarted(CitadelLauncher.getInstance(), this);
    }

    public void loop() {
        Shader defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        while (!glfwWindowShouldClose(glfwWindowAddress)) {
            // Listen for input events
            glfwPollEvents();

            updateAspectRatio();

            Scene scene = SceneManager.getCurrentScene();
            if (scene != null && Time.getDeltaTime() > 0) {
                Renderer.setShader(defaultShader);
                scene.update();
            }

            runImGuiFrame();

            KeyListener.endFrame();
            MouseListener.endFrame();

            glfwSwapBuffers(glfwWindowAddress);

            Time.onFrameEnded();
            ProfilerManager.update();

            // Use the FPS cap
            if (!CitadelConstants.VSYNC) {
                if (CitadelConstants.FPS_CAP <= 0) {
                    continue;
                }

                double desiredDuration = (double) 1 / CitadelConstants.FPS_CAP;

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

        WindowEvents.CLOSED.invoker().onWindowClosed(this);
        CitadelLifecycleEvents.CITADEL_STOPPING.invoker().onCitadelStopping(CitadelLauncher.getInstance(), this);
    }

    private void runImGuiFrame() {
        imGuiGl3.newFrame();
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        Map<AssetLocation, ImGuiScreen> guis = Registry.getRegistry(Registries.IM_GUI_SCREEN);
        for (Map.Entry<AssetLocation, ImGuiScreen> entry : guis.entrySet()) {
            ImGuiScreen screen = entry.getValue();
            screen.render();
        }

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            org.lwjgl.glfw.GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    private void cleanup() {
        CitadelLifecycleEvents.CITADEL_CLEANING_UP.invoker().onCitadelCleaningUp(CitadelLauncher.getInstance(), this);

        SceneManager.unLoadCurrentScene();
        SoundManager.getInstance().cleanup();

        // Clean ImGui
        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        ImGui.destroyContext();

        // Free the memory
        glfwFreeCallbacks(glfwWindowAddress);
        glfwDestroyWindow(glfwWindowAddress);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        CitadelLifecycleEvents.CITADEL_CLEANED_UP.invoker().onCitadelCleanedUp(CitadelLauncher.getInstance(), this);
    }

    private void updateWindowSize(int width, int height) {
        EventResult eventResult = WindowEvents.RESIZED.invoker().onWindowResized(this, width, height);

        if (eventResult == EventResult.CANCELED) {
            glfwSetWindowSize(glfwWindowAddress, this.width, this.height);
            return;
        }

        this.width = width;
        this.height = height;
        updateAspectRatio();
    }

    private void updateAspectRatio() {
        this.aspectRatio = (float) this.width / (float) this.height;
    }

    public void setWindowTitleForced(String newTitle) {
       this.lastTimeTitleChange = Time.getRunningTime() - CitadelConstants.WINDOW_TITLE_CHANGE_FREQUENCY;
       setWindowTitle(newTitle);
    }

    public boolean setWindowTitle(String newTitle) {
        if (Time.getRunningTime() - this.lastTimeTitleChange < CitadelConstants.WINDOW_TITLE_CHANGE_FREQUENCY) {
            return false;
        }

        EventResult eventResult = WindowEvents.TITLE_CHANGING.invoker().onWindowTitleChanging(this, this.title, newTitle);
        if (eventResult == EventResult.CANCELED) {
            return false;
        }

        this.lastTimeTitleChange = Time.getRunningTime();
        this.title = newTitle;
        glfwSetWindowTitle(this.glfwWindowAddress, newTitle);
        return true;
    }

    public static void setWindowIcon(long window, String iconPath) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            String resourcePath = FileManager.getOrExtractFile(
                    FileManager.toResources(iconPath)
            );

            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer icon = STBImage.stbi_load(resourcePath, width, height, channels, 4);
            if (icon == null) {
                throw new RuntimeException("Error loading the icon: " + STBImage.stbi_failure_reason());
            }

            GLFWImage.Buffer iconBuffer = GLFWImage.malloc(1);
            iconBuffer.width(width.get(0));
            iconBuffer.height(height.get(0));
            iconBuffer.pixels(icon);

            GLFW.glfwSetWindowIcon(window, iconBuffer);

            STBImage.stbi_image_free(icon);
        }
    }

    public void setCursorLocked(boolean locked) {
        this.isCursorLocked = locked;

        if (locked) {
            GLFW.glfwSetInputMode(glfwWindowAddress, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        } else {
            GLFW.glfwSetInputMode(glfwWindowAddress, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
            glfwSetCursorPos(glfwWindowAddress, this.width / 2, this.height / 2);
        }
    }

    public boolean isCursorLocked() {
        return this.isCursorLocked;
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