package me.legrange.panstamp.def;

/**
 *  The position of data relative to a panStamp register
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

    public int getBytePos() {
        return bytePos;
    }

    public int getBitPos() {
        return bitPos;
    }

    public Type getType() {
        return type;
    }
    
    
    
    private final int bytePos;
    private final int bitPos;
    private final Type type;
}
