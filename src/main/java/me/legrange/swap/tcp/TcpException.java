package me.legrange.swap.tcp;

import me.legrange.swap.SWAPException;

/**
 * Thrown by the TcpModem if there is a problem.
 * @author gideon
 */
public class TcpException extends SWAPException {

    /** Instantiate exception 
     * 
     * @param msg Message to use
     * @param cause Causing exception
     */
    public TcpException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
