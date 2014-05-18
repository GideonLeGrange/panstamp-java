package me.legrange.panstamp.at;

import java.math.BigInteger;

/**
 * Utility to convert data 
 * @author gideon
 */
public class Bytes {
    
    /** return a long for the byte array */
    public static long asLong(byte val[]) {
        long l = 0;
        for (int i = 0; i < val.length; ++i) {
            l = (l << 8) | (0xff&val[i]);
        }
        return l;
    }
    
    /** return an array of bytes as hex */
    public static byte[] fromString(String val) {
        return new BigInteger(val).toByteArray();
    }

    public static String asHexString(byte[] val) {
        return new BigInteger(val).toString(16);
    }
    
}
