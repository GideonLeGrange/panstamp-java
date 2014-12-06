package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.def.Param;
import me.legrange.panstamp.def.Unit;

/**
 *
 * @author gideon
 */
public class BinaryParameter extends AbstractParameter<Boolean> {

    public BinaryParameter(RegisterImpl reg, Param par) {
        super(reg, par);
    }

    @Override
    public Type getType() {
        return Type.BINARY;
    }

    @Override
    public Boolean getValue() throws GatewayException {
        byte val[] = reg.getValue();
        int byteIdx = par.getPosition().getBytePos();
        int bitIdx = par.getPosition().getBitPos();
        return (val[byteIdx] & ~(0b1 << bitIdx)) != 0;
    }

    @Override
    public void setValue(Boolean value) throws GatewayException {
        byte val[] = reg.getValue();
        int byteIdx = par.getPosition().getBytePos();
        int bitIdx = par.getPosition().getBitPos();
        val[byteIdx] = (byte)(val[byteIdx] & ~(0b1 << bitIdx) |                                  ((byte)(value ? 0b1 : 0b0) << bitIdx));  
        reg.setValue(val);
    }

    
}
