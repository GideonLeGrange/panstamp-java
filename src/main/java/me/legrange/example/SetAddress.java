package me.legrange.example;

import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.swap.Registers;

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

    protected void run() throws GatewayException {

        gw.addListener(this);
        say("Waiting to detect node");

    }

    @Override
    public void gatewayUpdated(GatewayEvent ev) {
        PanStamp ps = ev.getDevice();
        say("Detected node %d", ps.getAddress());
        try {
            Register addr = ps.getRegister(Registers.Register.DEVICE_ADDRESS.position());
            say("Setting remote address %d", NEW_ADDRESS);
            addr.setValue(new byte[]{NEW_ADDRESS});
            Thread.sleep(15000);
            System.exit(1);
        } catch (GatewayException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {

        }
    }

}
