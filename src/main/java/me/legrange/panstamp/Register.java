package me.legrange.panstamp;

import java.util.List;

/**
 * An abstraction of a panStamp register.
 *
 * @author gideon
 */
public interface Register {

    /**
     * return the register ID
     *
     * @return The id of this register.
     */
    int getId();

    String getName();
    
    PanStamp getDevice();
    
    /**
     * return the endpoints defined for this register
     *
     * @return The endpoints
     */
    List<Endpoint> getEndpoints() throws GatewayException;

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

    /**
     * Add a listener to receive register updates
     *
     * @param l listener to add
     */
    void addListener(RegisterListener l);

    /**
     * remove a listener
     *
     * @param l listener to remove
     */
    void removeListener(RegisterListener l);

    /**
     * set the register value and send to remote node
     *
     * @param value the new value
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a
     * problem updating the register
     */
    void setValue(byte value[]) throws GatewayException;

    /**
     * get the value of the register
     *
     * @return The value of the register
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a
     * problem reading the register
     */
    byte[] getValue() throws GatewayException;

    /**
     * return true if the register has a currently set value
     */
    boolean hasValue();

}
