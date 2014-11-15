package me.legrange.panstamp;

/**
 *
 * @author gideon
 */
public interface PanStampEvent {
    
    public enum Type { PRODUCT_CODE_UPDATE, SYNC_STATE_CHANGE, REGISTER_DETECTED; };

    public Type getType();
    
    public PanStamp getDevice();
    
    public Register getRegister();
    
    
}
