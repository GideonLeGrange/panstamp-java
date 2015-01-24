package me.legrange.panstamp.store;

import me.legrange.panstamp.impl.StandardRegister;

/**
 *
 * @author gideon
 */
public interface PanStampState {
    
    byte[] getState(StandardRegister reg);
    
}
