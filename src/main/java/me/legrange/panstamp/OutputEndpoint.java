package me.legrange.panstamp;

/**
 *
 * @author gideon
 * @param <T> Type of data sent to the endpoint
 */
public interface OutputEndpoint<T> extends Endpoint<T> {
    
    /** set the value of the endpoint
     * @param unit The unit in which the value is passed.
     * @param value The value to set */ 
    void setValue(String unit, T value);
    
}
