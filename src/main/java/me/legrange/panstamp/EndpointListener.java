package me.legrange.panstamp;

/**
 * Listener that receives incoming data from an endpoint
 * @author gideon
 * @param <T> Type of value that will be received
 */
public interface EndpointListener<T> {
    
    /** Called on listeners if a new value was received for the endpoint */
    void valueReceived(Endpoint<T> ep, T value);
    
}
