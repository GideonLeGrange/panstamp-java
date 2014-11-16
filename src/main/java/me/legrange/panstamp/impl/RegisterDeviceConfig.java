package me.legrange.panstamp.impl;

import me.legrange.panstamp.DeviceConfig;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.Register;

/**
 *
 * @author gideon
 */
public class RegisterDeviceConfig implements DeviceConfig {
    
    public RegisterDeviceConfig(PanStampImpl ps) {
        this.ps = ps;
    }
    
    @Override
    public int getAddress() {
        return ps.getAddress();
    }
    
    @Override
    public int getChannel() throws GatewayException {
        Integer v = getIntValue(StandardEndpoint.FREQUENCY_CHANNEL);
        if (v != null) {
            return v;
        }
        return ps.getGateway().getChannel();
    }
    
    @Override
    public int getTxInterval() throws GatewayException {
        Integer v = getIntValue(StandardEndpoint.PERIODIC_TX_INTERVAL);
        if (v != null) {
            return v;
        }
        return 0;
    }
    
    @Override
    public int getSecurityOption() throws GatewayException {
        Integer v = getIntValue(StandardEndpoint.SECURITY_OPTION);
        if (v != null) {
            return v;
        }
        return 0;
    }

    /**
     *
     * @return The network address
     * @throws GatewayException Thrown if there is an error trying to figure out
     * the network address.
     */
    @Override
    public int getNetwork() throws GatewayException {
        Integer v = getIntValue(StandardEndpoint.NETWORK_ID);
        if (v != null) {
            return v;
        }
        return ps.getGateway().getNetworkId();
    }
    
    @Override
    public void setAddress(int addr) throws GatewayException {
        if (addr != getAddress()) {
            setIntValue(StandardEndpoint.DEVICE_ADDRESS, addr);
        }
    }
    
    @Override
    public void setNetwork(int network) throws GatewayException {
        if (network != getNetwork()) {
            setIntValue(StandardEndpoint.NETWORK_ID, network);
        }
    }
    
    @Override
    public void setChannel(int channel) throws GatewayException {
        if (channel != getChannel()) {
            setIntValue(StandardEndpoint.FREQUENCY_CHANNEL, channel);
        }
    }
    
    @Override
    public void setSecurityOption(int option) throws GatewayException {
        if (option != getSecurityOption()) {
            setIntValue(StandardEndpoint.SECURITY_OPTION, option);
        }
    }
    
    @Override
    public void setTxInterva(int txInterval) throws GatewayException {
        if (txInterval != getTxInterval()) {
            setIntValue(StandardEndpoint.PERIODIC_TX_INTERVAL, txInterval);
        }
    }
    
    private void setIntValue(StandardEndpoint epDef, int val) throws GatewayException {
        Register reg = ps.getRegister(epDef.getRegister().getId());
        Endpoint<Integer> ep = reg.getEndpoint(epDef.getName());
        ep.setValue(val);
    }
    
    private Integer getIntValue(StandardEndpoint epDef) throws GatewayException {
        Register reg = ps.getRegister(epDef.getRegister().getId());
        if (reg.hasValue()) {
            Endpoint<Integer> ep = reg.getEndpoint(epDef.getName());
            return ep.getValue();
        }
        return null;
    }
    
    private final PanStampImpl ps;
    
}
