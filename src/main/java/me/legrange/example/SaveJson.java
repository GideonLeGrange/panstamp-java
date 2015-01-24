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
import me.legrange.panstamp.json.JsonDataStore;
import me.legrange.panstamp.store.DataStoreException;

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
    private JsonDataStore store = new JsonDataStore("store.json");

    @Override
    protected void run() throws GatewayException {
            try {
                gw.addListener(this);
                Thread.sleep(20000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SaveJson.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.exit(0);
    }

    @Override
    public void gatewayUpdated(GatewayEvent ev) {
        try {
            PanStamp ps = ev.getDevice();
            System.out.printf("Storing for %d\n", ps.getAddress());
            store.save(ps);
            /*     try {
            say("Detected device %d", ps.getAddress());
            OnWake ow = new OnWake(ps); 
            ow.queue(Util.getEndpoint(ps, StandardEndpoint.PERIODIC_TX_INTERVAL), SLEEP);
            say("Queued change of sleep for %2x to 10", ps.getAddress());
            } catch (GatewayException ex) {
            Logger.getLogger(SetSleep.class.getName()).log(Level.SEVERE, null, ex);
            } */
        } catch (DataStoreException ex) {
            Logger.getLogger(SaveJson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
