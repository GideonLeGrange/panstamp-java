package me.legrange.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.json.GatewayConverter;

/**
 *
 * @author gideon
 */
public class SaveJson extends Example {

    public static void main(String... args) throws Exception {
        SaveJson app = new SaveJson();
        app.connect();
        app.run();
    }

    private static final int SLEEP = 10;
    private boolean done = false;

    @Override
    protected void run() throws GatewayException {
        PrintWriter out = null;
        try {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SaveJson.class.getName()).log(Level.SEVERE, null, ex);
            }
            String json = (new GatewayConverter().toJson(gw));
            out = new PrintWriter(new FileWriter("gateway.json"));
            out.println(json);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(SaveJson.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
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
