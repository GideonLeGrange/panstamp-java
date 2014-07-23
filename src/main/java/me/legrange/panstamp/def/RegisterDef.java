package me.legrange.panstamp.def;

/**
 * A device register
 * @author gideon
 */
public class RegisterDef {

    public RegisterDef(int id, String name) {
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
