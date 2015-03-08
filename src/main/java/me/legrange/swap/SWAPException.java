package me.legrange.swap;

/**
 * Super class of exceptions thrown by the gateway 
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class SWAPException extends Exception {

    public SWAPException(String msg) {
        super(msg);
    }

    public SWAPException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
