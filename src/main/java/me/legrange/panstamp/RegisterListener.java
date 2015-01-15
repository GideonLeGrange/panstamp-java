package me.legrange.panstamp;

/**
 * Implement this to receive updates when registers change.
 * @author Gideon le Grange
 */
public interface RegisterListener {

    /**
     * Receive a notice if a register is updated. 
     * @param ev The event raised.
     */
    void registerUpdated(RegisterEvent ev);
    
}
