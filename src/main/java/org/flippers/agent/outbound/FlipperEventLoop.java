package org.flippers.agent.outbound;

import org.flippers.tasks.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlipperEventLoop {

    private ExecutorService poolExecutor;

    public FlipperEventLoop(ExecutorService executorService) {
        this.poolExecutor = poolExecutor;
    }

    public FlipperEventLoop() {
        this(Executors.newFixedThreadPool(1));
    }

    public void enqueue(Task task) {
        this.poolExecutor.submit(() -> task.execute());
    }

}
