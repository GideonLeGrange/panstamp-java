package me.legrange.panstamp;

import java.util.List;

/**
 * A PanStamp endpoint
 *
 * @author gideon
 * @param <T> type of value supported by the endpoint
 */
public interface Endpoint<T> {

    /** Return the name of this endpoint.
     * @return  The endpoint name */
    String getName();

    /**
     * Get the units supported by this endpoint
     *
     * @return The supported units.
     */
    List<String> getUnits();

    /**
     * Add a listener to the endpoint.
     *
     * @param el Listener that will receive incoming data
     */
    void addListener(EndpointListener<T> el);
    
    /** Remove a listener from the endpoint.
     * 
     * @param el The listener to remove
     */
    void removeListener(EndpointListener<T> el);

    /**
     * returns the current value of the endpoint in the given unit.
     *
     * @param unit Unit in which to receive the value
     * @return The value
     * @throws me.legrange.panstamp.impl.NoSuchUnitException
     */
    T getValue(String unit) throws GatewayException;

    /**
     * returns the current value of the endpoint.
     *
     * @return The value
     * @throws me.legrange.panstamp.impl.NoSuchUnitException
     */
    T getValue() throws GatewayException;

    /**
     * set the value of the endpoint
     *
     * @param unit The unit in which the value is passed.
     * @param value The value to set
     * @throws me.legrange.panstamp.GatewayException
     */
    void setValue(String unit, T value) throws GatewayException;

    /**
     * set the value of the endpoint
     *
     * @param value The value to set
     * @throws me.legrange.panstamp.GatewayException
     */
    void setValue(T value) throws GatewayException;

}
