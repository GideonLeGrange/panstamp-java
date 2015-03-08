package me.legrange.swap;

/**
 * Implement this to receive incoming SWAP messages.
  *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public interface MessageListener {
    
    /** receive message
     * @param msg Message received */
    void messageReceived(SwapMessage msg);
    
    /** receive status message
      * @param msg Message received  */
    void messageSent(SwapMessage msg);
    
}
