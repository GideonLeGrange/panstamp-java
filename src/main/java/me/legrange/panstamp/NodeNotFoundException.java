package me.legrange.panstamp;

/**
 * Thrown when a SWAP mote cannot be found 
 * @author gideon
 */
public class NodeNotFoundException extends GatewayException {

    public NodeNotFoundException(String msg) {
        super(msg);
    }

    public NodeNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    
}
