package me.legrange.panstamp;

/**
 * Implement this to receive updates when registers change.
 * @author Gideon le Grange
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
    
    void endpointAdded(Register reg, Endpoint ep);
    
    void parameteradded(Register reg, Parameter par);
}
