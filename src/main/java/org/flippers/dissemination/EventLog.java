package org.flippers.dissemination;

import java.util.concurrent.PriorityBlockingQueue;

public class EventLog {

    //Implement it with a Binary heap or Priority queue to ensure that we can constantly get events that have not been
    //disseminated enough. Also we need to see, how to avoid duplicates, as a overriding state for a node should clear
    //off any existing states for a node. A node marked as DEAD should prevent from disseminating older SUSPECT or ALIVE
    //events for example

    private PriorityBlockingQueue<Event> eventQueue;

    public EventLog() {
        this(new PriorityBlockingQueue(Integer.MAX_VALUE));
    }

    public EventLog(PriorityBlockingQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void enqueue(Event event) {
        this.eventQueue.add(event);
    }

    public Event getNext() throws InterruptedException {
        return this.eventQueue.take();
    }

}
