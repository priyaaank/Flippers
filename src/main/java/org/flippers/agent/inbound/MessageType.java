package org.flippers.agent.inbound;

import org.flippers.agent.outbound.FlipperEventLoop;
import org.flippers.commands.Command;
import org.flippers.commands.NoOp;
import org.flippers.commands.PingHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum MessageType {
    PING(PingHandler.class),
    ACK(NoOp.class);

    private Class<?> handlerClass;

    MessageType(Class<?> handlerClass) {
        this.handlerClass = handlerClass;
    }

    public Command handler() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> constructor = handlerClass.getConstructor(FlipperEventLoop.class);
        return (Command) constructor.newInstance(FlipperEventLoop.getInstance());
    }

}
