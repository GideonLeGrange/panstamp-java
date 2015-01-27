package me.legrange.panstamp;

import me.legrange.panstamp.def.DeviceDef;

/**
 *
 * @author gideon
 */
public interface DeviceLibrary {

    public DeviceDef findDevice(int manufacturedID, int productId) throws GatewayException;
        
}
