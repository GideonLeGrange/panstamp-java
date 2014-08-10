package me.legrange.panstamp;

import java.util.List;
import me.legrange.panstamp.impl.SerialGateway;
import me.legrange.swap.SWAPModem;

/**
 * A PanStamp network gateway
 *
 * @author gideon
 */
public abstract class Gateway {
    
    /**
     * Open the serial modem application and return a Gateway object for the connection. 
     * @param port
     * @param baud
     * @return
     * @throws GatewayException
     */
    public static Gateway openSerial(String port, int baud) throws GatewayException {
        return SerialGateway.openSerial(port, baud);
    }

    /**
     * Disconnect the connection and close the gateway
     *
     * @throws me.legrange.panstamp.GatewayException
     */
    public abstract void close() throws GatewayException;

    /**
     * use to check if a device with the given address is known
     *
     * @param address Address of the device we're looking for.
     * @return True if a device with the given address is known
     */
    public abstract boolean hasDevice(int address);

    /**
     * return the device with the given name
     *
     * @param address Address of device to fins
     * @return The device
     * @throws me.legrange.panstamp.NodeNotFoundException
     */
    public abstract PanStamp getDevice(int address) throws NodeNotFoundException;

    /**
     * return all the devices associated with this gateway
     *
     * @return The list of devices
     */
    public abstract List<PanStamp> getDevices();

    /**
     * add listener to receive new device events
     *
     * @param l The listener to add
     */
    public abstract void addListener(GatewayListener l);
    
    /** remove a listener from the gateway
     * @param l The listener to remove */
    public abstract void removeListener(GatewayListener l);
    
    /** gain access to the lower layer SWAP modem
     * @return The lower layer SWAP modem driving this gateway */
    public abstract SWAPModem getSWAPModem();

}
