package me.legrange.panstamp.xml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.legrange.panstamp.DeviceLibrary;
import me.legrange.panstamp.NetworkException;
import me.legrange.panstamp.DeviceNotFoundException;
import me.legrange.panstamp.definition.DeviceDefinition;

/**
 * A device library implementation is used to find and load panStamp XML device
 * definitions.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
abstract class XmlDeviceLibrary implements DeviceLibrary {

    @Override
    public boolean hasDeviceDefinition(int manufacturedID, int productId) throws NetworkException {
        return getDefinition(manufacturedID, productId) != null;
    }

    @Override
    public DeviceDefinition getDeviceDefinition(int manufacturedID, int productId) throws NetworkException {
        XmlDeviceDefinition def = getDefinition(manufacturedID, productId);
        if (def == null) {
            throw new DeviceNotFoundException(String.format("Could not find device definition for manufacturer/product %d/%d", manufacturedID, productId));
        }
        return def;
    }

    /**
     * implement this to supply an application specific way of finding the input
     * stream for a given path name.
     *
     * @param path Path name of the XML file.
     * @return An input stream for the XML file, or null if it could not be
     * found.
     */
    abstract InputStream getStream(String path);

    private synchronized XmlDeviceDefinition getDefinition(int manufacturedID, int productId) throws DeviceNotFoundException, ParseException {
        if (devices == null) {
            devices = new HashMap<>();
            List<XmlDeviceDefinition> all = XmlParser.parse(this);
            for (XmlDeviceDefinition dev : all) {
                devices.put(makeId(dev.getDeveloper().getId(), dev.getId()), dev);
            }
        }
        return devices.get(makeId(manufacturedID, productId));
    }

    private String makeId(int manufacturedID, int productId) {
        return String.format("%d/%d", manufacturedID, productId);
    }

    private Map<String, XmlDeviceDefinition> devices;

}
