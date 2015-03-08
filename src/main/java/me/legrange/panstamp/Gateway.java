package me.legrange.panstamp;

import java.util.List;
import me.legrange.swap.SWAPModem;

/**
 * A PanStamp network gateway. This object represents the interface to a network
 * of PanStamp devices accessible via a SWAP modem.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface Gateway {

    /**
     * Open the gateway. This will open the underlying modem and internal
     * processes that are needed.
     *
     * @throws GatewayException Thrown if there is a problem opening the modem.
     */
    void open() throws GatewayException;

    /**
     * Disconnect the connection and close the gateway
     *
     * @throws me.legrange.panstamp.GatewayException
     */
    void close() throws GatewayException;
    
    /** 
     * Check if the gateway is open (is connected to a panStamp network). 
     * 
     * @return True if the network is running.
     */
    boolean isOpen();

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
    
    /** Removes the device with the given address from the network
     * 
     * @param address The address of the device to remove.
     */
    void removeDevice(int address);

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
     * Return the device library being used to lookup device definitions. 
     * @return The current library.
     */
    DeviceLibrary getDeviceLibrary();
    
    /** 
     * Set the device library used to lookup device definitions.
     * @param lib The library to use.
     */
    void setDeviceLibrary(DeviceLibrary lib);
    
    /** Return the device store used to save device state. 
     * 
     * @return The current store.
     */
    DeviceStateStore getDeviceStore();
    
    /** 
     * Set the device store to use to lookup persisted device registers. 
     * @param store The store to use.
     */
    void setDeviceStore(DeviceStateStore store);

    /**
     * return the network ID for the network supported by this gateway
     *
     * @return The network ID
     * @throws me.legrange.panstamp.core.ModemException Thrown if there is a
     * problem determining the network ID
     */
    int getNetworkId() throws GatewayException;

    /** Set the network ID for the network accessed by this gateway
     * 
     * @param id The network ID
     * @throws GatewayException thrown if there is a problem setting the network ID 
     */
    void setNetworkId(int id) throws GatewayException;

    /**
     * get the gateway panStamp's address
     *
     * @return the gateway address
     * @throws me.legrange.panstamp.core.ModemException
     */
    int getDeviceAddress() throws GatewayException;

    /** 
     * Set the device address for the gateway panStamp 
     * @param addr
     * @throws GatewayException 
     */
    void setDeviceAddress(int addr) throws GatewayException;

    /**
     * Get the frequency channel
     *
     * @return The channel
     * @throws me.legrange.panstamp.core.ModemException
     */
    int getChannel() throws GatewayException;

    /** 
     * Set the frequency channel 
     * 
     * @param channel The channel to use. 
     * @throws GatewayException 
     */
    void setChannel(int channel) throws GatewayException;

    /**
     * Get the security option
     *
     * @return the security option value.
     * @throws me.legrange.panstamp.core.ModemException
     */
    int getSecurityOption() throws GatewayException;

    /** 
     * Set the security option 
     * 
     * @param secOpt
     * @throws GatewayException 
     */
    void setSecurityOption(int secOpt) throws GatewayException;
}
