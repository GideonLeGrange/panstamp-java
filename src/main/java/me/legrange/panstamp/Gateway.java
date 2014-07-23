package me.legrange.panstamp;

import java.util.Collection;
import me.legrange.panstamp.impl.SerialGateway;

/**
 * A PanStamp network gateway
 * @author gideon
 */
public abstract class Gateway {
    
    
    public static void main(String...args) throws Exception {
        Gateway gw = Gateway.open("/dev/tty.usbserial-A800HNMV", 38400);
        while (true) {
            for (PanStamp p : gw.getDevices()) {
                    
                InputEndpoint<Double> ep0 = (InputEndpoint<Double>) p.getEndpoint("Temperature");
                InputEndpoint<Double> ep1 = (InputEndpoint<Double>) p.getEndpoint("Voltage");
                System.out.printf("%s C, %s V\n", ep0.getValue("C"), ep1.getValue("V"));
            }
            Thread.sleep(10000);
        }
    }

    /**
     *
     * @param port
     * @param baud
     * @return
     * @throws GatewayException
     */
    public static Gateway open(String port, int baud) throws GatewayException {
        return SerialGateway.openSerial(port, baud);
    }

    /** Disconnect from the modem and close the gateway
     * @throws me.legrange.panstamp.GatewayException */
    public abstract void close() throws GatewayException;

    /**
     * use to check if a device with the given address is known
     * @param address Address of the device we're looking for.
     * @return True if a device with the given address is known 
     */
    public abstract boolean hasDevice(int address); 

    /** return the device with the given name 
     * @param address Address of device to fins
     * @return The device
     * @throws me.legrange.panstamp.NodeNotFoundException */
    public abstract PanStamp getDevice(int address) throws NodeNotFoundException;

    /** return the devices associated with this gateway
     * @return  The collection of devices */
    public abstract Collection<PanStamp> getDevices();
    
    
}
