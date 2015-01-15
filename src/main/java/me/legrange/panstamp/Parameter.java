package me.legrange.panstamp;

/**
 * A parameter that can be set for a panStamp device. 
 * @author gideon
 * @param <T> Type of parameter
 */
public interface Parameter<T> {
    
    public enum Type { NUMBER, INTEGER, STRING, BINARY; };
  
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
     * @return 
     */
    boolean hasValue();

     /* returns the current value of the parameter.
     *
     * @return The value
     * @throws me.legrange.panstamp.impl.NoSuchUnitException
     */
    T getValue() throws GatewayException;
    
    /** returns the default value of the parameter as defined in XML. 
     * 
     * @return The default value.
     */
    T getDefault();

    /**
     * set the value of the parameter
     *
     * @param value The value to set
     * @throws me.legrange.panstamp.GatewayException
     */
    void setValue(T value) throws GatewayException;
    
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
