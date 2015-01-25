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
    
    MapState(Map<StandardRegister, byte[]> state) {
       this.state = state;
    }
    
    private final Map<StandardRegister, byte[]> state;
    
}
