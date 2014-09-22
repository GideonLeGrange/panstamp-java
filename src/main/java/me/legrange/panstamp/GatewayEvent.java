package me.legrange.panstamp;

/**
 *
 * @author gideon
 */
public interface GatewayEvent {
    
    public enum Type { DEVICE_DETECTED; }
    
    Type getType();
    
    PanStamp getDevice();
    
}
