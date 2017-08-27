package org.flippers.agent;

public class ConnectionAcceptor {

    private static ConnectionAcceptor singleInstance;

    private ConnectionAcceptor() {

    }

    public static ConnectionAcceptor instance() {
        if(singleInstance == null) {
            singleInstance = new ConnectionAcceptor();
        }
        return singleInstance;
    }

    

}
