package me.legrange.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
public class LoadJson extends Example {

    public static void main(String... args) throws Exception {
        LoadJson app = new LoadJson();
//        app.connect();
        app.run();
    }

    private static final int SLEEP = 10;
    private boolean done = false;

    @Override
    protected void run() throws GatewayException {
        BufferedReader in = null;
        try {
            GatewayConverter gc = new GatewayConverter();
            StringBuilder buf = new StringBuilder();
            in = new BufferedReader(new FileReader("gateway.json"));
            while (in.ready()) {
                buf.append(in.readLine());
                buf.append("\n");
            }
            in.close();
            gc.fromJson(buf.toString());
                    } catch (FileNotFoundException ex) {
            Logger.getLogger(LoadJson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoadJson.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(LoadJson.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
