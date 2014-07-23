package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Unit;

/**
 *
 * @author gideon
 */
public class NumberInputEndpoint extends AbstractInputEndpoint<Double> {

    public NumberInputEndpoint(PanStamp ps, EndpointDef epDef) {
        super(ps, epDef); 
    }
    
    @Override
    public Double getValue(String unit) throws GatewayException {
        byte bytes[] = ps.getRegister(epDef.getRegister().getId()).getValue();
        long val = 0;
        for (int i = 0; i < epDef.getSize().getBytes(); ++i) {
            val = val << 8;
            val = val | bytes[epDef.getPosition().getBytePos() + i];
        }
        Unit u = getUnit(unit);
        return ((double)val) * u.getFactor() + u.getOffset();
        
    }
}
