package me.legrange.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.Gateway;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.StandardEndpoint;
import me.legrange.panstamp.AbstractPanStampListener;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.SWAPException;

/**
 *
 * @author gideon
 */
public class SetAddress extends Example {

    public static int NEW_ADDR = 0x03;
    public static int OLD_ADDR = 0x04;

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
    public void deviceDetected(Gateway gw, PanStamp ps) {
        int addr = ps.getAddress();
        say("Detected node %2x", addr);
        if (addr == NEW_ADDR) {
            say("Detected %2x so we're done, quit", NEW_ADDR);
            done = true;
        } else if (addr == OLD_ADDR) {
            try {
                ps.addListener(new AbstractPanStampListener() {

                    @Override
                    public void syncRequired(PanStamp ps) {
                        System.out.printf("Device %2x needs to sync. Press the button\n", ps.getAddress());
                    }
                });
                Endpoint ep = Util.getEndpoint(ps, StandardEndpoint.DEVICE_ADDRESS);
                ep.setValue(NEW_ADDR);
            } catch (GatewayException ex) {
                Logger.getLogger(SetAddress.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    @Override
    public void deviceRemoved(Gateway gw, PanStamp dev) {
    }
    private boolean done = false;



}
