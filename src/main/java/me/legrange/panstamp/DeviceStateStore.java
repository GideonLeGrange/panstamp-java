package me.legrange.panstamp;

/**
 * Application developers can implement this interface to add their preferred persistence
 * implementation to the library.
 * @author gideon
 */
public interface DeviceStateStore {
    
    /** Check if a stored register value for the given address and standard register is available. 
     * @param address The address of the device for which the value is required.
     * @param reg The register for which the value is required.
     * @return True if a value is available. 
     */
    boolean hasRegisterValue(int address, StandardRegister reg);
    
    /** Return the stored register value for the given address and standard register. 
     * 
     * @param address The address of the device for which the value is required.
     * @param reg The register for which the value is required.
     * @return The value if available or null if no value is known. 
     */
    byte[] getRegisterValue(int address, StandardRegister reg);
    
    /** Store the register value for the given address and standard register. 
     * 
     * @param address The address of the device for which the value is stored.
     * @param reg The register for which the value is stored.
     * @param value The value to be stored.
     */
    void setRegisterValue(int address, StandardRegister reg, byte[] value);
    
}
