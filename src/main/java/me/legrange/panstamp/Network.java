package me.legrange.panstamp;

/**
 * An abstraction of the network configuration for a PanStamp network
 * @author gideon
 */
public class Network {

    public Network(int networkId, int frequencyChannel, int securityOption) {
        this.networkId = networkId;
        this.frequencyChannel = frequencyChannel;
        this.securityOption = securityOption;
    }

    /** return the network ID */
    public int getNetworkId() {
        return networkId;
    }

    /** return the frequency channel */
    public int getFrequencyChannel() {
        return frequencyChannel;
    }

    /** return the security option */
    public int getSecurityOption() {
        return securityOption;
    }

    
    private final int networkId;
    private final int frequencyChannel;
    private final int securityOption;
    
}
