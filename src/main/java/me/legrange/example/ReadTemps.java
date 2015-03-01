package me.legrange.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampEvent;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.Register;

/**
 *
 * @author gideon
 */
public class ReadTemps extends Example implements EndpointListener<Double>, PanStampListener {

    public static void main(String... args) throws Exception {
        ReadTemps rt = new ReadTemps();
        rt.connect();
        rt.run();
    }

    @Override
    public void gatewayUpdated(GatewayEvent ev) {
        PanStamp ps = ev.getDevice();
        System.out.printf("Detected PanStamp '%d'. Adding listener\n", ps.getAddress());
        ps.addListener(this);
    }

    @Override
    public void deviceUpdated(PanStampEvent ev) {
        System.out.printf("Device update for '%d'. Checking for register\n", ev.getDevice().getAddress());
        try {
            PanStamp ps = ev.getDevice();
            if (ps.hasRegister(12)) {
                Register reg = ps.getRegister(12);
                if (reg.hasEndpoint("Temperature")) {
                    Endpoint ep = reg.getEndpoint("Temperature");
                    ep.addListener(this);
                }
            }
        } catch (GatewayException ex) {
            Logger.getLogger(ReadTemps.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void valueReceived(Endpoint<Double> e, Double v) {
        System.out.printf("It is %.2fC\n", v);
    }

}
