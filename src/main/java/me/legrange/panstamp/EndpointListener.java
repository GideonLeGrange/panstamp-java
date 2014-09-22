package me.legrange.panstamp;

/**
 * Listener that receives incoming data from an endpoint
 * @author gideon
 * @param <T> Type of value that will be received
 */
public interface EndpointListener<T> {
    
    void endpointUpdated(EndpointEvent<T> ep);
    
}
