package me.legrange.panstamp;

/**
 *
 * @author gideon
 */
public interface RegisterEvent {
    
    public enum Type { VALUE_RECEIVED, ENDPOINT_ADDED, PARAMETER_ADDED; }
    
    Type getType();
    
    Register getRegister();
    
    Endpoint getEndpoint();
    
}
