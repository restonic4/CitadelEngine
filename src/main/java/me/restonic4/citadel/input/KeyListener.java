package me.restonic4.citadel.input;

import me.restonic4.citadel.registries.built_in.types.KeyBind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public abstract class KeyListener {
    private static boolean keyPressed[] = new boolean[GLFW_KEY_LAST + 1]; // Listen for keys registered in GLFW. The + 1 is because they added an offset
    private static boolean keyPressedOnce[] = new boolean[GLFW_KEY_LAST + 1];
    private static boolean keyBeginPress[] = new boolean[GLFW_KEY_LAST + 1];

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key <= GLFW_KEY_LAST && key >= 0) {
            if (action == GLFW_PRESS) {
                keyPressed[key] = true;
                keyBeginPress[key] = true;
            } else if (action == GLFW_RELEASE) {
                keyPressed[key] = false;
                keyBeginPress[key] = false;
                keyPressedOnce[key] = false;
            }
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }

    public static boolean isKeyPressedOnce(int keyCode) {
        if (isKeyPressed(keyCode) && !keyPressedOnce[keyCode]) {
            keyPressedOnce[keyCode] = true;
            return true;
        }

        return false;
    }

    public static boolean isKeyBeginPress(int keyCode) {
        return keyBeginPress[keyCode];
    }

    public static void endFrame() {
        Arrays.fill(keyBeginPress, false);
    }
}
