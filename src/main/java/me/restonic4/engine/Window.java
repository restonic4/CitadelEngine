package me.restonic4.engine;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryUtil;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.Callable;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final long windowHandle;
    private int height;
    private MouseInput mouseInput;
    private Callable<Void> resizeFunc;
    private int width;

    private final Set<Integer> keysPressedOnce;

    public Window(String title, WindowOptions opts, Callable<Void> resizeFunc) {
        this.resizeFunc = resizeFunc;
        this.keysPressedOnce = new HashSet<>();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        if (opts.antiAliasing) {
            glfwWindowHint(GLFW_SAMPLES, 4);
        }
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        if (opts.compatibleProfile) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        }

        if (opts.width > 0 && opts.height > 0) {
            this.width = opts.width;
            this.height = opts.height;
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            width = vidMode.width();
            height = vidMode.height();
        }

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> resized(w, h));

        glfwSetErrorCallback((int errorCode, long msgPtr) ->
                System.err.println("Error code [" + errorCode + "], msg [" + MemoryUtil.memUTF8(msgPtr) + "]")
        );

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            keyCallBack(key, action);
        });

        glfwMakeContextCurrent(windowHandle);

        if (opts.fps > 0) {
            glfwSwapInterval(0);
        } else {
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);

        int[] arrWidth = new int[1];
        int[] arrHeight = new int[1];
        glfwGetFramebufferSize(windowHandle, arrWidth, arrHeight);
        width = arrWidth[0];
        height = arrHeight[0];

        mouseInput = new MouseInput(windowHandle);
    }

    public void cleanup() {
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
    }

    public int getHeight() {
        return height;
    }

    public MouseInput getMouseInput() {
        return mouseInput;
    }

    public int getWidth() {
        return width;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public boolean isKeyPressedOnce(int keyCode) {
        if (isKeyPressed(keyCode) && !keysPressedOnce.contains(keyCode)) {
            keysPressedOnce.add(keyCode);
            return true;
        }

        return false;
    }

    public void keyCallBack(int key, int action) {
        if (action == GLFW_RELEASE) {
            keysPressedOnce.remove(key);
        }
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    protected void resized(int width, int height) {
        this.width = width;
        this.height = height;
        try {
            resizeFunc.call();
        } catch (Exception excp) {
            System.err.println("Error calling resize callback: " + excp.getMessage());
        }
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public static class WindowOptions {
        public boolean antiAliasing;
        public boolean compatibleProfile;
        public int fps;
        public int height;
        public int ups = Engine.TARGET_UPS;
        public int width;
    }
}