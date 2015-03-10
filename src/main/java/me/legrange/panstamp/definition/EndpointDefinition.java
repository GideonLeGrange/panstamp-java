package me.legrange.panstamp.definition;

import java.util.List;

/**
 * Definition of an endpoint as defined by XML.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public interface EndpointDefinition {
    
    /** Get the register definition to which this endpoint definition applies. 
     * 
     * @return The Register definition.
     */
    public RegisterDefinition getRegister();
    
    /** Get the name of the endpoint. 
     * 
     * @return The endpoint name.
     */
    public String getName();

    /** Get the direction of data flow for this endpoint. 
     * 
     * @return The direction.
     */
    public Direction getDirection();

    /** Get the data type for this endpoint. 
     * 
     * @return The type.
     */
    public Type getType();

    /** Get the position of the endpoint data within the register. 
     * 
     * @return The position object. 
     */
    public Position getPosition();

    /** Get the size of the endpoint data within the register. 
     * 
     * @return The size object. 
     */
    public Size getSize();

    /** Get the data units supported by the endpoint. 
     * 
     * @return The list of units. 
     */
    public List<Unit> getUnits();

}
