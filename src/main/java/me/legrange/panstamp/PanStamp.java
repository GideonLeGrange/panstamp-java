package me.legrange.panstamp;

import java.util.List;

/**
 * An abstraction of a panStamp device.
 *
 * @author gideon
 */
public interface PanStamp {

    /**
     * @return address of this mote
     */
    public int getAddress();
    
    /** 
     * return the device name (as defined by the endpoint definition). 
     * @return The name of the device
     */
    public String getName();

    /** return the device configuration for this mote
     * @return The device configuration
     * @throws me.legrange.panstamp.GatewayException */
    public DeviceConfig getConfig() throws GatewayException;
    
    /** return the gateway this device is attached to.
     * @return  The gateway */
    public Gateway getGateway();

    /**
     * @return the register for the given id
     * @param id ID of register to return
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a
     * problem retrieving the register.
     */
    Register getRegister(int id) throws GatewayException;

    boolean hasRegister(int id) throws GatewayException;

    /**
     * returns the list of registers defined for this device
     *
     * @return The list of registers.
     */
    List<Register> getRegisters() throws GatewayException;

    /** add an event listener */
    void addListener(PanStampListener l);

    /** remove an event listener */
    void removeListener(PanStampListener l);

}
