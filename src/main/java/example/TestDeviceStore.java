package example;

import me.legrange.panstamp.ModemException;
import me.legrange.panstamp.Network;
import me.legrange.panstamp.NetworkException;

/**

 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class TestDeviceStore extends TestEvents {

    private static final String PORT = "/dev/tty.usbserial-A800HNMV";
    private static final int BAUD = 38400;
    private Network nw;
    private int seq = 0;

    public static void main(String... args) throws Exception {

        TestDeviceStore te = new TestDeviceStore();
        te.connect();
        te.setupDeviceStore();
        while (true) {
            Thread.sleep(1000);
        }
    }

    /** Connect to serial port and start the network */
    private void connect() throws NetworkException {
        nw = Network.openSerial(PORT, BAUD);
      
       
    }

    private void setupDeviceStore() {
        nw.setDeviceStore(new ConfigDeviceStateStore());
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

}
