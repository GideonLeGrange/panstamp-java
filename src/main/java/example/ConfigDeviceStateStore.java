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

import java.util.Map;
import me.legrange.panstamp.DeviceStateStore;
import me.legrange.panstamp.Register;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class ConfigDeviceStateStore implements DeviceStateStore {

    // assume productCodes is filled in from the config file by the application
    private Map<Integer, byte[]> productCodes;

    @Override
    public boolean hasRegisterValue(Register reg) {
        return (reg.getId() == 0) && (productCodes.get(reg.getDevice().getAddress()) != null);
    }

    @Override
    public byte[] getRegisterValue(Register reg) {
        return productCodes.get(reg.getId());
    }

    @Override
    public void setRegisterValue(Register reg, byte[] value) {
        // don't have to do anything since it is a config file, but a good idea would be to log 
        // if the product code set by the library does not match the one we have in the config file
    }
    
}
