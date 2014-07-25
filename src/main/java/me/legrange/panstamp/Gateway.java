package me.legrange.panstamp;

import java.util.Collection;
import me.legrange.panstamp.impl.SerialGateway;

/**
 * A PanStamp network gateway
 *
 * @author gideon
 */
public abstract class Gateway {

    public static void main(String... args) throws Exception {
        Gateway gw = Gateway.open("/dev/tty.usbserial-A800HNMV", 38400);
        while (gw.getDevices().isEmpty()) {
            Thread.sleep(1000);
        }
        PanStamp p = gw.getDevices().iterator().next();
        System.out.printf("Found '%s' \n", p.getAddress());

        Endpoint<Boolean> ep0 = (Endpoint<Boolean>) p.getEndpoint("Binary 0");
         ep0.addListener("", new  EndpointListener<Boolean>() {

            @Override
            public void valueReceived(Boolean val) {
                System.out.printf("LED is %s", val ? "on" : "off");
            }
        });
        
        /* InputEndpoint<Double> ep0 = (InputEndpoint<Double>) p.getEndpoint("Temperature");
         ep0.addListener("C", new EndpointListener<Double>() {

         @Override
         public void valueReceived(Double val) {
         System.out.printf("Temperature: %0.2f\n", val);
         }
         }); */
        boolean on = true;
        while (true) {
            ep0.setValue(on);
            on = !on;
            Thread.sleep(5000);
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

    /**
     * Disconnect from the modem and close the gateway
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
     * return the devices associated with this gateway
     *
     * @return The collection of devices
     */
    public abstract Collection<PanStamp> getDevices();

}
