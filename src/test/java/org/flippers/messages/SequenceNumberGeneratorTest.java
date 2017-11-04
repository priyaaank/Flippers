package org.flippers.messages;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SequenceNumberGeneratorTest {

    private SequenceNumberGenerator sequenceNumberGenerator;

    @Before
    public void setUp() throws Exception {
        this.sequenceNumberGenerator = new SequenceNumberGenerator();
    }

    @Test
    public void shouldGenerateAUuidThatIsString() throws Exception {
        assertThat(sequenceNumberGenerator.uniqSequence().getClass().getName(), is(String.class));
    }

    @Test
    public void shouldGenerateAUuidThatIs38DigitsInSize() throws Exception {
        assertTrue(sequenceNumberGenerator.uniqSequence().length() > 30);
    }

}