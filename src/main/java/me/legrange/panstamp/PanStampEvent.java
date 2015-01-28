package me.legrange.panstamp;

/**
 * Event used to notify listeners of events on a panStamp device. 
 * 
 * @author gideon
 */
public interface PanStampEvent {
   
    /** The types of events that can be raised for a device. */
    public enum Type { PRODUCT_CODE_UPDATE, SYNC_STATE_CHANGE, REGISTER_DETECTED, SYNC_REQUIRED; };

    /** Get type of the specific event
     * @return  The type of the event. */
    public Type getType();
    
    /** Get the device which raised the event
     * @return The device. */
    public PanStamp getDevice();
    
    /** get the register involved in this event, if applicable *
     * @return The register. May be null */
    public Register getRegister();
    
    /** get the sync state represented by this event 
     * @return The sync state
     */
    int getSyncState();
    
}
