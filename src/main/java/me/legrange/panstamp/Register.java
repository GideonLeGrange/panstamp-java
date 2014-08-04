package me.legrange.panstamp;

/**
 * An abstraction of a panStamp register.
 * @author gideon
 */
public interface Register {

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
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a problem updating the register
     */
    void setValue(byte value[]) throws GatewayException;
    
    /** get the value of the register
     * @return The value of the register
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a problem reading the register */
    byte[] getValue() throws GatewayException;
    
    /** return true if the register has a currently set value */
    boolean hasValue();
    
}

