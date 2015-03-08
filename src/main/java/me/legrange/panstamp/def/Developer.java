package me.legrange.panstamp.def;

/**
 * The object represents the information associated with a panStamp developer
 * in the device XML tree. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class Developer {

    /** Get the developer ID. 
     * 
     * @return The developer ID.
     */
    public int getId() {
        return id;
    }

    /** Get the developer name
     * 
     * @return The developer name.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Developer{" + "id=" + id + ", name=" + name + '}';
    }

    /** Create a new instance with the given id and name */
    Developer(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    private final int id;
    private final String name;
    
}
