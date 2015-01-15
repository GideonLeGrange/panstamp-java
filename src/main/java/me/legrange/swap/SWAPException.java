package me.legrange.swap;

/**
 * Super class of exceptions thrown by the gateway 
 * @author gideon
 */
public class SWAPException extends Exception {

    public SWAPException(String msg) {
        super(msg);
    }

    public SWAPException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
