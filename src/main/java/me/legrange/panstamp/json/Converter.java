package me.legrange.panstamp.json;

/**
 * Implement this interface to convert an object to and from JSON
 * @author gideon
 */
public interface Converter<T> {
    
    /** 
     * Convert object to JSON 
     */
    String toJson(T val) throws ConversionException;
    
    /** 
     * Convert from JSON to object. 
     */
    T fromJson(String text) throws ConversionException;
    
}
