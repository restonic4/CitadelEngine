package me.restonic4.engine.input;

public class MouseListener {
    private static MouseListener instance;

    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3]; // Listen for mouse 3 buttons
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
        mouseListener.isDragging = mouseListener.mouseButtonPressed[0] || mouseListener.mouseButtonPressed[1] || mouseListener.mouseButtonPressed[2];
    }
}
