package me.legrange.panstamp;

/**
 *
 * @author gideon
 */
public interface PanStampEvent {
    
    public enum Type { PRODUCT_CODE_UPDATE, SYNC_STATE_CHANGE; };

    public Type getType();
    
    public PanStamp getDevice();
    
}
