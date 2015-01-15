package me.legrange.panstamp;

/**
 * PanStamp device configuration. Use this interface to read
 * and change the configuration of a remote panStamp device.
 * @author gideon
 */
public interface DeviceConfig {
    
    /** Return the address of the device
     * @return The address  */
    int getAddress();
    
    /** Return the network channel *
     * 
     * @return The channel
     * @throws GatewayException 
     */
    int getChannel() throws GatewayException;
    /** Return the network ID
     * 
     * @return The network ID
     * @throws GatewayException 
     */
    int getNetwork() throws GatewayException;
    
    /** Return the current security option
     * @return The security option
     * @throws me.legrange.panstamp.GatewayException  */
    int getSecurityOption() throws GatewayException;

    /** Return the transmit interval
     * @return The interval
     * @throws me.legrange.panstamp.GatewayException  */
    int getTxInterval() throws GatewayException;

    /** 
     * Set the address of the panStamp 
     * @param addr The address to set
     * @throws GatewayException Thrown if there is a problem setting the address
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
    void setTxInterva(int txInterval)throws GatewayException;


}
