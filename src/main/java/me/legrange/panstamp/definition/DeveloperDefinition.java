package me.legrange.panstamp.definition;

/**
 * The object represents the information associated with a panStamp developer
 * in the device XML tree. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public interface DeveloperDefinition {

    /** Get the developer ID. 
     * 
     * @return The developer ID.
     */
    public int getId();
    
    /** Get the developer name
     * 
     * @return The developer name.
     */
    public String getName();
}
