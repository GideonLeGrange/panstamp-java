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
import me.legrange.panstamp.PanStamp;

/**
 *
 * @author gideon
 */
public class ReadAndSetPanStampInformation extends Example {

    @Override
    protected void doExampleCode(Network nw) throws NetworkException {
        PanStamp ps = nw.getDevice(5);
        System.out.printf("PanStamp name: %s\n", ps.getName());
        System.out.printf("PanStamp address: %d\n", ps.getAddress());
        System.out.printf("PanStamp manufacturer ID: %d\n", ps.getManufacturerId());
        System.out.printf("PanStamp product ID: %d\n", ps.getProductId());
    }
    
    
    
}
