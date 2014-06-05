package me.legrange.panstamp.device;

import java.util.List;
import java.util.Map;

/**
 * A device library implementation for loading device definitions from the file system.
 * @author gideon
 */
public class FileLibrary implements DeviceLibrary {
    
    FileLibrary(String path) {
        this.path = path;
    }
    
    @Override
    public Device findDevice(int manufacturedID, int productId) throws DeviceNotFoundException, ParseException {
        if (devices == null) {
            List<Device> all = XMLParser.parse(path);
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
    
    private String makeId(int manufacturedID, int productId) {
        return String.format("%d/%d", manufacturedID, productId);
    }
    
    private final String path;
    private Map<String,Device> devices;
    
}
