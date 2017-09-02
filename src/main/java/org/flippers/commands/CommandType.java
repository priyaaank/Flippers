package org.flippers.commands;

public enum CommandType {
    PING(PingHandler.class);

    private Class<?> handlerClass;

    CommandType(Class<?> handlerClass) {
        this.handlerClass = handlerClass;
    }

}
