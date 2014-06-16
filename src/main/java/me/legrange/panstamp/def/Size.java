
package me.legrange.panstamp.def;

/**
 * The size of a data point 
 * @author gideon
 */
public class Size {
    
    public enum Type { BITS, BYTES; }

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
    

    private final int sizeByte;
    private final int sizeBit;
    private final Type type;
}
