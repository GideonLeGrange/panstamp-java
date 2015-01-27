package me.legrange.panstamp;

import me.legrange.panstamp.def.DeviceDef;
import me.legrange.panstamp.def.DeviceNotFoundException;

/**
 * A device library can be implemented to find a device definition based on a panStamp's
 * manufacturer ID and product ID.  
 * 
 * The device definitions are found in XML files, and while application developers 
 * will typically not implement this interface, it can be implemented to define 
 * custom ways of loading XML files. 
 * 
 * @author gideon
 */
public interface DeviceLibrary {
    
    /** Checks if a device definition for the supplied manufacturer ID and product ID is available. 
     * 
     * @param manufacturedID Manufacturer ID for device.
     * @param productId Product ID for device. 
     * @return True if the definition is available. 
     * @throws GatewayException If there is a problem loading a device definition. 
     */
    boolean hasDeviceDefinition(int manufacturedID, int productId) throws GatewayException;

    /** get the device definition based on the supplied manufacturer ID and product ID. 
     * 
     * @param manufacturedID Manufacturer ID for device.
     * @param productId Product ID for device. 
     * @return The device definition. 
     * @throws GatewayException If there is a problem loading a device definition. 
     */
    DeviceDef getDeviceDefinition(int manufacturedID, int productId) throws GatewayException;
        
}
