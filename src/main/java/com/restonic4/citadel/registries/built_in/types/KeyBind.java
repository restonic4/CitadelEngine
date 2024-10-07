package com.restonic4.citadel.registries.built_in.types;

import com.restonic4.citadel.input.KeyListener;
import com.restonic4.citadel.registries.RegistryObject;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class KeyBind extends RegistryObject {
    private int[] defaultKeyCombination;
    private boolean wasPressedOnce = false;

    // Overrides
    private static final Map<Integer, String> keyNames = new HashMap<>();
    static {
        keyNames.put(GLFW.GLFW_KEY_ESCAPE, "Esc");
        keyNames.put(GLFW.GLFW_KEY_ENTER, "Enter");
        keyNames.put(GLFW.GLFW_KEY_TAB, "Tab");
        keyNames.put(GLFW.GLFW_KEY_BACKSPACE, "Backspace");
        keyNames.put(GLFW.GLFW_KEY_DELETE, "Delete");
        keyNames.put(GLFW.GLFW_KEY_LEFT_CONTROL, "Ctrl");
        keyNames.put(GLFW.GLFW_KEY_RIGHT_CONTROL, "Ctrl");
        keyNames.put(GLFW.GLFW_KEY_LEFT_SHIFT, "Shift");
        keyNames.put(GLFW.GLFW_KEY_RIGHT_SHIFT, "Shift");
        keyNames.put(GLFW.GLFW_KEY_LEFT_ALT, "Alt");
        keyNames.put(GLFW.GLFW_KEY_RIGHT_ALT, "Alt");
        keyNames.put(GLFW.GLFW_KEY_LEFT_SUPER, "Super");
        keyNames.put(GLFW.GLFW_KEY_RIGHT_SUPER, "Super");

        // F1-12
        for (int i = 0; i < 12; i++) {
            keyNames.put(GLFW.GLFW_KEY_F1 + i, "F" + (i + 1));
        }
    }

    public KeyBind(int... defaultKeyCombination) {
        this.defaultKeyCombination = defaultKeyCombination;
    }

    public boolean isPressed() {
        int pressed = 0;

        for (int i : defaultKeyCombination) {
            if (KeyListener.isKeyPressed(i)) {
                pressed++;
            }
        }

        return pressed == defaultKeyCombination.length;
    }

    public boolean isPressedOnce() {
        if (isPressed() && !wasPressedOnce) {
            wasPressedOnce = true;
            return true;
        }

        if (!isPressed()) {
            wasPressedOnce = false;
        }

        return false;
    }

    public String getKeyString() {
        if (defaultKeyCombination == null || defaultKeyCombination.length == 0) {
            return "Not assigned";
        }

        StringBuilder keyStringBuilder = new StringBuilder();
        boolean first = true;

        for (int key : defaultKeyCombination) {
            if (!first) {
                keyStringBuilder.append(" + ");
            }
            keyStringBuilder.append(getKeyName(key));
            first = false;
        }

        return keyStringBuilder.toString();
    }

    private String getKeyName(int keyCode) {
        return keyNames.getOrDefault(keyCode, GLFW.glfwGetKeyName(keyCode, 0) != null ? GLFW.glfwGetKeyName(keyCode, 0) : "Unknown key");
    }
}
