package me.legrange.panstamp.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A device library implementation is used to find and load panStamp XML device definitions. 
 * @author gideon
 */
public abstract class DeviceLibrary {                                   

    public Device findDevice(int manufacturedID, int productId) throws DeviceNotFoundException, ParseException {
        if (devices == null) {
            devices = new HashMap<>();
            List<Device> all = XMLParser.parse(getSource());
            for (Device dev : all) {
                devices.put(makeId(dev.getDeveloper().getId(), dev.getId()), dev);
            }
        }
        Device dev = devices.get(makeId(manufacturedID, productId));
        if (dev == null) {
            throw new DeviceNotFoundException(String.format("Could not find device definition for manufacturer/product %d/%d", manufacturedID, productId));
        }
        return dev;
    }
    
    protected abstract StreamSource getSource();
    
    private String makeId(int manufacturedID, int productId) {
        return String.format("%d/%d", manufacturedID, productId);
    }
    
    private Map<String,Device> devices;
    
}
