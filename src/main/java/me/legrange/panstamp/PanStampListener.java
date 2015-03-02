package me.legrange.panstamp;

/**
 * A listener that can be implemented to receive events from a panStamp device.
 *
 * @author gideon
 */
public interface PanStampListener {

    void productCodeChange(PanStamp dev, int manufacturerId, int productId);
    
    void syncStateChange(PanStamp dev, int syncState);
    
    void registerDetected(PanStamp dev, Register reg);
    
    void syncRequired(PanStamp dev);

}
