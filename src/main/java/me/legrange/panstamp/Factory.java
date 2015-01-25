package me.legrange.panstamp;

import me.legrange.panstamp.impl.GatewayImpl;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.serial.SerialModem;
import me.legrange.swap.tcp.TcpModem;

/**
 *
 * @author gideon
 */
public class Factory {
    
    public static Gateway createSerial(String port, int baud) {
        SerialModem sm = new SerialModem(port, baud);
        return createGateway(sm);
    }
    
    
    public static Gateway createTcp(String host, int port) {
        TcpModem tm = new TcpModem(host, port);
        return createGateway(tm);
    }
    
    private static Gateway createGateway(SWAPModem modem) {
        return new GatewayImpl(modem);
    }
    
}
