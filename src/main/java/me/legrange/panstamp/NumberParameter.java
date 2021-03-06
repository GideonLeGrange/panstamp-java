package me.legrange.panstamp;

import me.legrange.panstamp.definition.ParameterDefinition;

/**
 * An parameter that supports the "num" type from the XML definitions and maps
 * these to Doubles. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class NumberParameter extends AbstractParameter<Double> {

    @Override
    public Double getValue() throws NetworkException {
        byte bytes[] = reg.getValue();
        long val = 0;
        for (int i = 0; i < par.getSize().getBytes(); ++i) {
            val = val << 8;
            val = val | (bytes[par.getPosition().getBytePos() + i]) & 0xFF;
        }
        return (double) val;
    }

    @Override
    public void setValue(Double value) throws NetworkException {
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

    NumberParameter(Register reg, ParameterDefinition par) {
        super(reg, par);
    }

    @Override
    public Double getDefault() {
        return Double.valueOf(par.getDefault());
    }
    
    

}
