package me.legrange.panstamp;

/**
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
     * @param id Register to read
     */
    Register getRegister(int id);

    /**
     * return the endpoint for the given name
     * @param name
     * @return 
     */
    Endpoint getEndpoint(String name) throws GatewayException;

}
