package me.legrange.example;

import me.legrange.panstamp.Gateway;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.GatewayListener;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.swap.Registers;

/**
 *
 * @author gideon
 */
public class SetAddress implements GatewayListener {

    private static final String PORT = "/dev/tty.usbserial-A800HNMV";
    private static final int BAUD = 38400;
    private static final byte NEW_ADDRESS = 5;
    private Gateway gw;
    
    public static void main(String... args) throws Exception {
        new SetAddress().run();
    }

    private void run() throws GatewayException {

         gw = Gateway.open(PORT, BAUD);
        gw.addListener(this);
        say("Waiting to detect node");

    }

    @Override
    public void deviceDetected(PanStamp ps) {
        say("Detected node %d", ps.getAddress());
        try {
           Register addr = ps.getRegister(Registers.Register.DEVICE_ADDRESS.position());
            say("Setting remote address %d", NEW_ADDRESS);
           addr.setValue(new byte[]{NEW_ADDRESS});
            Thread.sleep(5000);
            System.exit(1);
        } catch (GatewayException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {

        }
    }

    private void say(String fmt, Object... args) {
        System.out.printf(fmt, args);
        System.out.println();
    }

}
