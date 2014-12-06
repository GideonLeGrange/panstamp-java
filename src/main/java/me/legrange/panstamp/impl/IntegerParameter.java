package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Param;
import me.legrange.panstamp.def.Unit;

/**
 *
 * @author gideon
 */
public class IntegerParameter extends AbstractParameter<Integer> {

    public IntegerParameter(RegisterImpl reg, Param par) {
        super(reg, par);
    }

    @Override
    public Type getType() {
        return Type.INTEGER;
    }
    
    @Override
    public Integer getValue() throws GatewayException {
        byte bytes[] = reg.getValue();//ps.getRegister(epDef.getRegister().getId()).getValue();
        int val = 0;
        for (int i = 0; i < par.getSize().getBytes(); ++i) {
            val = val << 8;
            val = val | (bytes[par.getPosition().getBytePos() + i]) & 0xFF;
        }
        return val;
    }

    @Override
    public void setValue(Integer value) throws GatewayException {
        long val = value.longValue();
        byte bytes[] = new byte[par.getSize().getBytes()];
        for (int i = par.getSize().getBytes() - 1; i >= 0; --i) {
            bytes[i] = (byte) (val & 0xFF);
            val = val >>> 8;
        }
        reg.setValue(bytes);
    }
}
