package me.legrange.panstamp;

import java.util.List;
import me.legrange.panstamp.impl.ModemException;
import me.legrange.swap.SWAPModem;

/**
 * A PanStamp network gateway. This object represents the interface to a network of 
 * Panstamp devices accessible via a SWAP modem. 
 *
 * @author gideon
 */
public interface Gateway {
    
    /** Open the gateway. 
     * This will open the underlying modem and internal processes that are needed. 
     * 
     * @throws GatewayException  Thrown if there is a problem opening the modem.
     */
    void open() throws GatewayException;

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

    /**
     * remove a listener from the gateway
     *
     * @param l The listener to remove
     */
    public abstract void removeListener(GatewayListener l);

    /**
     * return the SWAP modem to gain access to the lower layer
     *
     * @return The SWAP modem supporting this gateway
     */
    public abstract SWAPModem getSWAPModem();

    /**
     * return the network ID for the network supported by this gateway
     *
     * @return The network ID
     * @throws me.legrange.panstamp.impl.ModemException Thrown if there is a problem determining the network ID 
     */
    public abstract int getNetworkId() throws ModemException;
    
     /** get the gateway panStamp's address 
      * @return the gateway address
      */
     public abstract int getDeviceAddress() throws ModemException;
    
    /** Get the frequency channel
     * @return TThe channel  */
    public abstract int getChannel() throws ModemException;

    /** Get the security option
     * @return the security option value. */
    public abstract int getSecurityOption() throws ModemException;
}
