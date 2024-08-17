package me.restonic4.engine.input;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener instance;

    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[GLFW_MOUSE_BUTTON_LAST + 1]; // Listen for mouse buttons registered in GLFW. The + 1 is because they added an offset
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener getInstance() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        MouseListener mouseListener = getInstance();

        mouseListener.lastX = mouseListener.xPos;
        mouseListener.lastY = mouseListener.yPos;
        mouseListener.xPos = xpos;
        mouseListener.yPos = ypos;

        // If a mouse button is pressed, means its dragging, because this method gets called when the mouse moves
        mouseListener.isDragging = mouseListener.mouseButtonPressed[0] || mouseListener.mouseButtonPressed[1] || mouseListener.mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        MouseListener mouseListener = getInstance();

        if (action == GLFW_PRESS) {
            if (button < mouseListener.mouseButtonPressed.length) {
                mouseListener.mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < mouseListener.mouseButtonPressed.length) {
                mouseListener.mouseButtonPressed[button] = false;
                mouseListener.isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        MouseListener mouseListener = getInstance();

        mouseListener.scrollX = xOffset;
        mouseListener.scrollY = yOffset;
    }

    public static void endFrame() {
        MouseListener mouseListener = getInstance();

        mouseListener.scrollX = 0;
        mouseListener.scrollY = 0;
        mouseListener.lastX = mouseListener.xPos;
        mouseListener.lastY = mouseListener.yPos;
    }

    public static float getX() {
        return (float) getInstance().xPos;
    }

    public static float getY() {
        return (float) getInstance().yPos;
    }

    public static float getDx() {
        return (float) (getInstance().lastX - getInstance().xPos);
    }

    public static float getDy() {
        return (float) (getInstance().lastY - getInstance().yPos);
    }

    public static float getScrollX() {
        return (float) getInstance().scrollX;
    }

    public static float getScrollY() {
        return (float) getInstance().scrollY;
    }

    public static boolean isDragging() {
        return getInstance().isDragging;
    }

    public static boolean isMouseButtonDown(int button) {
        if (button < getInstance().mouseButtonPressed.length) {
            return getInstance().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
