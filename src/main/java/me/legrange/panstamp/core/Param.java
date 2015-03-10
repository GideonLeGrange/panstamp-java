package me.legrange.panstamp.core;

/**
 * A device parameter definition.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class Param {

    /**
     * Get then name of the parameter (as per XML).
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the data type of the parameter
     *
     * @return The type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the position of the parameter data within the register.
     *
     * @return The position object.
     */
    public Position getPosition() {
        return position;
    }

    void setPosition(Position position) {
        this.position = position;
    }
    
    /** Get the size of the parameter data within the register. 
     * 
     * @return The size object. 
     */
    public Size getSize() {
        return size;
    }

    void setSize(Size size) {
        this.size = size;
    }

    /** Get the default value of the parameter as per XML. 
     * 
     * @return The default value. 
     */
    public String getDefault() {
        return def;
    }

    void setDefault(String def) {
        this.def = def;
    }

    /** Get the verification patter for the parameter as per XML. 
     * 
     * @return A String regular expression pattern. 
     */
    public String getVerif() {
        return verif;
    }

    void setVerif(String verif) {
        this.verif = verif;
    }

    Param(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    private final String name;
    private final Type type;
    private Position position;
    private Size size;
    private String def;
    private String verif;
}
