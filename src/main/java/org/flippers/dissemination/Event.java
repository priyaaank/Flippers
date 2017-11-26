package org.flippers.dissemination;

public class Event {

    private EventType eventType;
    private String hostIp;

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
}
