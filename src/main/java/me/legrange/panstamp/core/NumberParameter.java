package me.legrange.panstamp.core;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.def.Param;

/**
 *
 * @author gideon
 */
class NumberParameter extends AbstractParameter<Double> {

    @Override
    public Double getValue() throws GatewayException {
        byte bytes[] = reg.getValue();
        long val = 0;
        for (int i = 0; i < par.getSize().getBytes(); ++i) {
            val = val << 8;
            val = val | (bytes[par.getPosition().getBytePos() + i]) & 0xFF;
        }
        return (double) val;
    }

    @Override
    public void setValue(Double value) throws GatewayException {
        long val = value.longValue();
        byte bytes[] = new byte[par.getSize().getBytes()];
        for (int i = par.getSize().getBytes() - 1; i >= 0; --i) {
            bytes[i] = (byte) (val & 0xFF);
            val = val >>> 8;
        }
        reg.setValue(bytes);
    }

    @Override
    public Type getType() {
        return Type.NUMBER;
    }

    NumberParameter(RegisterImpl reg, Param par) {
        super(reg, par);
    }

    @Override
    public Double getDefault() {
        return Double.valueOf(par.getDefault());
    }
    
    

}
