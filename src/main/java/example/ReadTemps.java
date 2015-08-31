/*
 * Copyright 2015 gideon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.DeviceStateStore;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.Network;
import me.legrange.panstamp.NetworkException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;

/**
 *
 * @author gideon
 */
public class ReadTemps extends Example {


    public static void main(String... args) throws Exception {
        new ReadTemps().run();
    }

    @Override
    protected void doExampleCode(Network nw) throws NetworkException {
        try {
            nw.setDeviceStore(new DeviceStateStore() {
                
                @Override
                public boolean hasRegisterValue(Register reg) {
                    return (reg.getId() == 0);
                }
                
                @Override
                public byte[] getRegisterValue(Register reg) {
                    if (reg.getId() == 0) {
                        switch (reg.getDevice().getAddress()) {
                            case 3:
                            case 4:
                                say("Supplying product code for device %d", reg.getDevice().getAddress());
                                return new byte[]{0, 0, 0, 1, 0, 0, 0, 4};
                        }
                    }
                    return new byte[]{};
                }
                
                @Override
                public void setRegisterValue(Register reg, byte[] value) {
                }
                
            });
            
            PanStamp ps = nw.addDevice(3);
            Thread.sleep(1000);
            Register r = ps.getRegister(12);
            final Endpoint<Double> e = r.getEndpoint("Temperature");
            e.setUnit("F");
            e.addListener(new EndpointListener<Double>() {

                @Override
                public void valueReceived(Endpoint ep, Double value) {
                    System.out.printf("Temperature is %.1f%s\n", value, e.getUnit());
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(ReadTemps.class.getName()).log(Level.SEVERE, null, ex);
        }
        


    }

    private synchronized void say(String fmt, Object... args) {
        if (!fmt.endsWith("\n")) {
            fmt = fmt + "\n";
        }
        System.out.printf(fmt, args);
        System.out.flush();
    }

}
