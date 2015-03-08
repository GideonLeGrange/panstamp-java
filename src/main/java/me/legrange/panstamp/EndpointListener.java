package me.legrange.panstamp;

/**
 * Listener that receives incoming data from an endpoint
 * @author gideon
 * @param <T> Type of value that will be received
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface EndpointListener<T> {
    
    /** Called on listeners if a new value was received for the endpoint 
     * 
     * @param ep The endpoint for which the new value was received. 
     * @param value The new value.
     */
    void valueReceived(Endpoint<T> ep, T value);
    
    
    
}
