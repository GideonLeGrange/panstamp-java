package me.legrange.panstamp.impl;

import me.legrange.panstamp.NetworkConfig;

/**
 * An abstraction of the network configuration for a PanStamp network
 * @author gideon
 */
public class NetworkConfigImpl implements NetworkConfig {

    public NetworkConfigImpl(int networkId, int frequencyChannel, int securityOption) {
        this.networkId = networkId;
        this.frequencyChannel = frequencyChannel;
        this.securityOption = securityOption;
    }

  
    @Override
    public int getNetworkId() {
        return networkId;
    }

    @Override
    public int getFrequencyChannel() {
        return frequencyChannel;
    }

    @Override
    public int getSecurityOption() {
        return securityOption;
    }

    
    private final int networkId;
    private final int frequencyChannel;
    private final int securityOption;
    
}
