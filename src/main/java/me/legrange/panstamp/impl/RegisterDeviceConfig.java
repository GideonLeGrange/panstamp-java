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
