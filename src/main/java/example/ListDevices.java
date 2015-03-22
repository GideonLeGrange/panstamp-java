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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Network;
import me.legrange.panstamp.NetworkException;
import me.legrange.panstamp.PanStamp;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class ListDevices extends Example {
    
    public static void main(String...args) throws Exception {
        new ListDevices().run();
    }

    @Override
    protected void doExampleCode(Network nw) throws NetworkException {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ListDevices.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<PanStamp> devices = nw.getDevices();
        System.out.println("Listing devices");
        for (PanStamp device : devices) {
            System.out.printf("panStamp with address %d is on the network\n", device.getAddress(), device.getName());
        }
        
        boolean hasIt = nw.hasDevice(2);
        System.out.printf("Do we have device #2? %s\n", (hasIt ? "yes" : "no"));

        PanStamp dev = nw.getDevice(2);
        
    }

}
