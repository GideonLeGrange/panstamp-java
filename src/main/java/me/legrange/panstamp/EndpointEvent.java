package me.legrange.panstamp;

/**
 *
 * @author gideon
 */
public interface EndpointEvent<T> {
    
    public enum Type { VALUE_RECEIVED; }
    
    public Type getType();
    
    public Endpoint<T> getEndpoint();
}
