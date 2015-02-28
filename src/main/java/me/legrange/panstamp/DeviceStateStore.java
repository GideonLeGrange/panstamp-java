package me.legrange.panstamp;

/**
 * Application developers can implement this interface to add their preferred persistence
 * implementation to the library.
 * @author gideon
 */
public interface DeviceStateStore {
    
     /** Check if a stored endpoint value for the given address and standard endpoint is available. 
     * @param address The address of the device for which the value is required.
     * @param ep The endpoint for which the value is required.
     * @return True if a value is available. 
     */
    boolean hasEndpointValue(int address, StandardEndpoint ep);
    
        /** Return the stored register value for the given address and standard register. 
     * 
     * @param address The address of the device for which the value is required.
     * @param ep The endpoint for which the value is required.
     * @return The value if available or null if no value is known. 
     */
    int getEndpointValue(int address, StandardEndpoint ep);
    
        
    /** Store the endpoint value for the given address and standard endpoint. 
     * 
     * @param address The address of the device for which the value is stored.
     * @param ep The endpoint for which the value is stored.
     * @param value The value to be stored.
     */
    void setEndpointValue(int address, StandardEndpoint ep, int value);
    
    
}
