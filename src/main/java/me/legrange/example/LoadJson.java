package me.legrange.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.impl.StandardRegister;
import me.legrange.panstamp.store.JsonDataStore;
import me.legrange.panstamp.store.DataStore;
import me.legrange.panstamp.store.DataStoreException;
import me.legrange.panstamp.store.RegisterState;

/**
 *
 * @author gideon
 */
public class LoadJson extends Example {

    public static void main(String... args) throws Exception {
        LoadJson app = new LoadJson();
        app.connect();
        app.run();
    }

    private DataStore store;

    @Override
    protected void run() throws GatewayException {
         gw.addListener(this);
         store = new JsonDataStore("store.json");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoadJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void gatewayUpdated(GatewayEvent ev) {
        PanStamp ps = ev.getDevice();
        try {
            RegisterState load = store.load(ps.getAddress());
            System.out.printf("Saved state for %d: %s\n", ps.getAddress(), printState(load));
        } catch (DataStoreException ex) {
            Logger.getLogger(LoadJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private String printState(RegisterState s) {
        StringBuilder buf = new StringBuilder();
        for (StandardRegister sr : StandardRegister.ALL) {
            byte val[] = s.getState(sr);
            if (val.length > 0) {
                buf.append(sr.getName());
            }
        }
        return buf.toString();
    }

}
