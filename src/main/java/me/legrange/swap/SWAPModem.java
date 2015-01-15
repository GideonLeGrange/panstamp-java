package me.legrange.swap;

import me.legrange.swap.serial.SerialModem;

/**
 *
 * @author gideon
 */
public abstract class SWAPModem {

    /**
     * An interface abstracting a SWAP modem.
     *
     * @param port
     * @param baud
     * @return
     * @throws SWAPException
     */
    public static SWAPModem openSerial(String port, int baud) throws SWAPException {
        return SerialModem.open(port, baud);
    }

    /**
     * disconnect and close the modem
     *
     * @throws me.legrange.swap.SWAPException
     */
    public abstract void close() throws SWAPException;

    /**
     * send a message out onto the network
     *
     * @param msg Message to send.
     * @throws me.legrange.swap.SWAPException
     */
    public abstract void send(SwapMessage msg) throws SWAPException;

    /**
     * add a message listener to receive messages
     *
     * @param l listener to add.
     */
    public abstract void addListener(MessageListener l);

    /**
     * remove a listener
     *
     * @param l listener to remove
     */
    public abstract void removeListener(MessageListener l);

    /**
     * get the network setup
     *
     * @return The setup data
     */
    public abstract ModemSetup getSetup() throws SWAPException;

    /** set the network setup */
    public abstract void setSetup(ModemSetup setup) throws SWAPException;

}
