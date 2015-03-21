package me.legrange.panstamp;

import me.legrange.panstamp.definition.ParameterDefinition;

/**
 * An parameter that supports  integer values for endpoint data and maps values to Java Integers.s
 * these to booleans. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class IntegerParameter extends AbstractParameter<Integer> {

    IntegerParameter(Register reg, ParameterDefinition par) {
        super(reg, par);
    }

    @Override
    public Type getType() {
        return Type.INTEGER;
    }
    
    @Override
    public Integer getValue() throws NetworkException {
        byte bytes[] = reg.getValue();//ps.getRegister(epDef.getRegister().getId()).getValue();
        int val = 0;
        for (int i = 0; i < par.getSize().getBytes(); ++i) {
            val = val << 8;
            val = val | (bytes[par.getPosition().getBytePos() + i]) & 0xFF;
        }
        return val;
    }

    @Override
    public void setValue(Integer value) throws NetworkException {
        long val = value.longValue();
        byte bytes[] = new byte[par.getSize().getBytes()];
        for (int i = par.getSize().getBytes() - 1; i >= 0; --i) {
            bytes[i] = (byte) (val & 0xFF);
            val = val >>> 8;
        }
        reg.setValue(bytes);
    }

    @Override
    public Integer getDefault() {
        return Integer.parseInt(par.getDefault());
    }

    
}
