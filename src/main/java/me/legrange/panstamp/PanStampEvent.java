package me.legrange.panstamp;

/**
 *
 * @author gideon
 */
public interface PanStampEvent {
    
    public enum Type { PRODUCT_CODE_UPDATE, SYNC_STATE_CHANGE, REGISTER_DETECTED, SYNC_REQUIRED; };

    public Type getType();
    
    public PanStamp getDevice();
    
    public Register getRegister();
    
    
}
