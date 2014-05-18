package me.legrange.panstamp;

/**
 * Super class of exceptions thrown by the gateway 
 * @author gideon
 */
public class GatewayException extends Exception {

    public GatewayException(String msg) {
        super(msg);
    }

    public GatewayException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
