package me.legrange.swap;

/**
 * Thrown when a packet decoding error occurs.
 * @author gideon
 */
public class DecodingException extends SWAPException {

    public DecodingException(String msg) {
        super(msg);
    }

    public DecodingException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
