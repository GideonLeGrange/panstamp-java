package me.legrange.panstamp;

/**
 * Implement this to receive updates when registers change.
 */
public interface RegisterListener {

    /**
     * Receive a notice if a register is updated. 
     * @param reg
     */
    void registerUpdated(Register reg);
    
}
