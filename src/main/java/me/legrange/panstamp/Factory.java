package me.legrange.panstamp;

import me.legrange.panstamp.core.GatewayImpl;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.serial.SerialModem;
import me.legrange.swap.tcp.TcpModem;

/**
 * Factory used to construct panStamp gateway instances. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public final class Factory {
    
    /** Create a new serial gateway (gateway attached to a serial port) with the given port and speed, 
     * and with the default device library and data store. 
     * 
     * @param port The serial port to open, for example COM1 or /dev/ttyS0
     * @param baud The speed at which to open it, for example 34800 
     * @return The newly created gateway. 
     */
    public static Gateway createSerial(String port, int baud) {
        SerialModem sm = new SerialModem(port, baud);
        return createGateway(sm);
    }

    /** Create a new TCP/IP gateway (gateway attached to a remote TCP service) with the given host and port, 
     * and with the default device library and data store. 
     * 
     * @param host The host name to which to connect, for example 'localhost' or '192.168.1.1'
     * @param port The TCP port to which to connect.
     * @return The newly created gateway 
     */
    public static Gateway createTcp(String host, int port) {
        TcpModem tm = new TcpModem(host, port);
        return createGateway(tm);
    }
    
    /** Create a new gateway with the given pre-existing SWAP modem. 
     * 
     * @param modem
     * @return 
     */
    public static Gateway createGateway(SWAPModem modem) {
        return new GatewayImpl(modem);
    }
}
