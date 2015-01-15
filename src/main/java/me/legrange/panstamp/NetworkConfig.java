package me.legrange.panstamp;

/**
 * An abstraction of the network configuration for a PanStamp network
 * @author gideon
 */
public class NetworkConfig {

    public NetworkConfig(int networkId, int frequencyChannel, int securityOption) {
        this.networkId = networkId;
        this.frequencyChannel = frequencyChannel;
        this.securityOption = securityOption;
    }

    /** Get the network ID
     * @return network ID */
    public int getNetworkId() {
        return networkId;
    }

    /** Get the frequency channel
     * @return TThe channel  */
    public int getFrequencyChannel() {
        return frequencyChannel;
    }

    /** Get the security option
     * @return the security option value. */
    public int getSecurityOption() {
        return securityOption;
    }

    
    private final int networkId;
    private final int frequencyChannel;
    private final int securityOption;
    
}
