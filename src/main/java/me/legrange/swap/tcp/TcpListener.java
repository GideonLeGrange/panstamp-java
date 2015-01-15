package me.legrange.swap.tcp;

import me.legrange.swap.ModemSetup;
import me.legrange.swap.SwapMessage;

/**
 * Implement this interface to receive messages from TcpTransport. 
 * @author gideon
 */
interface TcpListener {
    
    /** A SWAP message was received */
    void messgeReceived(SwapMessage msg);
    
    /** A setup message was received */
    void setupReceived(ModemSetup setup);
    
}
