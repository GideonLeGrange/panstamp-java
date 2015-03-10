package me.legrange.panstamp.definition;

/**
 * A device parameter definition.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public interface ParameterDefinition {

    /**
     * Get then name of the parameter (as per XML).
     *
     * @return The name.
     */
    public String getName();

    /**
     * Get the data type of the parameter
     *
     * @return The type.
     */
    public Type getType();
    
    /**
     * Get the position of the parameter data within the register.
     *
     * @return The position object.
     */
    public Position getPosition();

    
    /** Get the size of the parameter data within the register. 
     * 
     * @return The size object. 
     */
    public Size getSize();

    /** Get the default value of the parameter as per XML. 
     * 
     * @return The default value. 
     */
    public String getDefault();

    /** Get the verification patter for the parameter as per XML. 
     * 
     * @return A String regular expression pattern. 
     */
    public String getVerif();
}
