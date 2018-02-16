package org.flippers.agent;

import org.flippers.config.Config;
import org.flippers.messages.DataMessage;
import org.flippers.messages.MessageCreator;
import org.flippers.peers.MembershipList;
import org.flippers.peers.PeerNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import static org.flippers.config.Config.DefaultValues.DEFAULT_FAILURE_DETECTION_DELAY_PERIOD;
import static org.flippers.config.Config.DefaultValues.DEFAULT_FAILURE_DETECTION_INITIAL_DELAY;
import static org.flippers.config.Config.DefaultValues.DEFAULT_RANDOM_NODE_SELECTION_COUNT;
import static org.flippers.config.Config.KeyNames.FAILURE_DETECTION_DELAY_PERIOD;
import static org.flippers.config.Config.KeyNames.FAILURE_DETECTION_INITIAL_DELAY;
import static org.flippers.config.Config.KeyNames.RANDOM_NODE_SELECTION_COUNT;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FailureDetectorTest {

    public static final int NODE_COUNT = 5;
    private FailureDetector failureDetector;
    private List<PeerNode> selectedNodes;

    @Mock
    private ScheduledExecutorService executorService;

    @Mock
    private MembershipList membershipList;

    @Mock
    private MessageSender sender;

    @Mock
    private MessageCreator messageCreator;

    @Mock
    private Config config;

    @Before
    public void setUp() throws Exception {
        when(config.getValue(FAILURE_DETECTION_INITIAL_DELAY, DEFAULT_FAILURE_DETECTION_INITIAL_DELAY)).thenReturn(1000);
        when(config.getValue(FAILURE_DETECTION_DELAY_PERIOD, DEFAULT_FAILURE_DETECTION_DELAY_PERIOD)).thenReturn(1000);
        when(config.getValue(RANDOM_NODE_SELECTION_COUNT, DEFAULT_RANDOM_NODE_SELECTION_COUNT)).thenReturn(5);

        this.failureDetector = new FailureDetector(executorService, membershipList, sender, messageCreator, config);
        this.selectedNodes = selectMockNodes();
        when(membershipList.selectNodesRandomly(NODE_COUNT)).thenReturn(selectedNodes);
    }

    @Test
    public void shouldInitiatePingForRandomlySelectedNodes() {
        this.failureDetector.run();

        for (PeerNode peerNode : selectedNodes) {
            verify(peerNode).pingInitiated();
        }

        verify(sender, times(NODE_COUNT)).send(any(DataMessage.class));
    }

    private List<PeerNode> selectMockNodes() {
        return new ArrayList<PeerNode>() {{
            add(mock(PeerNode.class));
            add(mock(PeerNode.class));
            add(mock(PeerNode.class));
            add(mock(PeerNode.class));
            add(mock(PeerNode.class));
        }};
    }
}