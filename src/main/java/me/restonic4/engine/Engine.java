package me.restonic4.engine;

import me.restonic4.engine.graph.Render;
import me.restonic4.engine.scene.Scene;
import me.restonic4.engine.util.debug.DebugManager;
import me.restonic4.engine.util.debug.Logger;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Engine {

    public static final int TARGET_UPS = 30;
    private final IEngineLogic appLogic;
    private final Window window;
    private Render render;
    private boolean running;
    private Scene scene;
    private int targetFps;
    private int targetUps;

    public Engine(String windowTitle, Window.WindowOptions opts, IEngineLogic appLogic) {
        window = new Window(windowTitle, opts, () -> {
            resize();
            return null;
        });
        targetFps = opts.fps;
        targetUps = opts.ups;
        this.appLogic = appLogic;
        render = new Render(window);
        scene = new Scene(window.getWidth(), window.getHeight());
        appLogic.init(window, scene, render);
        running = true;
    }

    private void cleanup() {
        appLogic.cleanup();
        render.cleanup();
        window.cleanup();
    }

    private void resize() {
        int width = window.getWidth();
        int height = window.getHeight();
        scene.resize(width, height);
        render.resize(width, height);
    }

    private void run() {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;

        IGuiInstance iGuiInstance = scene.getGuiInstance();

        Logger.log("Starting the engine");

        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (targetFps <= 0 || deltaFps >= 1) {
                window.getMouseInput().input();
                boolean inputConsumed = iGuiInstance != null && iGuiInstance.handleGuiInput(scene, window);
                appLogic.input(window, scene, now - initialTime, inputConsumed);
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                appLogic.update(window, scene, diffTimeMillis);
                updateTime = now;
                deltaUpdate--;
            }

            if (targetFps <= 0 || deltaFps >= 1) {
                render.render(window, scene);
                deltaFps--;
                window.update();
            }
            initialTime = now;
        }

        cleanup();
    }

    public void start() {
        running = true;
        Logger.log("Dev environment: " + DebugManager.isDevEnvironment());
        run();
    }

    public void stop() {
        running = false;
    }

    public void close() {
        Logger.log("Closing the window");

        Logger.stop();

        glfwSetWindowShouldClose(window.getWindowHandle(), true);
    }

    public static void crash(String reason) {
        throw new RuntimeException(reason);
    }

    public static void crash() {
        crash("Forced crash");
    }
}
