package me.legrange.panstamp;

/**
 * Super class of exceptions thrown by the gateway 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class GatewayException extends Exception {

    public GatewayException(String msg) {
        super(msg);
    }

    public GatewayException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
