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
     * @return the register for the given id
     * @param id ID of register to return
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a
     * problem retrieving the register.
     */
    Register getRegister(int id) throws GatewayException;

    /**
     * return the endpoint for the given name
     *
     * @param name
     * @return
     * @throws me.legrange.panstamp.GatewayException
     */
    Endpoint getEndpoint(String name) throws GatewayException;

    /**
     * returns true if the device has an endpoint with the given name
     *
     * @param name Name of endpoint we're querying
     * @return True if the endpoint is known.
     * @throws me.legrange.panstamp.GatewayException Thrown if an error is
     * experienced
     */
    boolean hasEndpoint(String name) throws GatewayException;

    /** returns the list of endpoints defined for this device
     * @return The list of endpoints.
     * @throws me.legrange.panstamp.GatewayException Thrown if an error is
     * experienced*/
    List<Endpoint> getEndpoints() throws GatewayException;
    
    /** returns the list of registers defined for this device 
     * 
     * @return The list of registers. 
     */
    List<Register> getRegisters() throws GatewayException;
}
