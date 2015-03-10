package me.legrange.panstamp.definition;

/**
 * A unit definition for endpoint data. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class Unit {

    public Unit(String name, double factor, double offset) {
        this.name = name;
        this.factor = factor;
        this.offset = offset;
    }

    /** The name of the unit. 
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /** The multiplication factor for the data. 
     * 
     * @return The factor. 
     */
    public double getFactor() {
        return factor;
    }

    /** The offset constant for the data. 
     * 
     * @return The offset. 
     */
    public double getOffset() {
        return offset;
    }
    
    
    private final String name;
    private final double factor;
    private final double offset;
            
    
}
