
package me.legrange.panstamp.def;

/**
 * The size of a data point (endpoint or parameter). 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class Size {
    
    public enum Type { BITS, BYTES; }

    public Size(int sizeByte) { 
        this(sizeByte, 0);
    }
    
    public Size(int sizeByte, int sizeBit) {
        if (sizeBit == 0) {
            type = Type.BYTES;
        }
        else {
            type = Type.BITS;
        }
        this.sizeByte = sizeByte;
        this.sizeBit = sizeBit;
    }
    
    /** Get the type of this size (is it in bytes or bits). 
     * 
     * @return The type. 
     */
    public Type getType() {
        return type;
    }
    
    /** Get the number of bytes defined by this size.
     * 
     * @return The size in bytes. 
     */
    public int getBytes() {
        return sizeByte;
    }
    
    /** Get the number of bits defined by this size.
     * 
     * @return The size in bits. 
     */
    public int getBits() {
        return sizeBit;
    }

    private final int sizeByte;
    private final int sizeBit;
    private final Type type;
}
