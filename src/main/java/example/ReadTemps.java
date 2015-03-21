package example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.Gateway;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.event.AbstractEndpointListener;
import me.legrange.panstamp.event.AbstractPanStampListener;

/**
 *
 * @author gideon
 */
public class ReadTemps extends Example  {

    public static void main(String... args) throws Exception {
        ReadTemps rt = new ReadTemps();
        rt.connect();
        rt.run();
    }

    @Override
    public void deviceDetected(Gateway gw, PanStamp ps) {
        System.out.printf("Detected PanStamp '%d'. Adding listener\n", ps.getAddress());
        ps.addListener(psl);
    }

    @Override
    public void deviceRemoved(Gateway gw, PanStamp dev) {
   }

    

    private final EndpointListener<Double> epl = new AbstractEndpointListener<Double>() {

        @Override
        public void valueReceived(Endpoint<Double> e, Double v) {
            System.out.printf("It is %.2fC\n", v);
        }

    };
    
    private final PanStampListener psl = new AbstractPanStampListener() {

        @Override
        public void productCodeChange(PanStamp ps, int manufacturerId, int productId) {
            try {
                if (ps.hasRegister(12)) {
                    Register reg = ps.getRegister(12);
                    if (reg.hasEndpoint("Temperature")) {
                        Endpoint ep = reg.getEndpoint("Temperature");
                        ep.addListener(epl);
                    }
                }
            } catch (GatewayException ex) {
                Logger.getLogger(ReadTemps.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    };
}
