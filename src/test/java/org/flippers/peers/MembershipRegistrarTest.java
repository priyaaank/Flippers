package org.flippers.peers;

import org.flippers.agent.MessageSender;
import org.flippers.config.Config;
import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageCreator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.flippers.config.Config.DefaultValues.DEFAULT_LISTEN_PORT;
import static org.flippers.config.Config.DefaultValues.DEFAULT_SEED_NODES;
import static org.flippers.config.Config.KeyNames.LISTEN_PORT;
import static org.flippers.config.Config.KeyNames.SEED_NODES;
import static org.flippers.messages.MessageType.JOIN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MembershipRegistrarTest {

    @Mock
    private Config config;

    @Mock
    private MessageSender sender;

    @Captor
    private ArgumentCaptor<DataMessage> messageCaptor;

    private MessageCreator messageCreator;

    private MembershipRegistrar registrar;

    private PeerNode testNode;

    @Before
    public void setUp() throws UnknownHostException {
        when(config.getValues(SEED_NODES, DEFAULT_SEED_NODES)).thenReturn(new String[]{"127.0.0.1:8081"});
        when(config.getValue(LISTEN_PORT, DEFAULT_LISTEN_PORT)).thenReturn(DEFAULT_LISTEN_PORT);
        this.testNode = new PeerNode(InetAddress.getLocalHost(), DEFAULT_LISTEN_PORT);
        this.messageCreator = new MessageCreator(config, testNode);
        this.registrar = new MembershipRegistrar(config, sender, messageCreator);
    }

    @Test
    public void shouldInitiateMembershipRegistrationWithConfiguredSeedNodes() throws UnknownHostException {
        registrar.initiateRegistration();

        verify(sender).send(messageCaptor.capture());
        DataMessage value = messageCaptor.getValue();

        assertThat(value.getMessageType(), is(JOIN));
        assertThat(value.getSourceNode(), is(this.testNode));
        assertThat(value.getDestinationNode().getPort(), is(8081));
    }

    @Test
    public void shouldInitiateMembershipRegistrationWithAllSeedNodes() throws UnknownHostException {
        String[] seedNodes = {"127.0.0.1:8081", "127.0.0.1:8888"};
        when(config.getValues(SEED_NODES, DEFAULT_SEED_NODES)).thenReturn(seedNodes);

        registrar.initiateRegistration();

        verify(sender, times(2)).send(any(DataMessage.class));
    }

    @Test
    public void shouldNotSendMessageToNodesWhereConfigurationDetailsArsMissing() throws UnknownHostException {
        String[] seedNodes = {"127.0.0.1:8081", "127.0.0.1"};
        when(config.getValues(SEED_NODES, DEFAULT_SEED_NODES)).thenReturn(seedNodes);

        registrar.initiateRegistration();

        verify(sender, times(1)).send(any(DataMessage.class));
    }

}