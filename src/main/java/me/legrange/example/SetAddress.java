package me.legrange.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.impl.StandardEndpoint;
import me.legrange.panstamp.util.OnWake;
import me.legrange.panstamp.util.Util;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.SWAPException;

/**
 *
 * @author gideon
 */
public class SetAddress extends Example {

    public static int NEW_ADDR = 0x04;
    public static int OLD_ADDR = 0x01;

    public static void main(String... args) throws Exception {
        SetAddress app = new SetAddress();
        app.connect();
        app.run();
        app.close();
    }

    @Override
    protected void run() throws GatewayException {
        try {
            say("Setting up modem");
            ModemSetup setup = gw.getSWAPModem().getSetup();
            setup.setDeviceAddress(1);
            gw.getSWAPModem().setSetup(setup);
            say("Attaching listener");
            gw.addListener(this);
            while (!done) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SetAddress.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.exit(1);
        } catch (SWAPException ex) {
            Logger.getLogger(SetAddress.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public synchronized void gatewayUpdated(GatewayEvent ev) {
            if (false) throw new RuntimeException();
            switch (ev.getType()) {
                case DEVICE_DETECTED : {
                    PanStamp ps = ev.getDevice();
                    int addr = ps.getAddress();
                    say("Detected node %2x", addr);
                    if (addr == NEW_ADDR) {
                        say("Detected %2x so we're done, quit", NEW_ADDR);
                        done = true;
                    }
                    else if (addr == OLD_ADDR) {
                        try {
                            say("Detected %2x so scheduling update", OLD_ADDR);
                            OnWake ow = new OnWake(ps);
                            ow.queue(Util.getEndpoint(ps, StandardEndpoint.DEVICE_ADDRESS), NEW_ADDR);
                        } catch (GatewayException ex) {
                            Logger.getLogger(SetAddress.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
            }
    }

    private boolean done = false;

}
