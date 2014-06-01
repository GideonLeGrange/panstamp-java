
package me.legrange.panstamp.device;

/**
 * 
 * @author gideon
 */
public class Position {
    
    enum Type { BITS, BYTES; }
    
    
    public Position(int bytePos, int bitPos) {
        this.bytePos = bytePos;
        this.bitPos = bitPos;
        this.type = Type.BITS;
    }

    public Position(int bytePos) {
        this.bytePos = bytePos;
        this.bitPos =  0;
        this.type = Type.BYTES;
    }
    
    private final int bytePos;
    private final int bitPos;
    private final Type type;
}
