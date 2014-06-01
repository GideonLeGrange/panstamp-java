package me.legrange.panstamp.device;

/**
 * A device register
 * @author gideon
 */
public class Register {

    public Register(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    
    private final int id;
    private final String name;
    
}
