package me.legrange.panstamp.device;

/**
 * An endpoint unit
 * @author gideon
 */
public class Unit {

    public Unit(String name, double factor, double offset) {
        this.name = name;
        this.factor = factor;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public double getFactor() {
        return factor;
    }

    public double getOffset() {
        return offset;
    }
    
    
    private final String name;
    private final double factor;
    private final double offset;
            
    
}
