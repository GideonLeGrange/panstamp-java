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

/**
 *
 * @author gideon
 */
public class ReadAndSetNetworkInformation extends Example {

    @Override
    protected void doExampleCode(Network nw) throws NetworkException {
        // print the 
        System.out.println("Network settings:");
        System.out.printf("Frequency channel: %d\n", nw.getChannel());
        System.out.printf("Network ID: %04x\n", nw.getNetworkId());
        System.out.printf("Device address: %d\n", nw.getDeviceAddress());
        // 
        nw.setNetworkId(0x1234);
    }
    
    
    
}
