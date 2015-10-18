package me.legrange.panstamp;

/**
 * A parameter that can be set for a panStamp device. 
 * @param <T> Type of parameter
 *  
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface Parameter<T> extends Comparable<Parameter<T>>{
    
      /** Type of value sent and/or received by an endpoint. */
    public enum Type { 
        /** Numbers (represented as double values */ NUMBER, 
        /** Integer numbers */ INTEGER, 
        /** Text values */ STRING, 
        /** Boolean / On-off / True-false values */ BINARY,
        /** byte arrays */ BYTE_ARRAY; };
    
    /** Return the name of the parameter (as defined by XML)
     * 
     * @return The name of the parameter
     */
    String getName();
    
    /** Return the type of the parameter.
     * 
     * @return The type of the parameter
     */
    Type getType();
    
    /** Returns true if the parameter has a known value 
     * 
     * @return True if a value is known.
     */
    boolean hasValue();

     /* returns the current value of the parameter.
     *
     * @return The value
     * @throws me.legrange.panstamp.impl.NoSuchUnitException
     */
    T getValue() throws NetworkException;
    
    /** returns the default value of the parameter as defined in XML. 
     * 
     * @return The default value.
     */
    T getDefault();

    /**
     * set the value of the parameter
     *
     * @param value The value to set
     * @throws NetworkException Thrown if there is a problem setting the parameter value.
     */
    void setValue(T value) throws NetworkException;
    
    /** Return the verification pattern for this parameter.
     * 
     * @return The pattern regex. 
     */
    String getPattern();
    
    /** Returns the register associated with this parameter.
     * 
     * @return The register 
     */
    Register getRegister();
    
}
