package me.legrange.panstamp.store;

import java.util.Map;
import me.legrange.panstamp.StandardRegister;

/**
 *
 * @author gideon
 */
class MapState implements RegisterState {

    @Override
    public byte[] getState(StandardRegister reg) {
        byte bytes[] = state.get(reg);
        if (bytes == null) {
            return new byte[]{};
        }
        return bytes;
    }

    @Override
    public int getNetworkId() {
        return networkId;
    }

    @Override
    public int getAddress() {
        return address;
    }

    MapState(int networkId, int address, Map<StandardRegister, byte[]> state) {
        this.networkId = networkId;
        this.address = address;
        this.state = state;
    }
    
    private final int networkId;
    private final int address;
    private final Map<StandardRegister, byte[]> state;
    
}
