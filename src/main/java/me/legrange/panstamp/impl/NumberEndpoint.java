package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Unit;

/**
 *
 * @author gideon
 */
public class NumberEndpoint extends AbstractEndpoint<Double> {

    public NumberEndpoint(PanStampImpl ps, EndpointDef epDef) {
        super(ps, epDef); 
    }
    
    @Override
    public Double getValue() throws GatewayException {
        byte bytes[] = ps.getRegister(epDef.getRegister().getId()).getValue();
        long val = 0;
        for (int i = 0; i < epDef.getSize().getBytes(); ++i) {
            val = val << 8;
            val = val | (bytes[epDef.getPosition().getBytePos() + i]) & 0xFF;
        }
        return (double)val;
    }

    @Override
    public void setValue(Double value) throws GatewayException {
        long val = value.longValue();
        byte bytes[] = new byte[epDef.getSize().getBytes()];
        for (int i = epDef.getSize().getBytes() -1; i >=0; --i) {
            bytes[i] = (byte)(val & 0xFF);
            val = val >>> 8;
        }
        ps.getRegister(epDef.getRegister().getId()).setValue(bytes);
    }

    @Override
    protected Double transformIn(Double value, Unit unit) {
        return value  * unit.getFactor() + unit.getOffset();
    }

    @Override
    protected Double transformOut(Double value, Unit unit) {
       return (value  - unit.getOffset()) / unit.getFactor();
    }
}
