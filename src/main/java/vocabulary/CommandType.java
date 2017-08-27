package vocabulary;

public enum CommandType {
    HANDSHAKE(HandshakeHandler.class),
    PING(PingHandler.class);


    private Class<?> handlerClass;

    CommandType(Class<?> handlerClass) {
        this.handlerClass = handlerClass;
    }

}
