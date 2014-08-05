package me.legrange.example;

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
                Logger.getLogger(SetSleep.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    protected void connect() throws GatewayException {

         gw = Gateway.openSerial(PORT, BAUD);
        gw.addListener(this);

    }
    
    protected void say(String fmt, Object... args) {
        System.out.printf(fmt, args);
        System.out.println();
    }

}
