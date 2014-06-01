package me.legrange.panstamp.device;

import java.util.List;

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

    public String getDef() {
        return def;
    }

     void setDef(String def) {
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
