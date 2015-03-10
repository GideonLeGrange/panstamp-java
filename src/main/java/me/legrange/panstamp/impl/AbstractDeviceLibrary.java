package me.legrange.panstamp.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.legrange.panstamp.DeviceLibrary;
import me.legrange.panstamp.GatewayException;

/**
 * A device library implementation is used to find and load panStamp XML device
 * definitions.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
abstract class AbstractDeviceLibrary implements DeviceLibrary {

    @Override
    public boolean hasDeviceDefinition(int manufacturedID, int productId) throws GatewayException {
        return getDefinition(manufacturedID, productId) != null;
    }

    @Override
    public XMLDeviceDefinition getDeviceDefinition(int manufacturedID, int productId) throws GatewayException {
        XMLDeviceDefinition def = getDefinition(manufacturedID, productId);
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
    protected abstract InputStream getStream(String path);

    private synchronized XMLDeviceDefinition getDefinition(int manufacturedID, int productId) throws DeviceNotFoundException, ParseException {
        if (devices == null) {
            devices = new HashMap<>();
            List<XMLDeviceDefinition> all = XMLParser.parse(this);
            for (XMLDeviceDefinition dev : all) {
                devices.put(makeId(dev.getDeveloper().getId(), dev.getId()), dev);
            }
        }
        return devices.get(makeId(manufacturedID, productId));
    }

    private String makeId(int manufacturedID, int productId) {
        return String.format("%d/%d", manufacturedID, productId);
    }

    private Map<String, XMLDeviceDefinition> devices;

}
