package org.flippers.dissemination;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PriorityBlockingQueueSetTest {

    private static final Integer QUEUE_SIZE = 20;

    private PriorityBlockingQueueSet<Integer> queue;

    @Before
    public void setUp() throws Exception {
        queue = new PriorityBlockingQueueSet<>(QUEUE_SIZE);
    }

    @Test(expected = OutOfMemoryError.class)
    public void shouldThrowErrorWhenAttemptedToGrowBeyondMaximumSize() throws Exception {
        for (int element = 0; element <= 20; element++) {
            queue.enqueue(element);
        }
    }

    @Test
    public void shouldReturnTheSmallestNumberOnPriority() throws Exception {
        for (int element = 1; element <= 10; element++) {
            queue.enqueue(element);
        }

        assertThat(queue.dequeue(), is(1));
    }

    @Test
    public void shouldReturnNullWhenNoElements() throws Exception {
        assertThat(queue.dequeue(), is(nullValue()));
    }

}