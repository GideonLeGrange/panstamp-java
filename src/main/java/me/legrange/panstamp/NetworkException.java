package me.legrange.panstamp;

/**
 * Super class of exceptions thrown by the gateway 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class NetworkException extends Exception {

    public NetworkException(String msg) {
        super(msg);
    }

    public NetworkException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
