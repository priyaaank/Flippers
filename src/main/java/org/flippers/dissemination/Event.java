package org.flippers.dissemination;

import java.util.concurrent.atomic.AtomicInteger;

public class Event implements Comparable {

    private EventType eventType;
    private String hostIp;
    private AtomicInteger nodesNotifiedCount;

    public Event(String hostIp, EventType eventType) {
        this.hostIp = hostIp;
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getHostIp() {
        return hostIp;
    }

    public Integer incrementNotificationCount() {
        return nodesNotifiedCount.incrementAndGet();
    }

    @Override
    public int compareTo(Object otherEvent) {
        if (this.equals(otherEvent)) return 0;
        if(this.nodesNotifiedCount == null && ((Event)otherEvent).nodesNotifiedCount == null) return 0;
        if(this.nodesNotifiedCount == null) return -1;
        if(((Event)otherEvent).nodesNotifiedCount == null) return 1;
        if(this.nodesNotifiedCount.get() == ((Event)otherEvent).nodesNotifiedCount.get()) return 0;
        return this.nodesNotifiedCount.get() > ((Event)otherEvent).nodesNotifiedCount.get() ? 1 : -1;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType=" + eventType +
                ", hostIp='" + hostIp + '\'' +
                ", nodesNotifiedCount=" + nodesNotifiedCount +
                '}';
    }
}
