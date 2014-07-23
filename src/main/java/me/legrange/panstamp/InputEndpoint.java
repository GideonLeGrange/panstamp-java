package me.legrange.panstamp;

import java.util.List;
import me.legrange.panstamp.impl.NoSuchUnitException;

/**
 *
 * @author gideon
 * @param <T> Type of data received from the endpoint
 */
public interface InputEndpoint<T> extends Endpoint<T> {

    /**
     * Add a listener to the endpoint. 
     * @param unit Unit in which the data must be received.
     * @param el Listener that will receive incoming data
     * @throws me.legrange.panstamp.impl.NoSuchUnitException
     */
    void addListener(String unit, EndpointListener<T> el) throws NoSuchUnitException;
    
    /** returns the current value of the endpoint in the given unit.
     * @param unit Unit in which to receive the value
     * @return The value
     * @throws me.legrange.panstamp.impl.NoSuchUnitException  */
    T getValue(String unit) throws GatewayException;
    
}
