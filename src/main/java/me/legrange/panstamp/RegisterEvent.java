package me.legrange.panstamp;

/**
 * Event used to notify listeners of events on a register. 
 * 

 * @author gideon
 */
public interface RegisterEvent {
    
    /** The types of register events */
    public enum Type { VALUE_RECEIVED, ENDPOINT_ADDED, PARAMETER_ADDED; }
    
    /** Get the type of the specific event
     * @return The type of the event.
     */
    Type getType();
    
    /** Get the register which raised this event
     * 
     * @return The register.
     */
    Register getRegister();
    
    /** get the endpoint associated with this event, if applicable.
     * 
     * @return The endpoint. Can be null 
     */
    Endpoint getEndpoint();
    
}
