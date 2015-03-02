package me.legrange.panstamp;

/**
 * A listener that can be implemented to receive events from the gateway.
 * 
 * @author gideon
 */
public interface GatewayListener {
   
    void deviceDetected(Gateway gw, PanStamp dev);
    
    void deviceRemoved(Gateway gw, PanStamp dev);
    
}
