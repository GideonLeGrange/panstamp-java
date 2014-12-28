package me.legrange.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;

/**
 *
 * @author gideon
 */
public class SetSleep extends Example {

    public static void main(String... args) throws Exception {
        SetSleep app = new SetSleep();
        app.connect();
        app.run();
    }

    private static final int SLEEP = 10;
    private boolean done = false;

    @Override
    protected void run() throws GatewayException {
        gw.addListener(this);
        while (!done) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SetSleep.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.exit(0);
    }

    @Override
    public void gatewayUpdated(GatewayEvent ev) {
        PanStamp ps = ev.getDevice();
   /*     try {
            say("Detected device %d", ps.getAddress());
            OnWake ow = new OnWake(ps); 
            ow.queue(Util.getEndpoint(ps, StandardEndpoint.PERIODIC_TX_INTERVAL), SLEEP);
            say("Queued change of sleep for %2x to 10", ps.getAddress());
        } catch (GatewayException ex) {
            Logger.getLogger(SetSleep.class.getName()).log(Level.SEVERE, null, ex);
        } */
    }

}
