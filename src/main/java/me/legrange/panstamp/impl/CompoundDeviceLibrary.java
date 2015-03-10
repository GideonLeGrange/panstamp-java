package me.legrange.panstamp.impl;

import me.legrange.panstamp.DeviceLibrary;
import me.legrange.panstamp.GatewayException;

/**
 * A device library implementation that searches through one or more other
 * device libraries for a device definition.
 *
 * This will be useful in cases where the application developer wants to search
 * through for example an application XML directory first, before falling back
 * to the default class loader.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class CompoundDeviceLibrary implements DeviceLibrary {

    public CompoundDeviceLibrary(DeviceLibrary...sources) {
        this.sources = sources;
    }

    @Override
    public boolean hasDeviceDefinition(int manufacturedID, int productId) throws GatewayException {
        for (DeviceLibrary lib : sources) {
            if (lib.hasDeviceDefinition(manufacturedID, productId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public XMLDeviceDefinition getDeviceDefinition(int manufacturedID, int productId) throws GatewayException {
        for (DeviceLibrary lib : sources) {
            if (lib.hasDeviceDefinition(manufacturedID, productId)) {
                return lib.getDeviceDefinition(manufacturedID, productId);
            }
        }
        throw new DeviceNotFoundException(String.format("Could not find device definition for manufacturer/product %d/%d", manufacturedID, productId));
    }

    private final DeviceLibrary sources[];

}
