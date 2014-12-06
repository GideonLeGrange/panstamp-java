package me.legrange.panstamp;

/**
 *
 * @author gideon
 * @param <T> Type of parameter
 */
public interface Parameter<T> {
    
    public enum Type { NUMBER, STRING, BINARY; };
  
    String getName();
    
    Type getType();
    
    boolean hasValue();

    /**
     * returns the current value of the parameter in the given unit.
     *
     * @param unit Unit in which to receive the value
     * @return The value
     * @throws me.legrange.panstamp.impl.NoSuchUnitException
     */
    T getValue(String unit) throws GatewayException;

    /**
     * returns the current value of the parameter.
     *
     * @return The value
     * @throws me.legrange.panstamp.impl.NoSuchUnitException
     */
    T getValue() throws GatewayException;

    /**
     * set the value of the parameter
     *
     * @param value The value to set
     * @throws me.legrange.panstamp.GatewayException
     */
    void setValue(T value) throws GatewayException;
    
    Register getRegister();
    
}
