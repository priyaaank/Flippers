package org.flippers.agent.outbound;

import org.flippers.tasks.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class FlipperEventLoop {

    private static AtomicReference<FlipperEventLoop> singleEventLoop = new AtomicReference<>();
    private ExecutorService poolExecutor;

    private FlipperEventLoop() {
        this.poolExecutor = Executors.newFixedThreadPool(1);
    }

    public static FlipperEventLoop getInstance() {
        if (singleEventLoop.get() == null)
            singleEventLoop.compareAndSet(null, new FlipperEventLoop());
        return singleEventLoop.get();
    }

    public void enqueue(Task task) {
        this.poolExecutor.submit(() -> task.execute());
    }

}
