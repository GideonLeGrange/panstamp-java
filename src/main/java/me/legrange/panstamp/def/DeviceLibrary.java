package me.legrange.panstamp.def;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.legrange.panstamp.PanStamp;

/**
 * A device library implementation is used to find and load panStamp XML device definitions. 
 * @author gideon
 */
public abstract class DeviceLibrary {      
    
    public Device findDevice(int manufacturedID, int productId) throws DeviceNotFoundException, ParseException {
        if (devices == null) {
            devices = new HashMap<>();
            List<Device> all = XMLParser.parse(this);
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
    
    /** implement to supply implementation specific way of finding the input stream for 
     * a given path name. 
     * @param path Path name of the XML file.
     * @return An input stream for the XML file, or null if it could not be found.
     */
    protected abstract InputStream getStream(String path) ;
    
    private String makeId(int manufacturedID, int productId) {
        return String.format("%d/%d", manufacturedID, productId);
    }
    
    private Map<String,Device> devices;
    
}
