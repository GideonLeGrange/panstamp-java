package me.legrange.panstamp;

/**
 * Thrown when a panStamp device cannot be found in the network. 
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
