/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.legrange.example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.swap.Registers;

/**
 *
 * @author gideon
 */
public class SetSleep extends Example {
   public static void main(String... args) throws Exception {
        SetSleep app = new SetSleep();
        app.connect();
        app.run();
    }
 
    private static final byte SLEEP=10;
    private boolean done = false;

    @Override
    protected void run() throws GatewayException {
        while (!done) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SetSleep.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.exit(0);
    }

    @Override
    public void deviceDetected(PanStamp ps) {
        try {
            say("Detected device %d", ps.getAddress());
            Register reg = ps.getRegister(Registers.Register.PERIODIC_TX_INTERVAL.position()); 
            byte b[] = reg.getValue();
            int s = b[0] <<8 | b[1];
            say("Current sleep value is %d", s);
            reg.setValue(new byte[]{0,SLEEP});
            say("Tried to set sleep time, let's see!");
            done = true;
        } catch (GatewayException ex) {
            Logger.getLogger(SetSleep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
