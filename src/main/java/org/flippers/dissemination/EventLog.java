package org.flippers.dissemination;

import org.flippers.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.PriorityBlockingQueue;

import static org.flippers.config.Config.DefaultValues.DEFAULT_EVENT_QUEUE_SIZE;
import static org.flippers.config.Config.KeyNames.EVENT_LOG_QUEUE_SIZE;

public class EventLog {

    //Implement it with a Binary heap or Priority queue to ensure that we can constantly get events that have not been
    //disseminated enough. Also we need to see, how to avoid duplicates, as a overriding state for a node should clear
    //off any existing states for a node. A node marked as DEAD should prevent from disseminating older SUSPECT or ALIVE
    //events for example

    private static Logger LOGGER = LoggerFactory.getLogger(Event.class);
    private PriorityBlockingQueue<Event> eventQueue;

    public EventLog(Config config) {
        this(new PriorityBlockingQueue(config.getValue(EVENT_LOG_QUEUE_SIZE, DEFAULT_EVENT_QUEUE_SIZE)));
    }

    public EventLog(PriorityBlockingQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void enqueue(Event event) {
        this.eventQueue.add(event);
        LOGGER.debug(event.toString());
    }

    public Event getNext() throws InterruptedException {
        return this.eventQueue.take();
    }

}
