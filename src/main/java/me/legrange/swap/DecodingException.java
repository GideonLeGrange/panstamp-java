package me.legrange.swap;

/**
 * Thrown when a packet decoding error occurs.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class DecodingException extends SWAPException {

    public DecodingException(String msg) {
        super(msg);
    }

    public DecodingException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
