package me.legrange.panstamp;

/**
 * A listener that can be implemented to receive events from the gateway.
 * 
 * @author gideon
 */
public interface GatewayListener {
   
    /** Called on listeners to notify them of gateway events.
     * @param ev The event that occurred. */
    void gatewayUpdated(GatewayEvent ev);
    
}
