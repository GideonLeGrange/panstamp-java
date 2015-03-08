package me.legrange.panstamp;

/**
 * Thrown when a panStamp device cannot be found in the network. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class NodeNotFoundException extends GatewayException {

    public NodeNotFoundException(String msg) {
        super(msg);
    }

    public NodeNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    
}
