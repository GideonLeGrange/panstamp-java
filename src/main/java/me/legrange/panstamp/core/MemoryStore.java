package me.legrange.panstamp.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.legrange.panstamp.DeviceStateStore;
import me.legrange.panstamp.StandardRegister;

/**
 * An implementation of DeviceStateStore that keeps whatever is learned in
 * memory
 *
 * @author gideon
 */
public class MemoryStore implements DeviceStateStore {

    public MemoryStore() {
    }

    @Override
    public boolean hasRegisterValue(int address, StandardRegister reg) {
        return getRegisterValue(address, reg) != null;
    }

    @Override
    public byte[] getRegisterValue(int address, StandardRegister reg) {
        Map<StandardRegister, byte[]> forAddress = cache.get(address);
        if (forAddress != null) {
            return forAddress.get(reg);
        }
        return null;
    }

    @Override
    public void setRegisterValue(int address, StandardRegister reg, byte[] value) {
        Map<StandardRegister, byte[]> forAddress = cache.get(address);
        if (forAddress == null) {
            forAddress = cache.put(address, new ConcurrentHashMap<StandardRegister, byte[]>());
        }
        forAddress.put(reg, value);
    }

    private final Map<Integer, Map<StandardRegister, byte[]>> cache = new ConcurrentHashMap<>();

}
