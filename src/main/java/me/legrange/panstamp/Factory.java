package me.legrange.panstamp;

import me.legrange.panstamp.impl.GatewayImpl;
import me.legrange.panstamp.impl.ModemException;
import me.legrange.swap.SWAPException;
import me.legrange.swap.serial.SerialModem;
import me.legrange.swap.tcp.TcpException;
import me.legrange.swap.tcp.TcpModem;

/**
 *
 * @author gideon
 */
public class Factory {
    
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

    public static Gateway createSerial(String port, int baud) {
        SerialModem sm = new SerialModem(port, baud);
        return new GatewayImpl(sm);
    }
    
}
