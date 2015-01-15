package me.legrange.panstamp;

/**
 * Listener that receives incoming data from an endpoint
 * @author gideon
 * @param <T> Type of value that will be received
 */
public interface EndpointListener<T> {
    
   /** Called on listeners to notify them of endpoint events.
     * @param ev The event.
    */
    void endpointUpdated(EndpointEvent<T> ev);
    
}
