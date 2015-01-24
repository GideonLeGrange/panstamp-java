package me.legrange.panstamp;

import java.util.List;
import me.legrange.panstamp.impl.GatewayImpl;
import me.legrange.panstamp.impl.ModemException;
import me.legrange.swap.SWAPException;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.serial.SerialModem;
import me.legrange.swap.tcp.TcpModem;
import me.legrange.swap.tcp.TcpException;

/**
 * A PanStamp network gateway. This object represents the interface to a network of 
 * Panstamp devices accessible via a SWAP modem. 
 *
 * @author gideon
 */
public abstract class Gateway {
    
    /**
     * Open the serial modem application and return a Gateway object for the
     * connection.
     *
     * @param port Serial port to open.
     * @param baud Serial speed
     * @return
     * @throws me.legrange.panstamp.impl.ModemException
     */
    public static Gateway openSerial(String port, int baud) throws ModemException {
        SerialModem sm;
        try {
            sm = new SerialModem(port, baud);
            sm.open();
        } catch (SWAPException ex) {
            throw new ModemException(ex.getMessage(), ex);
        }
        return new GatewayImpl(sm);
    }
    
      /**
     * Open the TCP modem application and return a Gateway object for the
     * connection.
     *
     * @param host Host address to open.
     * @param port TCP port to connect to
     * @return The gateway
     * @throws me.legrange.panstamp.impl.ModemException
     */
    public static Gateway openTcp(String host, int port) throws ModemException {
        TcpModem tm;
        try {
            tm =  new TcpModem(host, port);
            tm.open();
        } catch (TcpException ex) {
            throw new ModemException(ex.getMessage(), ex);
        }
        return new GatewayImpl(tm);
    }


    /**
     * Disconnect the connection and close the gateway
     *
     * @throws me.legrange.panstamp.GatewayException
     */
    public abstract void close() throws GatewayException;

    /**
     * use to check if a device with the given address is known
     *
     * @param address Address of the device we're looking for.
     * @return True if a device with the given address is known
     */
    public abstract boolean hasDevice(int address);

    /**
     * return the device with the given name
     *
     * @param address Address of device to fins
     * @return The device
     * @throws me.legrange.panstamp.NodeNotFoundException
     */
    public abstract PanStamp getDevice(int address) throws NodeNotFoundException;

    /**
     * return all the devices associated with this gateway
     *
     * @return The list of devices
     */
    public abstract List<PanStamp> getDevices();

    /**
     * add listener to receive new device events
     *
     * @param l The listener to add
     */
    public abstract void addListener(GatewayListener l);

    /**
     * remove a listener from the gateway
     *
     * @param l The listener to remove
     */
    public abstract void removeListener(GatewayListener l);

    /**
     * return the SWAP modem to gain access to the lower layer
     *
     * @return The SWAP modem supporting this gateway
     */
    public abstract SWAPModem getSWAPModem();

    /**
     * return the network ID for the network supported by this gateway
     *
     * @return The network ID
     * @throws me.legrange.panstamp.impl.ModemException Thrown if there is a problem determining the network ID 
     */
    public abstract int getNetworkId() throws ModemException;
    
     /** get the gateway panStamp's address 
      * @return the gateway address
      */
     public abstract int getDeviceAddress() throws ModemException;
    
    /** Get the frequency channel
     * @return TThe channel  */
    public abstract int getChannel() throws ModemException;

    /** Get the security option
     * @return the security option value. */
    public abstract int getSecurityOption() throws ModemException;
}
