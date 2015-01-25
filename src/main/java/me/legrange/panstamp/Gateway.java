package me.legrange.panstamp;

import java.util.List;
import me.legrange.panstamp.impl.ModemException;
import me.legrange.swap.SWAPModem;

/**
 * A PanStamp network gateway. This object represents the interface to a network of 
 * PanStamp devices accessible via a SWAP modem. 
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
    void close() throws GatewayException;

    /**
     * use to check if a device with the given address is known
     *
     * @param address Address of the device we're looking for.
     * @return True if a device with the given address is known
     */
    boolean hasDevice(int address);

    /**
     * return the device with the given name
     *
     * @param address Address of device to fins
     * @return The device
     * @throws me.legrange.panstamp.NodeNotFoundException
     */
    PanStamp getDevice(int address) throws NodeNotFoundException;

    /**
     * return all the devices associated with this gateway
     *
     * @return The list of devices
     */
    List<PanStamp> getDevices();

    /**
     * add listener to receive new device events
     *
     * @param l The listener to add
     */
    void addListener(GatewayListener l);

    /**
     * remove a listener from the gateway
     *
     * @param l The listener to remove
     */
    void removeListener(GatewayListener l);

    /**
     * return the SWAP modem to gain access to the lower layer
     *
     * @return The SWAP modem supporting this gateway
     */
    SWAPModem getSWAPModem();

    /**
     * return the network ID for the network supported by this gateway
     *
     * @return The network ID
     * @throws me.legrange.panstamp.impl.ModemException Thrown if there is a problem determining the network ID 
     */
    int getNetworkId() throws ModemException;
    
     /** get the gateway panStamp's address 
      * @return the gateway address
     * @throws me.legrange.panstamp.impl.ModemException
      */
     int getDeviceAddress() throws ModemException;
    
    /** Get the frequency channel
     * @return TThe channel
     * @throws me.legrange.panstamp.impl.ModemException  */
    int getChannel() throws ModemException;

    /** Get the security option
     * @return the security option value.
     * @throws me.legrange.panstamp.impl.ModemException */
    int getSecurityOption() throws ModemException;
}
