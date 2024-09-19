package com.restonic4.citadel.core;

import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadManager {
    private static final List<Thread> threads = new ArrayList<>();
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static Thread startThread(Runnable runnable) {
        return startThread(StringBuilderHelper.concatenate("ThreadManager#", threads.size()), runnable);
    }

    public static Thread startThread(String name, Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(name);
        threads.add(thread);
        thread.start();

        return thread;
    }

    public static Future<?> executeTask(Runnable runnable) {
        return executorService.submit(runnable);
    }

    public static Thread findThreadByName(String name) {
        for (Thread thread : threads) {
            if (thread.getName().equals(name)) {
                return thread;
            }
        }
        return null;
    }

    public static void stopThread(Thread thread) {
        if (thread != null && thread.isAlive()) {
            try {
                thread.interrupt();
            } catch (Exception e) {
                Logger.logError(e);
            }
        }
    }

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
}
