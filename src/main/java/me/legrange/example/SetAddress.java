package me.legrange.example;

import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.impl.StandardEndpoint;
import me.legrange.panstamp.util.Endpoints;
import me.legrange.panstamp.util.OnWake;
import me.legrange.panstamp.util.Update;

/**
 *
 * @author gideon
 */
public class SetAddress extends Example {

    private static final byte NEW_ADDRESS = 2;

    public static void main(String... args) throws Exception {
        SetAddress app = new SetAddress();
        app.connect();
        app.run();
    }

    @Override
    protected void run() throws GatewayException {

        gw.addListener(this);
        say("Waiting to detect node");

    }

    @Override
    public void gatewayUpdated(GatewayEvent ev) {
        PanStamp ps = ev.getDevice();
        say("Detected node %d", ps.getAddress());
        try {
            if (ps.getAddress() == 0xFF) { 
                OnWake ow = new OnWake(ps);
                ow.queue(new Update(Endpoints.getEndpoint(ps, StandardEndpoint.DEVICE_ADDRESS), 6));
            }
            else if (ps.getAddress()== 6) {
                say("Exiting");
                System.exit(1);
            }
        } catch (GatewayException ex) {
            ex.printStackTrace();
            System.exit(1);
        } 
    }

}
