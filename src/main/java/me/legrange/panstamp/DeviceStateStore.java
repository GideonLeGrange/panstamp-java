package me.legrange.panstamp;

/**
 * Application developers can implement this interface to add their preferred persistence
 * implementation to the library.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface DeviceStateStore {
    
     /** Check if a stored register value for the given register is available. 
     * @param reg The register for which the value is required.
     * @return True if a value is available. 
     */
    boolean hasRegisterValue(Register reg);
      
    /** Return the stored register value for the given register. 
     * 
     * @param reg The register for which the value is required.
     * @return The value if available or null if no value is known. 
     */
    byte[] getRegisterValue(Register reg);
    
        
    /** Store the register value for the register.
     * 
     * @param reg The register for which the value is to be stored. 
     * @param ep The endpoint for which the value is stored.
     * @param value The value to be stored.
     */
    void setRegisterValue(Register reg, byte[] value);
    
    
}
