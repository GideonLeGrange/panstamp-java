package me.legrange.panstamp.core;

/**
 * The position of data relative to a panStamp register definition.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
class Position {

    enum Type {

        BITS, BYTES;
    }

    public Position(int bytePos, int bitPos) {
        this.bytePos = bytePos;
        this.bitPos = bitPos;
        this.type = Type.BITS;
    }

    public Position(int bytePos) {
        this.bytePos = bytePos;
        this.bitPos = 0;
        this.type = Type.BYTES;
    }

    /** Get the byte offset of the position. 
     * 
     * @return The byte offset. 
     */
    public int getBytePos() {
        return bytePos;
    }

    /** Get the bit offset of the position. 
     * 
     * @return The bit offset. 
     */
    public int getBitPos() {
        return bitPos;
    }
    
    /** Get the type of the position: Byte-wise or bit-wise. 
     * 
     * @return The type.
     */
    Type getType() {
        return type;
    }

    private final int bytePos;
    private final int bitPos;
    private final Type type;
}
