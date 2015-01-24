package me.legrange.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.impl.ModemException;
import me.legrange.panstamp.json.JsonStore;
import me.legrange.swap.SWAPException;

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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SaveJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.out.println("J: " + JsonStore.networkAsJson(gw));
        } catch (SWAPException ex) {
            Logger.getLogger(SaveJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(0);
    }

    @Override
    public void gatewayUpdated(GatewayEvent ev) {
    }

}
