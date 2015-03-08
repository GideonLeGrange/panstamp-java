package me.legrange.panstamp;

/**
 * Implement this to receive updates when registers change.
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface RegisterListener {

    /** A value for the register was received from the network. 
     * 
     * @param reg The register for which the value was received. 
     * @param value The value received. 
     */
    void valueReceived(Register reg, byte value[]);
    
    /** A value for the register was set by the application. 
     * 
     * @param reg The register for which the value was set. 
     * @param value The value set. 
     */
    void valueSet(Register reg, byte value[]);
    
    /** An endpoint was added to a register.
     * 
     * @param reg The register involved. 
     * @param ep The endpoint added. 
     */
    void endpointAdded(Register reg, Endpoint ep);
    
    /** A parameter was added to a register. 
     * 
     * @param reg The register involved
     * @param par The endpoint added. 
     */
    void parameterAdded(Register reg, Parameter par);
}
