package example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Gateway;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.GatewayListener;

/**
 *
 * @author gideon
 */
public abstract class Example implements GatewayListener {

    private static final String PORT = "/dev/tty.usbserial-A800HNMV";
    private static final int BAUD = 38400;
    protected Gateway gw;
    
    protected void run() throws GatewayException {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Example.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    protected void connect() throws GatewayException {
        say("Opening gateway on %s:%d", PORT, BAUD);
         gw = Gateway.createSerial(PORT, BAUD);
         gw.open();
    }
    
    protected void close() throws GatewayException { 
        gw.close();
    }
    
    protected void say(String fmt, Object... args) {
        System.out.printf(fmt, args);
        System.out.println();
    }

}
