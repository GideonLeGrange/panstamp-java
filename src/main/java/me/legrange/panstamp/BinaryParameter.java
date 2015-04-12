package me.legrange.panstamp;

import me.legrange.panstamp.definition.ParameterDefinition;

/**
 * An parameter that supports the "bin" type from the XML definitions and maps
 * these to booleans. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class BinaryParameter extends AbstractParameter<Boolean> {

    BinaryParameter(Register reg, ParameterDefinition par) {
        super(reg, par);
    }

    @Override
    public Type getType() {
        return Type.BINARY;
    }

    @Override
    public Boolean getValue() throws NetworkException {
        byte val[] = reg.getValue();
        int byteIdx = par.getPosition().getBytePos();
        int bitIdx = par.getPosition().getBitPos();
        return (val[byteIdx] & ~(0b1 << bitIdx)) != 0;
    }

    @Override
    public void setValue(Boolean value) throws NetworkException {
        byte val[] = reg.getValue();
        int byteIdx = par.getPosition().getBytePos();
        int bitIdx = par.getPosition().getBitPos();
        val[byteIdx] = (byte)(val[byteIdx] & ~(0b1 << bitIdx) | ((byte)(value ? 0b1 : 0b0) << bitIdx));  
        reg.setValue(val);
    }

    @Override
    public Boolean getDefault() {
        String val = par.getDefault().trim().toLowerCase();
        switch (val) {
            case "true": 
            case "yes":
            case "1" :
                return true;
            default : 
                return false;
        }
    }


    
}
