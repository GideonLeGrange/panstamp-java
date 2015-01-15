package me.legrange.swap;

/**
 * Implement this to receive incoming SWAP messages.
 * @author gideon
 */
public interface MessageListener {
    
    /** receive message
     * @param msg Message received */
    void messageReceived(SwapMessage msg);
    
    /** receive status message
      * @param msg Message received  */
    void messageSent(SwapMessage msg);
    
}
