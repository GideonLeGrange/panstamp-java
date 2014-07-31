package me.legrange.panstamp;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.impl.NoSuchUnitException;
import me.legrange.panstamp.impl.SerialGateway;

/**
 * A PanStamp network gateway
 *
 * @author gideon
 */
public abstract class Gateway {

    public static void main(String... args) throws Exception {
        Gateway gw = Gateway.open("/dev/tty.usbserial-A800HNMV", 38400);
        System.out.println("Opened gateway");
        gw.addListener(new GatewayListener() {

            @Override
            public void deviceDetected(PanStamp ps) {
                System.out.println("Detected device");
                Endpoint<Double> ep0 = null;
                try {
                    ep0 = (Endpoint<Double>) ps.getEndpoint("Temperature");
                            System.out.println("Found endpoint 'Temperature'");

                } catch (GatewayException ex) {
                    Logger.getLogger(Gateway.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    ep0.addListener(new EndpointListener<Double>() {

                        @Override
                        public void valueReceived(Double val) {
                            System.out.printf("Temperature: %0.2f\n", val);
                        }
                    });
                } catch (NoSuchUnitException ex) {
                    Logger.getLogger(Gateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        System.out.println("Added listener");

        while (true) {
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
     * return all the devices associated with this gateway
     *
     * @return The list of devices
     */
    public abstract List<PanStamp> getDevices();

    /**
     * add listener to receive new device events
     *
     * @param l
     */
    public abstract void addListener(GatewayListener l);

}
