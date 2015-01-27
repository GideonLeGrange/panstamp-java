package me.legrange.panstamp.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import me.legrange.panstamp.DataStore;
import me.legrange.panstamp.DataStoreException;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.RegisterState;

/**
 * A simple non-persistent in-memory implementation of DataStore to use in 
 * cases where a persistent store is not required or wanted. 
 * @author gideon
 */
public class MemoryStore implements DataStore {

    @Override
    public Set<Integer> getAddresses(Integer networkId) throws DataStoreException {
        return getNetworkMap(networkId).keySet();
    }

    @Override
    public void save(RegisterState state) throws DataStoreException {
        try {
            getNetworkMap(state.getNetworkId()).put(state.getAddress(), state);
        } catch (GatewayException ex) {
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }

    @Override
    public RegisterState load(Integer networkId, Integer addr) throws DataStoreException {
        return getNetworkMap(networkId).get(addr);
    }

    private Map<Integer, RegisterState> getNetworkMap(Integer networkId) {
        Map<Integer, RegisterState> addrMap = data.get(networkId);
        if (addrMap == null) {
            addrMap = new HashMap<>();
            data.put(networkId, addrMap);
        }
        return addrMap;
    }

    private final Map<Integer, Map<Integer, RegisterState>> data = new HashMap<>();
}
