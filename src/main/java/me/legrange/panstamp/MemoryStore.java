package me.legrange.panstamp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.legrange.panstamp.DeviceStateStore;
import me.legrange.panstamp.Register;

/**
 * An implementation of DeviceStateStore that keeps whatever is learned in
 * memory. Used as default implementation.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class MemoryStore implements DeviceStateStore {

    MemoryStore() {
    }

    @Override
    public boolean hasRegisterValue(Register reg) {
        return mapForAddress(reg.getDevice().getAddress()).get(reg.getId()) != null;
    }

    @Override
    public byte[] getRegisterValue(Register reg) {
        return mapForAddress(reg.getDevice().getAddress()).get(reg.getId());
    }

    @Override
    public void setRegisterValue(Register reg, byte value[]) {
         mapForAddress(reg.getDevice().getAddress()).put(reg.getId(), value);
    }
    
    private Map<Integer, byte[]> mapForAddress(int address) {
        Map<Integer, byte[]> map = cache.get(address);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            cache.put(address, map);
        }
        return map;
    }

    private final Map<Integer, Map<Integer, byte[]>> cache = new ConcurrentHashMap<>();

}
