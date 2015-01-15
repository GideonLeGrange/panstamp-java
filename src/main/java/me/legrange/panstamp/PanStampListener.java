package me.legrange.panstamp;

/**
 * A listener that can be implemented to receive events from a panStamp device.
 *
 * @author gideon
 */
public interface PanStampListener {

    /**
     * Called on listeners to notify them of panStamp events.
     *
     * @param ev The event that occurred.
     */
    void deviceUpdated(PanStampEvent ev);

}
