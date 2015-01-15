package me.legrange.swap;

/**
 * A class that encapsulates the modem sketch's settings.
 *
 * @author gideon
 */
public class ModemSetup {

    public ModemSetup(int channel, int networkID, int deviceAddress) {
        this.channel = channel;
        this.networkID = networkID;
        this.deviceAddress = deviceAddress;
    }

    public int getChannel() {
        return channel;
    }

    public int getNetworkID() {
        return networkID;
    }

    public int getDeviceAddress() {
        return deviceAddress;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setNetworkID(int networkID) {
        this.networkID = networkID;
    }

    public void setDeviceAddress(int deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    @Override
    public String toString() {
        return String.format("channel = %d, network = %4x, address = %d", channel, networkID, deviceAddress);
    }

    
    private  int channel;
    private  int networkID;
    private  int deviceAddress;
}
