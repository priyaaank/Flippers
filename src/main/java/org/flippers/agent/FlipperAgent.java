package org.flippers.agent;

import org.flippers.config.FileConfig;
import org.flippers.handlers.HandlerExecutor;
import org.flippers.handlers.MessageHandlerExecutor;
import org.flippers.handlers.MessageTypeRegistry;
import org.flippers.messages.MessageCreator;
import org.flippers.peers.MembershipList;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.flippers.config.Config.DefaultValues.DEFAULT_LISTEN_PORT;
import static org.flippers.config.Config.DefaultValues.DEFAULT_THREAD_POOL_COUNT;
import static org.flippers.config.Config.KeyNames.LISTEN_PORT;
import static org.flippers.config.Config.KeyNames.THREAD_POOL_SIZE;

public class FlipperAgent {

    private static final int DEFAULT_POOL_SIZE = 1;
    private ExecutorService executorService;
    private HandlerExecutor handlerExecutor;
    private MessageListener listener;
    private MessageSender sender;
    private DatagramSocket socket;
    private MessageTypeRegistry registry;
    private FailureDetector failureDetector;
    private MembershipList membershipList;
    private AtomicBoolean shutdownInitiated = new AtomicBoolean(Boolean.FALSE);

    public FlipperAgent(FileConfig config) throws SocketException {
        this.socket = new DatagramSocket(config.getValue(LISTEN_PORT, DEFAULT_LISTEN_PORT));
        Integer corePoolSize = config.getValue(THREAD_POOL_SIZE, DEFAULT_THREAD_POOL_COUNT);
        this.executorService = Executors.newFixedThreadPool(corePoolSize);
        this.sender = new MessageSender(this.socket, this.executorService);
        this.registry = new MessageTypeRegistry(this.sender, config);
        this.handlerExecutor = new MessageHandlerExecutor(executorService, registry);
        this.listener = new MessageListener(this.socket, this.handlerExecutor);
        this.membershipList = new MembershipList(config);
        this.failureDetector = new FailureDetector(Executors.newScheduledThreadPool(corePoolSize), this.membershipList, this.sender, new MessageCreator(config));
    }

    public FlipperAgent() throws SocketException {
        this(new FileConfig());
    }

    public void start() {
        this.listener.beginAccepting();
        this.beginTicks();
    }

    private void beginTicks() {
        //Start a controller thread that will send pings and disseminate info
    }

    public void stop() throws InterruptedException {
        if (shutdownInitiated.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
            this.socket.close();
        }
    }

}
