package me.legrange.panstamp;

import me.legrange.panstamp.definition.EndpointDefinition;
import me.legrange.panstamp.definition.Unit;

/**
 * An endpoint that supports the "num" type from the XML definitions and maps
 * these to Doubles. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class NumberEndpoint extends AbstractEndpoint<Double> {

    NumberEndpoint(Register reg, EndpointDefinition epDef) {
        super(reg, epDef); 
    }

    @Override
    public Type getType() {
        return Type.NUMBER;
    }
    
    @Override
    public Double getValue() throws GatewayException {
        byte bytes[] = reg.getValue();//ps.getRegister(epDef.getRegister().getId()).getValue();
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
        reg.setValue(bytes);
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
