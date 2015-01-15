package me.legrange.panstamp;

/**
 * Exception thrown in cases where it is expected to find an endpoint, and it does't exist.
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
