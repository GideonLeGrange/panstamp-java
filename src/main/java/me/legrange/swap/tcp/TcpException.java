package me.legrange.swap.tcp;

import me.legrange.swap.SWAPException;

/**
 * Thrown by the TcpModem if there is a problem.
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class TcpException extends SWAPException {

    /** Instantiate exception 
     * 
     * @param msg Message to use
     * @param cause Causing exception
     */
    TcpException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
