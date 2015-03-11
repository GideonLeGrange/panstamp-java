package me.legrange.panstamp;

import java.util.List;

/**
 * An abstraction of a panStamp device.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface PanStamp {
    
    /**
     * Get the address of this device.
     *
     * @return The address
     */
    int getAddress();

    /**
     * Return the network channel *
     *
     * @return The channel
     * @throws GatewayException
     */
    int getChannel() throws GatewayException;

    /**
     * Return the network ID
     *
     * @return The network ID
     * @throws GatewayException
     */
    int getNetwork() throws GatewayException;

    /**
     * Return the current security option
     *
     * @return The security option
     * @throws me.legrange.panstamp.GatewayException
     */
    int getSecurityOption() throws GatewayException;

    /**
     * Return the transmit interval
     *
     * @return The interval
     * @throws me.legrange.panstamp.GatewayException
     */
    int getTxInterval() throws GatewayException;
    /** 
     * Set the address of the panStamp 
     * @param addr The address to set
     * @throws GatewayException Thrown if there is a problem reading the interval.
     */
    void setAddress(int addr) throws GatewayException;
    
    /** 
     * Set the network id of the device
     * @param network The network id
     * @throws GatewayException Thrown if there is a problem setting the ID
     */
    void setNetwork(int network) throws GatewayException;

    /**
     * Set the network channel of the device 
     * @param channel The channel to set.
     * @throws GatewayException  Thrown if there is a problem setting the channel
     */
    void setChannel(int channel) throws GatewayException;

    /**
     * Set the security option of the device.
     * @param option The security option to set.
     * @throws GatewayException  Thrown if there is a problem setting the option.
     */
    void setSecurityOption(int option) throws GatewayException;

    /** 
     * Set the transmit interval (in seconds) of the device.
     * @param txInterval The interval to set. 
     * @throws GatewayException Thrown if there is a problem setting the interval.
     */
    void setTxInterval(int txInterval)throws GatewayException;
    
    /** 
     * Get the device manufacturer id. 
     * @return The manufacturer Id
     * @throws GatewayException Thrown if there is a problem reading the id.
     */
    int getManufacturerId() throws GatewayException;

        /** 
     * Get the device product id. 
     * @return The product Id
     * @throws GatewayException Thrown if there is a problem reading the id.
     */
    int getProductId() throws GatewayException;

    /**
     * Get the device name (as defined by the endpoint definition).
     *
     * @return The name of the device
     */
    String getName();

    /**
     * Get the gateway this device is attached to.
     *
     * @return The gateway
     */
    Gateway getGateway();

    /**
     * Get the register with the given register ID for this device.
     *
     *
     * @return the register for the given id
     * @param id ID of register to return
     */
    Register getRegister(int id);

    /**
     * Determine if the device has a register with the given ID.
     *
     * @param id The id of the register required.
     * @return True if the panStamp has the register.
     */
    boolean hasRegister(int id);

    /**
     * Get the list of registers defined for this device
     *
     * @return The list of registers.
     */
    List<Register> getRegisters();

    /**
     * add an event listener
     *
     * @param l The listener to add
     */
    void addListener(PanStampListener l);

    /**
     * remove an event listener
     *
     * @param l The listener to remove
     */
    void removeListener(PanStampListener l);

}
