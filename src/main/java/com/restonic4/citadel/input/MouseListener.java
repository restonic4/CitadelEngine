package com.restonic4.citadel.input;

import com.restonic4.ClientSide;

import static org.lwjgl.glfw.GLFW.*;

@ClientSide
public class MouseListener {
    private static double scrollX, scrollY = 0;
    private static double xPos, yPos, lastY, lastX = 0;
    private static boolean mouseButtonPressed[] = new boolean[GLFW_MOUSE_BUTTON_LAST + 1]; // Listen for mouse buttons registered in GLFW. The + 1 is because they added an offset
    private static boolean isDragging;

    public static void mousePosCallback(long window, double xpos, double ypos) {
        lastX = xPos;
        lastY = yPos;
        xPos = xpos;
        yPos = ypos;

        // If a mouse button is pressed, means its dragging, because this method gets called when the mouse moves
        isDragging = mouseButtonPressed[0] || mouseButtonPressed[1] || mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < mouseButtonPressed.length) {
                mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < mouseButtonPressed.length) {
                mouseButtonPressed[button] = false;
                isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        scrollX = xOffset;
        scrollY = yOffset;
    }

    public static void endFrame() {
        scrollX = 0;
        scrollY = 0;
        lastX = xPos;
        lastY = yPos;
    }

    public static float getX() {
        return (float) xPos;
    }

    public static float getY() {
        return (float) yPos;
    }

    public static float getDx() {
        return (float) (lastX - xPos);
    }

    public static float getDy() {
        return (float) (lastY - yPos);
    }

    public static double getLastX() {
        return lastX;
    }

    public static double getLastY() {
        return lastY;
    }

    public static float getScrollX() {
        return (float) scrollX;
    }

    public static float getScrollY() {
        return (float) scrollY;
    }

    public static boolean isDragging() {
        return isDragging;
    }

    public static boolean isMouseButtonDown(int button) {
        if (button < mouseButtonPressed.length) {
            return mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
