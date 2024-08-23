package me.restonic4.engine.input;

import me.restonic4.engine.util.debug.Logger;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[GLFW_KEY_LAST + 1]; // Listen for keys registered in GLFW. The + 1 is because they added an offset
    private boolean keyPressedOnce[] = new boolean[GLFW_KEY_LAST + 1];
    private boolean keyBeginPress[] = new boolean[GLFW_KEY_LAST + 1];

    public static KeyListener getInstance() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key <= GLFW_KEY_LAST && key >= 0) {
            if (action == GLFW_PRESS) {
                getInstance().keyPressed[key] = true;
                getInstance().keyBeginPress[key] = true;
            } else if (action == GLFW_RELEASE) {
                getInstance().keyPressed[key] = false;
                getInstance().keyBeginPress[key] = false;
                getInstance().keyPressedOnce[key] = false;
            }
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return getInstance().keyPressed[keyCode];
    }

    public static boolean isKeyPressedOnce(int keyCode) {
        if (isKeyPressed(keyCode) && !getInstance().keyPressedOnce[keyCode]) {
            getInstance().keyPressedOnce[keyCode] = true;
            return true;
        }

        return false;
    }

    public static boolean isKeyBeginPress(int keyCode) {
        return getInstance().keyBeginPress[keyCode];
    }

    public static void endFrame() {
        Arrays.fill(getInstance().keyBeginPress, false);
    }
}
