package me.legrange.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.json.JsonDataStore;
import me.legrange.panstamp.json.PanStampState;
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
    private final JsonDataStore store = new JsonDataStore("store.json");

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
            store.save(ps.getAddress(), new PanStampState(ps));
        } catch (DataStoreException ex) {
            Logger.getLogger(SaveJson.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }

}
