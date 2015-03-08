package me.legrange.panstamp;

/**
 * Exception thrown in cases where it is expected to find an endpoint, and it
 * doesn't exist.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class EndpointNotFoundException extends GatewayException {

    public EndpointNotFoundException(String msg) {
        super(msg);
    }

    public EndpointNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
