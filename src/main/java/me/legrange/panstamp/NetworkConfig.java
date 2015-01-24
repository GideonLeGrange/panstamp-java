package me.legrange.panstamp;

/**
 * An abstraction of the network configuration for a PanStamp network
 * @author gideon
 */
public interface NetworkConfig {

    /** Get the network ID
     * @return network ID */
     int getNetworkId();
    
    /** Get the frequency channel
     * @return TThe channel  */
    int getFrequencyChannel();

    /** Get the security option
     * @return the security option value. */
    int getSecurityOption();

    
}
