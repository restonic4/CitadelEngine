package me.restonic4.citadel.util;

public class Time {
    private static long timeStarted = System.nanoTime();

    private static double startFrameTime = Time.getRunningTime();
    private static double endFrameTime;
    private static double deltaTime = -1.0f;

    // Gets the time in nanoseconds when the app started
    public static long getTimeStarted() {
        return timeStarted;
    }

    // Gets the time passed between the start and right now in nanoseconds and converts it to seconds with the CONVERSION_FACTOR
    public static double getRunningTime() {
        return ((System.nanoTime() - timeStarted) * CitadelConstants.NANOSECOND_CONVERSION_FACTOR);
    }

    public static double getDeltaTime() {
        return deltaTime;
    }

    public static int getFPS() {
        return (int) (1 / deltaTime);
    }

    // Listening when a frame ends and updates the delta time
    public static void onFrameEnded() {
        endFrameTime = Time.getRunningTime();
        deltaTime = endFrameTime - startFrameTime;
        startFrameTime = endFrameTime;
    }
}
