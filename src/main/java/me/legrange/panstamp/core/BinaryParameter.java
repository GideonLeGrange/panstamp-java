package me.legrange.panstamp.core;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.def.Param;

/**
 *
 * @author gideon
 */
class BinaryParameter extends AbstractParameter<Boolean> {

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

    @Override
    public Boolean getDefault() {
        String val = par.getDefault().trim();
        if (val.toLowerCase().equals("true") || val.toLowerCase().equals("yes")) return true;
        if (val.toLowerCase().equals("false") || val.toLowerCase().equals("no")) return false;
        if (val.equals("1")) return true;
        if (val.equals("0")) return false;
        return false;
    }


    
}
