package me.legrange.panstamp;

import java.util.List;

/**
 * An abstraction of a panStamp device.
 *
 * @author gideon
 */
public interface PanStamp {

    /**
     * Get the address of this device.
     * @return The address 
     */
    public int getAddress();
    
    /** 
     * Get the device name (as defined by the endpoint definition). 
     * 
     * @return The name of the device
     */
    public String getName();

    /** return the device configuration for this device
     * 
     * @return The device configuration
     * @throws me.legrange.panstamp.GatewayException */
    public DeviceConfig getConfig() throws GatewayException;
    
    /** Get the gateway this device is attached to.
     * @return  The gateway */
    public Gateway getGateway();

    /**
     * Get the register with the given register ID for this device.
     * 
     * 
     * @return the register for the given id
     * @param id ID of register to return
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a
     * problem retrieving the register.
     */
    Register getRegister(int id) throws GatewayException;

    /** 
     * Determine if the device has a register with the given ID. 
     * @param id The id of the register required.
     * @return True if the panStamp has the register.
     * @throws GatewayException 
     */
    boolean hasRegister(int id) throws GatewayException;

    /**
     * Get the list of registers defined for this device
     *
     * @return The list of registers.
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a problem with the list of register.
     */
    List<Register> getRegisters() throws GatewayException;

    /** add an event listener
     * @param l The listener to add */
    void addListener(PanStampListener l);

    /** remove an event listener
     * @param l The listener to remove */
    void removeListener(PanStampListener l);

}
