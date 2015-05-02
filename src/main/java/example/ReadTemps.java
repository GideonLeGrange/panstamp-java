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

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.DeviceStateStore;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.Network;
import me.legrange.panstamp.NetworkException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.event.AbstractEndpointListener;
import me.legrange.panstamp.event.AbstractNetworkListener;
import me.legrange.swap.SwapModem;
import me.legrange.swap.tcp.TcpModem;

/**
 *
 * @author gideon
 */
public class ReadTemps {

    private static final String HOST = "localhost";
    private static final int PORT = 3333;
    protected Network nw;
    private PrintWriter out;

    public static void main(String... args) throws Exception {
        new ReadTemps().run();
    }

    protected void run() throws NetworkException, IOException {
        SwapModem modem = new TcpModem(HOST, PORT);
        out = new PrintWriter(new FileWriter("temps.csv"));
        nw = Network.create(modem);
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
        nw.open();
        doExampleCode(nw);
        System.out.println("Press enter to quit");
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        r.readLine();
        nw.close();
        out.close();
    }

    protected void doExampleCode(Network nw) throws NetworkException {
        nw.addListener(new AbstractNetworkListener() {

            @Override
            public void deviceDetected(Network gw, final PanStamp dev) {
                switch (dev.getAddress()) {
                    case 3:
                    case 4:
                        say("Detected known device %d. Adding", dev.getAddress());
                         {
                            try {
                                Register reg = dev.getRegister(12);
                                while (!reg.hasEndpoint("Temperature")) {
                                    Thread.sleep(1000);
                                }
                                Endpoint<Double> ep = reg.getEndpoint("Temperature");
                                ep.addListener(new AbstractEndpointListener<Double>() {

                                    @Override
                                    public void valueReceived(Endpoint<Double> ep, Double value) {
                                        out.printf("%s,%d,%.2f\n", new Date(), dev.getAddress(), value);
                                        out.flush();
                                    }
                                    
                                });
                            } catch (NetworkException ex) {
                                Logger.getLogger(ReadTemps.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                        Logger.getLogger(ReadTemps.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        }
                        break;
                }
            }

        });
    }

    private synchronized void say(String fmt, Object... args) {
        if (!fmt.endsWith("\n")) {
            fmt = fmt + "\n";
        }
        System.out.printf(fmt, args);
        System.out.flush();
    }

}
