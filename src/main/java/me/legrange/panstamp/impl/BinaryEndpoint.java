package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Unit;

/**
 *
 * @author gideon
 */
public class BinaryEndpoint extends AbstractEndpoint<Boolean> {

    public BinaryEndpoint(PanStampImpl ps, EndpointDef epDef) {
        super(ps, epDef);
    }

    @Override
    public Boolean getValue() throws GatewayException {
        Register reg = ps.getRegister(epDef.getRegister().getId());
        byte val[] = reg.getValue();
        int byteIdx = epDef.getPosition().getBytePos();
        int bitIdx = epDef.getPosition().getBitPos();
        return (val[byteIdx] & ~(0b1 << bitIdx)) != 0;
    }

    @Override
    public void setValue(Boolean value) throws GatewayException {
        Register reg = ps.getRegister(epDef.getRegister().getId());
        byte val[] = reg.getValue();
        int byteIdx = epDef.getPosition().getBytePos();
        int bitIdx = epDef.getPosition().getBitPos();
        val[byteIdx] = (byte)(val[byteIdx] & ~(0b1 << bitIdx) |                                  ((byte)(value ? 0b1 : 0b0) << bitIdx));  
        reg.setValue(val);
    }
    
    @Override
    protected Boolean transformIn(Boolean value, Unit unit) {
        return value;
    }
     @Override
    protected Boolean transformOut(Boolean value, Unit unit) {
        return value;
    }
    
    
}
