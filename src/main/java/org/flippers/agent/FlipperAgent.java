package org.flippers.agent;

import org.flippers.config.FileConfig;
import org.flippers.dissemination.EventGenerator;
import org.flippers.dissemination.EventLog;
import org.flippers.handlers.HandlerExecutor;
import org.flippers.handlers.MessageHandlerExecutor;
import org.flippers.handlers.MessageTypeRegistry;
import org.flippers.messages.MessageCreator;
import org.flippers.peers.MembershipList;
import org.flippers.peers.MembershipRegistrar;
import org.flippers.peers.PeerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.flippers.config.Config.DefaultValues.DEFAULT_LISTEN_PORT;
import static org.flippers.config.Config.DefaultValues.DEFAULT_THREAD_POOL_COUNT;
import static org.flippers.config.Config.KeyNames.LISTEN_PORT;
import static org.flippers.config.Config.KeyNames.THREAD_POOL_SIZE;

public class FlipperAgent {

    private final static Logger LOGGER = LoggerFactory.getLogger(FlipperAgent.class);

    private final PeerNode thisNode;
    private ExecutorService executorService;
    private HandlerExecutor handlerExecutor;
    private MessageListener listener;
    private MessageSender sender;
    private DatagramSocket socket;
    private MessageTypeRegistry registry;
    private FailureDetector failureDetector;
    private MembershipList membershipList;
    private EventGenerator eventGenerator;
    private MembershipRegistrar registrar;
    private AtomicBoolean shutdownInitiated = new AtomicBoolean(Boolean.FALSE);

    public FlipperAgent(FileConfig config) throws SocketException, UnknownHostException {
        thisNode = new PeerNode(InetAddress.getLocalHost(), config.getValue(LISTEN_PORT, DEFAULT_LISTEN_PORT));
        MessageCreator messageCreator = new MessageCreator(config, thisNode);
        this.socket = new DatagramSocket(config.getValue(LISTEN_PORT, DEFAULT_LISTEN_PORT));
        Integer corePoolSize = config.getValue(THREAD_POOL_SIZE, DEFAULT_THREAD_POOL_COUNT);
        this.eventGenerator = new EventGenerator(new EventLog(config));
        this.membershipList = new MembershipList(config, this.eventGenerator);
        this.executorService = Executors.newFixedThreadPool(corePoolSize);
        this.sender = new MessageSender(this.socket, this.executorService);
        this.registry = new MessageTypeRegistry(this.sender, config, this.membershipList, messageCreator);
        this.handlerExecutor = new MessageHandlerExecutor(executorService, registry);
        this.listener = new MessageListener(this.socket, this.handlerExecutor);
        this.registrar = new MembershipRegistrar(config, sender, messageCreator);
        this.failureDetector = new FailureDetector(Executors.newScheduledThreadPool(corePoolSize), this.membershipList, this.sender, messageCreator, config);
    }

    public FlipperAgent() throws SocketException, UnknownHostException {
        this(new FileConfig());
    }

    public void start() {
        LOGGER.debug("Starting Flipper agent");
        this.listener.beginAccepting();
        this.registrar.initiateRegistration();
        this.failureDetector.startDetection();
    }

    public void stop() {
        if (shutdownInitiated.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
            this.socket.close();
        }
    }

}
