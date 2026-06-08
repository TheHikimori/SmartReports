package me.azuredev.smartreports.database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncExecutor {

    private static final ExecutorService EXECUTOR =
            Executors.newFixedThreadPool(8);

    public static ExecutorService getExecutor() {
        return EXECUTOR;
    }

    public static void shutdown() {
        EXECUTOR.shutdownNow();
    }
}