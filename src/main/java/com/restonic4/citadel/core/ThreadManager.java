package com.restonic4.citadel.core;

import com.restonic4.citadel.util.helpers.StringBuilderHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Easy way of managing threads.
 */
public class ThreadManager {
    private static final List<Thread> threads = new ArrayList<>();
    private static final List<Thread> criticalThreads = new ArrayList<>();
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Starts a new thread with a default name.
     * @param runnable The code to be executed.
     * @return The thread object.
     */
    public static Thread startThread(Runnable runnable) {
        return startThread(StringBuilderHelper.concatenate("ThreadManager#", threads.size()), false, runnable);
    }

    /**
     * Starts a new critical thread with a default name. If this thread crashes the app will also crash as well.
     * @param runnable The code to be executed.
     * @return The thread object.
     */
    public static Thread startCriticalThread(Runnable runnable) {
        return startThread(StringBuilderHelper.concatenate("ThreadManager#Critical#", criticalThreads.size()), true, runnable);
    }

    /**
     * Starts a new thread with a custom name.
     * @param runnable The code to be executed.
     * @param isCritical Sets if the app should crash if the thread crashes.
     * @return The thread object.
     */
    public static Thread startThread(String name, boolean isCritical, Runnable runnable) {
        Logger.logExtra(StringBuilderHelper.concatenate("Starting a new thread: ", name));

        Runnable threadCode;

        if (isCritical) {
            threadCode = () -> {
                try {
                    runnable.run();
                } catch (Exception e) {
                    Logger.logError(e);
                    Logger.logError("Critical thread crashed, shutting down the application...");
                    handleCriticalCrash(e);
                }
            };
        }
        else {
            threadCode = runnable;
        }

        Thread thread = new Thread(threadCode);
        thread.setName(name);
        threads.add(thread);
        thread.start();

        return thread;
    }


    /**
     * Executes code asynchronously.
     * @param runnable The code to be executed.
     * @return A Future object.
     */
    public static Future<?> executeTask(Runnable runnable) {
        return executorService.submit(runnable);
    }

    /**
     * Finds a thread by its name.
     * @param name The name.
     * @return The found Thread object, could be null.
     */
    public static Thread findThreadByName(String name) {
        for (Thread thread : threads) {
            if (thread.getName().equals(name)) {
                return thread;
            }
        }
        return null;
    }

    /**
     * Stops the thread safely.
     * @param thread The desired thread.
     */
    public static void stopThread(Thread thread) {
        if (thread != null && thread.isAlive()) {
            try {
                thread.interrupt();
            } catch (Exception e) {
                Logger.logError(e);
            }
        }
    }

    /**
     * Stops all threads.
     */
    public static void stopAllThreads() {
        for (Thread thread : threads) {
            stopThread(thread);
        }
        threads.clear();
    }

    public static void shutdownExecutor() {
        executorService.shutdown();
    }

    public static List<Thread> getThreads() {
        return threads;
    }

    private static void handleCriticalCrash(Exception e) {
        Logger.logError("Critical crash detected: " + e.getMessage());
        stopAllThreads();
        shutdownExecutor();

        System.exit(1);
    }
}
