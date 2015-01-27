package me.legrange.panstamp.store;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.StandardRegister;

/**
 *
 * @author gideon
 */
public class PanStampState implements RegisterState {

    @Override
    public int getNetworkId() throws GatewayException {
        return ps.getNetwork();
    }

    @Override
    public int getAddress() {
        return ps.getAddress();
    }

    @Override
    public byte[] getState(StandardRegister sr) {
        try {
            if (ps.hasRegister(sr.getId())) {
                Register reg = ps.getRegister(sr.getId());
                if (reg.hasValue()) {
                    return reg.getValue();
                }
            }
        } catch (GatewayException ex) {
        }
        return new byte[]{};
    }

    public PanStampState(PanStamp ps) {
        this.ps = ps;
    }
        
    private final PanStamp ps;

}
