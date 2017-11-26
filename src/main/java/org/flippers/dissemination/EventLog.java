package org.flippers.dissemination;

public class EventLog {

    //Implement it with a Binary heap or Priority queue to ensure that we can constantly get events that have not been
    //disseminated enough. Also we need to see, how to avoid duplicates, as a overriding state for a node should clear
    //off any existing states for a node. A node marked as DEAD should prevent from disseminating older SUSPECT or ALIVE
    //events for example

    public void enqueue(Event event) {

    }

}
