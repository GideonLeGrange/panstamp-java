package me.legrange.swap;

/**
 * Super class of exceptions thrown by the gateway 
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class SwapException extends Exception {

    public SwapException(String msg) {
        super(msg);
    }

    public SwapException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
