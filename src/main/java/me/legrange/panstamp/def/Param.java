package me.legrange.panstamp.def;

/**
 * A device param 
 * @author gideon
 */
public class Param {
    
     Param(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    void setPosition(Position position) {
        this.position = position;
    }

    public Size getSize() {
        return size;
    }

     void setSize(Size size) {
        this.size = size;
    }

    public String getDefault() {
        return def;
    }

     void setDefault(String def) {
        this.def = def;
    }

    public String getVerif() {
        return verif;
    }

     void setVerif(String verif) {
        this.verif = verif;
    }
    
    

    private final String name;
    private final Type type;
    private Position position;
    private Size size;
    private String def;
    private String verif;
}
