package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Unit;

/**
 *
 * @author gideon
 */
public class IntegerEndpoint extends AbstractEndpoint<Integer> {

    public IntegerEndpoint(RegisterImpl reg, EndpointDef epDef) {
        super(reg, epDef);
    }

    @Override
    public Integer getValue() throws GatewayException {
        byte bytes[] = reg.getValue();//ps.getRegister(epDef.getRegister().getId()).getValue();
        int val = 0;
        for (int i = 0; i < epDef.getSize().getBytes(); ++i) {
            val = val << 8;
            val = val | (bytes[epDef.getPosition().getBytePos() + i]) & 0xFF;
        }
        return val;
    }

    @Override
    public void setValue(Integer value) throws GatewayException {
        long val = value.longValue();
        byte bytes[] = new byte[epDef.getSize().getBytes()];
        for (int i = epDef.getSize().getBytes() - 1; i >= 0; --i) {
            bytes[i] = (byte) (val & 0xFF);
            val = val >>> 8;
        }
        reg.setValue(bytes);
    }

    @Override
    protected Integer transformIn(Integer value, Unit unit) {
        Double d = (value * unit.getFactor() + unit.getOffset());
        return d.intValue();
    }

    @Override
    protected Integer transformOut(Integer value, Unit unit) {
        Double d = (value - unit.getOffset()) / unit.getFactor();
        return d.intValue();
    }
}
