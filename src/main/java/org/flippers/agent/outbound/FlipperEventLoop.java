package org.flippers.agent.outbound;

import org.flippers.tasks.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlipperEventLoop {

    private static FlipperEventLoop singleInstance;
    private ExecutorService poolExecutor;

    private FlipperEventLoop(ExecutorService executorService) {
        this.poolExecutor = poolExecutor;
    }

    private FlipperEventLoop() {
        this(Executors.newFixedThreadPool(1));
    }

    public static FlipperEventLoop getInstance() {
        synchronized (singleInstance) {
            if (singleInstance == null) singleInstance = new FlipperEventLoop();
        }
        return singleInstance;
    }

    public void enqueue(Task task) {
        this.poolExecutor.submit(() -> task.execute());
    }

}
