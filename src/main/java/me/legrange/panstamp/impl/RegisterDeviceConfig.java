package me.legrange.panstamp.impl;

import me.legrange.panstamp.DeviceConfig;

/**
 *
 * @author gideon
 */
public class RegisterDeviceConfig implements DeviceConfig {

    RegisterDeviceConfig(int network, int address, int channel, int securityOption, int txInterval) {
        this.network = network;
        this.address = address;
        this.channel = channel;
        this.securityOption = securityOption;
        this.txInterval = txInterval;
    }

    @Override
    public int getNetwork() {
        return network;
    }

    @Override
    public int getAddress() {
        return address;
    }

    @Override
    public int getChannel() {
        return channel;
    }

    @Override
    public int getSecurityOption() {
        return securityOption;
    }

    @Override
    public int getTxInterval() {
        return txInterval;
    }

    private final int network;
    private final int address;
    private final int channel;
    private final int securityOption;
    private final int txInterval;
}
