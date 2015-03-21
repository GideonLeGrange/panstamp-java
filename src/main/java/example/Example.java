package example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Network;
import me.legrange.panstamp.NetworkException;
import me.legrange.panstamp.NetworkListener;

/**
 *
 * @author gideon
 */
public abstract class Example implements NetworkListener {

    private static final String PORT = "/dev/tty.usbserial-A800HNMV";
    private static final int BAUD = 38400;
    protected Network gw;
    
    protected void run() throws NetworkException {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Example.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    protected void connect() throws NetworkException {
        say("Opening gateway on %s:%d", PORT, BAUD);
         gw = Network.createSerial(PORT, BAUD);
         gw.open();
    }
    
    protected void close() throws NetworkException { 
        gw.close();
    }
    
    protected void say(String fmt, Object... args) {
        System.out.printf(fmt, args);
        System.out.println();
    }

}
