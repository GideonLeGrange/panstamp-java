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

    public int getChannel() throws GatewayException; ;

    public int getNetwork() throws GatewayException;

    public int getSecurityOption() throws GatewayException;

    public int getTxInterval() throws GatewayException;

    public Gateway getGateway();

    public String getName();

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

    void addListener(PanStampListener l);

    void removeListener(PanStampListener l);

}
