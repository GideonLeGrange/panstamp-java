package me.legrange.swap;

/**
 * A virtual modem that provides access to a SWAP transport. Currently we
 * implement two kinds, serial and TCP/IP.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public interface SwapModem {

    /** Type of SWAP modem. Currently only serial and TCP_IP are supported, and it's unlikely to change. */
    public enum Type {

        SERIAL, TCP_IP
    };
    
    /** 
     * connect to the modem. 
     * @throws me.legrange.swap.SwapException Thrown if there is a problem opening the modem.
     */
    void open() throws SwapException;

    /**
     * disconnect and close the modem
     *
     * @throws me.legrange.swap.SwapException
     */
    void close() throws SwapException;
    
    /** 
     * Check if the modem is open (connected to it's implementation device) 
     * @return true if the modem is connected
     * 
     */
    boolean isOpen();

    /**
     * send a message out onto the network
     *
     * @param msg Message to send.
     * @throws me.legrange.swap.SwapException
     */
    void send(SwapMessage msg) throws SwapException;

    /**
     * add a message listener to receive messages
     *
     * @param l listener to add.
     */
    void addListener(MessageListener l);

    /**
     * remove a listener
     *
     * @param l listener to remove
     */
    void removeListener(MessageListener l);

    /**
     * get the network setup
     *
     * @return The setup data
     * @throws me.legrange.swap.SwapException Thrown if there is a problem
     * retrieving the setup
     */
    ModemSetup getSetup() throws SwapException;

    /**
     * set the network setup
     *
     * @param setup The modem setup to apply
     * @throws me.legrange.swap.SwapException Thrown if there is a problem
     * applying the setup
     */
    void setSetup(ModemSetup setup) throws SwapException;

    /**
     * determine the type of virtual modem
     *
     * @return The type of the modem
     */
    Type getType();

}
