package me.restonic4.citadel.util;

import me.restonic4.citadel.registries.built_in.managers.ProfilerStats;
import me.restonic4.citadel.util.debug.diagnosis.ProfilerManager;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

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
        return glfwGetTime();
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

        // Sending data to the profiler in case its on
        ProfilerManager.registerStat(ProfilerStats.DELTA_TIME, (float) deltaTime);
        ProfilerManager.registerStat(ProfilerStats.FPS, getFPS());
    }
}
