package me.legrange.panstamp;

/**
 * A listener that can be implemented to receive events from the gateway.
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface NetworkListener {
   
    /** A previously unknown device was detected on the SWAP network. 
     * 
     * @param gw The gateway involved.
     * @param dev The device detected.
     */
    void deviceDetected(Network gw, PanStamp dev);

    /** A device was removed from the network. 
     * 
     * @param gw The gateway involved. 
     * @param dev The device removed. 
     */
    void deviceRemoved(Network gw, PanStamp dev);
    
    /** The network was opened. 
     * 
     * @param nw The network.
     * @see 1.2
     */
    void networkOpened(Network nw);
    
    /** The network was closed. 
     * 
     * @param nw The network.
     * @see 1.2
     */
    void networkClosed(Network nw);
    
}
