package me.legrange.panstamp.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.legrange.panstamp.DeviceStateStore;
import me.legrange.panstamp.StandardEndpoint;

/**
 * An implementation of DeviceStateStore that keeps whatever is learned in
 * memory
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class MemoryStore implements DeviceStateStore {

    public MemoryStore() {
    }

    @Override
    public boolean hasEndpointValue(int address, StandardEndpoint ep) {
        return mapForAddress(address).get(ep) != null;
    }

    @Override
    public int getEndpointValue(int address, StandardEndpoint ep) {
        return mapForAddress(address).get(ep);
    }

    @Override
    public void setEndpointValue(int address, StandardEndpoint ep, int value) {
        mapForAddress(address).put(ep, value);
    }

    private Map<StandardEndpoint, Integer> mapForAddress(int address) {
        Map<StandardEndpoint, Integer> map = cache.get(address);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            cache.put(address, map);
        }
        return map;
    }

    private final Map<Integer, Map<StandardEndpoint, Integer>> cache = new ConcurrentHashMap<>();

}
