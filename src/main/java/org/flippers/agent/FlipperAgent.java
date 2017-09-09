package org.flippers.agent;

import org.flippers.config.Config;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class FlipperAgent {

    private ExecutorService executorService;
    private ReceivedMessageHandler receivedReceivedMessageHandler;
    private MessageListener listener;
    private MessageSender sender;
    private DatagramSocket socket;
    private MessageTypeRegistry registry;
    private AtomicBoolean shutdownInitiated = new AtomicBoolean(Boolean.FALSE);

    public FlipperAgent(Config config) throws SocketException {
        this.socket = new DatagramSocket(config.getListenPort());
        this.executorService = Executors.newFixedThreadPool(1);
        this.sender = new MessageSender(this.socket, this.executorService);
        this.registry = new MessageTypeRegistry(this.sender, config);
        this.receivedReceivedMessageHandler = new DefaultReceivedMessageHandler(executorService, registry);
        this.listener = new MessageListener(this.socket, this.receivedReceivedMessageHandler);
    }

    public FlipperAgent() throws SocketException {
        this(new Config.ConfigBuilder().build());
    }

    public void start() {
        this.listener.beginAccepting();
    }

    public void stop() throws InterruptedException {
        if (shutdownInitiated.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
            this.socket.close();
        }
    }

}
