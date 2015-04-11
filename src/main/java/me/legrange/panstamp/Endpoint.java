package me.legrange.panstamp;

import java.util.List;

/**
 * A PanStamp endpoint
 *
 * @param <T> type of value supported by the endpoint
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface Endpoint<T> {
    
    /** Type of value sent and/or received by an endpoint. */
    public enum Type { 
        /** Numbers (represented as double values */ NUMBER, 
        /** Integer numbers */ INTEGER, 
        /** Text values */ STRING, 
        /** Boolean / On-off / True-false values */ BINARY; };
    
   
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
     * Return the unit currently used for converting the endpoint values. This is
     * the unit set by calling getUnit() or the default unit to use if no unit is set. The default
     * unit is the first unit in the device definition, if units are defined, or an empty string. 
     * 
     * @return The unit used. 
     */
    String getUnit();
    
    /** 
     * Set the unit to use for converting endpoint values.
     * @see getUnit() for more information. 
     * 
     * @param unit The unit to use
     * @throws NoSuchUnitException Thrown if the unit specified does not exist.  
     */
    void setUnit(String unit) throws NoSuchUnitException;

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
     * @throws me.legrange.panstamp.NetworkException Thrown if there is a problem retrieving and endpoint value.
     */
    T getValue(String unit) throws NetworkException;

    /**
     * returns the current value of the endpoint.
     *
     * @return The value 
     * @throws me.legrange.panstamp.NetworkException Thrown if there is a problem retrieving and endpoint value.
     */
    T getValue() throws NetworkException;

    /**
     * set the value of the endpoint
     *
     * @param unit The unit in which the value is passed.
     * @param value The value to set
     * @throws me.legrange.panstamp.NetworkException Thrown if there is a problem setting the endpoint value.
     */
    void setValue(String unit, T value) throws NetworkException;

    /**
     * set the value of the endpoint
     *
     * @param value The value to set
     * @throws me.legrange.panstamp.NetworkException Thrown if there is a problem setting the endpoint value.
     */
    void setValue(T value) throws NetworkException;
    
    /** 
     * Get the register to which this endpoint is associated. 
     * @return The register containing this endpoint. 
     */
    Register getRegister();

}
