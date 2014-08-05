package me.legrange.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;

/**
 *
 * @author gideon
 */
public class ReadTemps extends Example implements EndpointListener<Double> {
   
    public static void main(String...args) throws Exception {
        ReadTemps rt = new ReadTemps();
        rt.connect();
        rt.run();
    }
    
    @Override
    public void deviceDetected(PanStamp ps) {
        try {
            if (ps.hasEndpoint("Temperature")) {
                Endpoint ep = ps.getEndpoint("Temperature");
                ep.addListener(this);
            }
        } catch (GatewayException ex) {
            Logger.getLogger(ReadTemps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void valueReceived(Endpoint<Double> ep) {
        try {
            System.out.printf("It is %.2fC\n", ep.getValue("C"));
        } catch (GatewayException ex) {
            Logger.getLogger(ReadTemps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
