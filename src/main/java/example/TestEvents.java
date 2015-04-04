package example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
 *
 * @since 1.0
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

    private void connect() throws NetworkException {
        nw = Network.openSerial(PORT, BAUD);
    }

    private void setupListeners() throws NetworkException {
        nw.addListener(new NetworkListener() {

            @Override
            public void deviceDetected(Network gw, PanStamp dev) {
                say("N: Device with address %d added to network", dev.getAddress());
                dev.addListener(new PanStampListener() {

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
                        say("P: Register %d detected for device %d", reg.getId(), dev.getAddress());
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
                                say("R: Endpoint '%s' added to %d", ep.getName(), reg.getId());
                                ep.addListener(new EndpointListener() {

                                    @Override
                                    public void valueReceived(Endpoint ep, Object value) {
                                        say("E: Value '%s' received for '%s'", ep.getName(), value);
                                    }
                                });
                            }

                            @Override
                            public void parameterAdded(Register reg, Parameter par) {
                                say("R: Parameter '%s' added to %d", par.getName(), reg.getId());
                            }
                        });
                    }

                    @Override
                    public void syncRequired(PanStamp dev) {
                        say("P: Sync required for device %d", dev.getAddress());
                    }
                });
            }

            @Override
            public void deviceRemoved(Network gw, PanStamp dev) {
                say("N: Device with address %d removed to network", dev.getAddress());
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
