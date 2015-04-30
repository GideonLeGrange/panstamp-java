package example;

import java.math.BigInteger;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.ModemException;
import me.legrange.panstamp.Network;
import me.legrange.panstamp.NetworkException;
import me.legrange.panstamp.NetworkListener;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.Parameter;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterListener;

/**
 * This example shows how to work with the classes from Network, PanStamp, Register and Endpoint
 * so that events for all instances in the structure can be received. 
 * 
 * It is important to understand that when you add a listener (event handler) to an instance of one of these
 * classes, it is very likely that you may already have "missed" some events.
 * 
 * This examples demonstrates how to iterate through existing data and how to add listeners to receive future data. 
 * 
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class TestEvents {

    private static final String PORT = "/dev/tty.usbserial-A800HNMV";
    private static final int BAUD = 38400;
    private Network nw;
    private int seq = 0;

    public static void main(String... args) throws Exception {

        TestEvents te = new TestEvents();
        te.connect();
        te.setupListeners();
        while (true) {
            Thread.sleep(1000);
        }
    }

    /** Connect to serial port and start the network */
    private void connect() throws NetworkException {
        nw = Network.openSerial(PORT, BAUD);
       
    }

    /** Set up the listeners for the objects currently in the tree.
     * Note that we build and event handler that will call addPanstamp() when we are notified of a new panStamp, 
     * AND we call addPanstamp on devices already known when our code executes */
    private void setupListeners() throws NetworkException {
        nw.addListener(new NetworkListener() {

            @Override
            public void deviceDetected(Network gw, PanStamp dev) {
                addPanStamp(dev);
                say("N: Device with address %d added to network", dev.getAddress());
            }

            @Override
            public void deviceRemoved(Network gw, PanStamp dev) {
                say("N: Device with address %d removed to network", dev.getAddress());
            }

            @Override
            public void networkOpened(Network nw) {
                say("N: Opened");
            }

            @Override
            public void networkClosed(Network nw) {
                say("N: Closed");
            }
        });
        for (PanStamp ps : nw.getDevices()) {
            addPanStamp(ps);
        }
    }

    /** Do whatever we do if we have a new panStamp. */
    private void addPanStamp(PanStamp ps) {
        ps.addListener(new PanStampListener() {

            @Override
            public void productCodeChange(PanStamp dev, int manufacturerId, int productId) {
                say("P: Product code for %d changed to %d/%d\n", dev.getAddress(),
                        manufacturerId, productId);
            }

            @Override
            public void syncStateChange(PanStamp dev, int syncState) {
                say("P: Sync state for %d changed to %d\n", dev.getAddress(), syncState);
            }

            @Override
            public void registerDetected(PanStamp dev, Register reg) {
                addRegister(reg);
                say("P: Register %d detected for device %d", reg.getId(), dev.getAddress());
            }

            @Override
            public void syncRequired(PanStamp dev) {
                say("P: Sync required for device %d", dev.getAddress());
            }
        });
        for (Register reg : ps.getRegisters()) {
            addRegister(reg);
        }
    }

    /** do whatever we do if we have a new register */
    private void addRegister(Register reg) {
        reg.addListener(new RegisterListener() {

            @Override
            public void valueReceived(Register reg, byte[] value) {
                say("R: Value for %d updated to '%s'", reg.getId(), formatBytes(value));
            }

            @Override
            public void valueSet(Register reg, byte[] value) {
                say("R: Value for %d set to '%s'", reg.getId(), formatBytes(value));
            }

            @Override
            public void endpointAdded(Register reg, Endpoint ep) {
                addEndpoint(ep);
                say("R: Endpoint '%s' added to %d", ep.getName(), reg.getId());
            }

            @Override
            public void parameterAdded(Register reg, Parameter par) {
                say("R: Parameter '%s' added to %d", par.getName(), reg.getId());
            }
        });
        for (Endpoint ep : reg.getEndpoints()) {
            addEndpoint(ep);
        }
    }

    /** do what we do if we encounter a new endpoint */
    private void addEndpoint(Endpoint ep) {
        ep.addListener(new EndpointListener() {

            @Override
            public void valueReceived(Endpoint ep, Object value) {
                say("E: Value '%s' received for '%s'", ep.getName(), value);
            }
        });
    }

    private void disconnect() throws ModemException {
        nw.close();
    }

    private synchronized void say(String fmt, Object... args) {
        System.out.printf("[%04d] ", seq);
        System.out.printf(fmt, args);
        if (!fmt.endsWith("\n")) {
            System.out.println();
        }
        seq++;
    }

    private String formatBytes(byte val[]) {
        return new BigInteger(val).toString(16);
    }

}
