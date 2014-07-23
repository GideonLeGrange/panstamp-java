package me.legrange.panstamp;

import java.util.List;

/**
 * A PanStamp endpoint 
 * @author gideon
 * @param <T> type of value supported by the endpoint
 */
public interface Endpoint<T> {
    
    String getName();
        
    /** Get the units supported by this endpoint
     * @return The supported units. */
    List<String> getUnits();
    
}
