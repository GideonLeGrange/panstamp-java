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

import me.legrange.panstamp.Network;
import me.legrange.panstamp.NetworkException;
import me.legrange.panstamp.NetworkListener;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.event.AbstractNetworkListener;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class NetworkEvents extends Example {

    @Override
    protected void doExampleCode(Network nw) throws NetworkException {
        nw.addListener(new NetworkListener() {

            @Override
            public void deviceDetected(Network gw, PanStamp dev) {
                System.out.printf("Device with address %d added to network", dev.getAddress());
            }

            @Override
            public void deviceRemoved(Network gw, PanStamp dev) {
                System.out.printf("Device with address %d removed to network", dev.getAddress());
            }
        });
        
        nw.addListener(new AbstractNetworkListener() {

            @Override
            public void deviceDetected(Network gw, PanStamp dev) {
                System.out.printf("Device with address %d added to network", dev.getAddress());
            }
            
        });
        
    }

    
    

}
