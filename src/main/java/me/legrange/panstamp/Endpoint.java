package me.legrange.panstamp;

import java.util.List;

/**
 * A PanStamp endpoint
 *
 * @author gideon
 * @param <T> type of value supported by the endpoint
 */
public interface Endpoint<T> {
    
    public enum Type { NUMBER, INTEGER, STRING, BINARY; };
    
   
    /** Return the name of this endpoint.
     * @return  The endpoint name */
    String getName();

    /** Return the type of the data supported by this endpoint 
     * 
     * @return Type of endpoint 
     */
    Type getType();
    
    /**
     * Get the units supported by this endpoint
     *
     * @return The supported units. This can be empty
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
     * Checks if the endpoint has a value available.
     * @return True if the endpoint has a value 
     */
    boolean hasValue();

    /**
     * returns the current value of the endpoint in the given unit.
     *
     * @param unit Unit in which to receive the value
     * @return The value
     * @throws me.legrange.panstamp.GatewayException
     */
    T getValue(String unit) throws GatewayException;

    /**
     * returns the current value of the endpoint.
     *
     * @return The value 
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a problem retrieving the value 
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
    
    /** 
     * Get the register to which this endpoint is associated. 
     * @return The register containing this endpoint. 
     */
    Register getRegister();

}
