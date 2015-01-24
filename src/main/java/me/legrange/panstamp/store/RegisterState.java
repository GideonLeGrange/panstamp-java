package me.legrange.panstamp.store;

import me.legrange.panstamp.impl.StandardRegister;

/**
 * This interface specifies the format in which panStamp state data is exchanged. The idea
 * is to decouple the state exchange totally from any form of implementation, so that state
 * does not require any pre-conceptions to be exchanged.
 * @author gideon
 */
public interface RegisterState {
    
    /** Return the state for the standard register specified. The array of
     * bytes represents the state as register value in exactly the same way that
     * register values defined by Register work. 
     * 
     * @param sr The standard register for which the state is requested.
     * @return  The state as byte[] value. If not state is available for the specifc register, an empty array is returned.
     */
    byte[] getState(StandardRegister sr);
    
}
