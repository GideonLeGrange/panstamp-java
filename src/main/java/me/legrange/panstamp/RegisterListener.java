package me.legrange.panstamp;

/**
 * Implement this to receive updates when registers change.
 * @author Gideon le Grange
 */
public interface RegisterListener {

    /**
     * Receive a notice if a register value is updated. 
     * @param ev The event raised.
     */
    void valueReceived(Register reg, byte value[]);
    
    void endpointAdded(Register reg, Endpoint ep);
    
    void parameteradded(Register reg, Parameter par);
}
