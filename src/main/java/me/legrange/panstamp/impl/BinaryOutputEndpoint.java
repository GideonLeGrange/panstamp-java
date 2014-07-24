package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.def.EndpointDef;

/**
 *
 * @author gideon
 */
class BinaryOutputEndpoint extends AbstractOutputEndpoint<Boolean> {

    public BinaryOutputEndpoint(PanStamp ps, EndpointDef epDef) {
        super(ps, epDef);
    }

    @Override
    public void setValue(String unit, Boolean value) throws GatewayException {
        Register reg = ps.getRegister(epDef.getRegister().getId());
        byte cur[] = reg.getValue();
        int byteIdx = epDef.getPosition().getBytePos();
        int bitIdx = epDef.getPosition().getBitPos();
        int b = value ? 0b1 : 0b0;
        int val = cur[byteIdx] | (byte)b; // FIX ME need to complete set.
    }
    
}
