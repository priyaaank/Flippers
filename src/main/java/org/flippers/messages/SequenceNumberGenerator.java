package org.flippers.messages;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public class SequenceNumberGenerator {

    public static final char NEGATIVE_SIGN = '-';
    public static final char CHAR_REPLACEMENT = '1';

    public String uniqSequence() {
        long lsb = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        long msb = Math.abs(UUID.randomUUID().getMostSignificantBits());
        byte[] bytes = ByteBuffer.allocate(16).putLong(msb).putLong(lsb).array();
        BigInteger big = new BigInteger(bytes);
        return big.toString().replace(NEGATIVE_SIGN, CHAR_REPLACEMENT);
    }

}
