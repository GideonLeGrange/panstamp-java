package me.legrange.panstamp.xml;

import me.legrange.panstamp.definition.DeveloperDefinition;

/**
 * The object represents the information associated with a panStamp developer
 * in the device XML tree. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class XMLDeveloperDefinition implements DeveloperDefinition {

 
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Developer{" + "id=" + id + ", name=" + name + '}';
    }

    /** Create a new instance with the given id and name */
    XMLDeveloperDefinition(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    private final int id;
    private final String name;
    
}
