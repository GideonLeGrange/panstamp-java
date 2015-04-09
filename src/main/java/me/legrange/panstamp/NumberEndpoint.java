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
    protected Double read(Unit unit) throws NoValueException {
        byte bytes[] = reg.getValue();
        long val = 0;
        for (int i = 0; i < epDef.getSize().getBytes(); ++i) {
            val = val << 8;
            val = val | (bytes[epDef.getPosition().getBytePos() + i]) & 0xFF;
        }
        return ((double) val) * unit.getFactor() + unit.getOffset();
    }

    @Override
    protected void write(Unit unit, Double value) throws NetworkException {
        value = (value - unit.getOffset()) / unit.getFactor();
        long val = value.longValue();
        byte bytes[] = new byte[epDef.getSize().getBytes()];
        for (int i = epDef.getSize().getBytes() - 1; i >= 0; --i) {
            bytes[i] = (byte) (val & 0xFF);
            val = val >>> 8;
        }
        reg.setValue(bytes);
    }
}
