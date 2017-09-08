package org.flippers.agent.outbound;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FlipperEventLoopTest {

    FlipperEventLoop eventLoop;
    CountDownLatch awaitCompletion = new CountDownLatch(1);

    @Before
    public void setUp() throws Exception {
        eventLoop = FlipperEventLoop.getInstance();
    }

    @Test
    public void shouldExecuteTheSubmittedTasks() throws Exception {
        eventLoop.enqueue(() -> awaitCompletion.countDown());
        assertThat(awaitCompletion.await(1, TimeUnit.SECONDS), is(Boolean.TRUE));
    }

}