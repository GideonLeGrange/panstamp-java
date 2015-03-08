package me.legrange.panstamp;

/**
 * A listener that can be implemented to receive events from a panStamp device.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public interface PanStampListener {

    /** The product code for a panStamp was changed. 
     * 
     * @param dev The device involved
     * @param manufacturerId The new manufacturer Id of the device.
     * @param productId  The new productId of the device. 
     */
    void productCodeChange(PanStamp dev, int manufacturerId, int productId);
    
    /** The synchronization state of the device has changed. 
     * 
     * @param dev The device involved.
     * @param syncState The current state.
     */
    void syncStateChange(PanStamp dev, int syncState);
    
    /** A new register was added to a panStamp device. 
     * 
     * @param dev The device involved.
     * @param reg The register added. 
     */
    void registerDetected(PanStamp dev, Register reg);
    
    /** A panStamp device needs to go into one of the synchronization states that 
     * allows it to receive updates. 
     * 
     * @param dev The panStamp device needing to sync. 
     */
    void syncRequired(PanStamp dev);

}
