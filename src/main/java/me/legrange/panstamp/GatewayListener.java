package me.legrange.panstamp;

/**
 * A listener that can be implemented to receive events from the gateway.
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface GatewayListener {
   
    /** A previously unknown device was detected on the SWAP network. 
     * 
     * @param gw The gateway involved.
     * @param dev The device detected.
     */
    void deviceDetected(Gateway gw, PanStamp dev);

    /** A device was removed from the network. 
     * 
     * @param gw The gateway involved. 
     * @param dev The device removed. 
     */
    void deviceRemoved(Gateway gw, PanStamp dev);
    
}
