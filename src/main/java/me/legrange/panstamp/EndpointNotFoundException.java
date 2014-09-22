package me.legrange.panstamp;

/**
 *
 * @author gideon
 */
public class EndpointNotFoundException extends GatewayException {

    public EndpointNotFoundException(String msg) {
        super(msg);
    }

    public EndpointNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
