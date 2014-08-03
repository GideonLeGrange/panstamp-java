package me.legrange.example;

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
    

    protected abstract void run() throws GatewayException;
    
    protected void connect() throws GatewayException {

         gw = Gateway.open(PORT, BAUD);
        gw.addListener(this);

    }
    
    protected void say(String fmt, Object... args) {
        System.out.printf(fmt, args);
        System.out.println();
    }

}
