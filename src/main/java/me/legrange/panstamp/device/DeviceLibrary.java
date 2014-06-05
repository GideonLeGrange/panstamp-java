package me.legrange.panstamp.device;

/**
 * Library of panStamp devices 
 * @author gideon
 */
public interface DeviceLibrary {
    
    Device findDevice(int manufacturedID, int productId) throws DeviceNotFoundException, ParseException;
    
}
