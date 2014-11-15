package me.legrange.panstamp.util;

/**
 *
 * @author gideon
 */
public class Registers {
    
    public enum SyncState {  RESTART(0), RXON(1), RXOFF(2), SYNC(3), LOWBAT(4); 
    
        private SyncState(int val) {
            this.val = val;
        }
    
        private final int val;
    };
    
}

