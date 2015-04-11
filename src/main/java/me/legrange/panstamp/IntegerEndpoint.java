package me.legrange.panstamp;

import me.legrange.panstamp.definition.EndpointDefinition;
import me.legrange.panstamp.definition.Unit;

/**
 * An endpoint that supports integer values for endpoint data and maps values to
 * Java Integers.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class IntegerEndpoint extends AbstractEndpoint<Integer> {

    IntegerEndpoint(Register reg, EndpointDefinition epDef) {
        super(reg, epDef);
    }

    @Override
    public Type getType() {
        return Type.INTEGER;
    }

    @Override
    protected Integer read(Unit unit) throws NoValueException {
        byte bytes[] = reg.getValue();
        if (bytes.length > 0) {
            int val = 0;
            for (int i = 0; i < epDef.getSize().getBytes(); ++i) {
                val = val << 8;
                val = val | (bytes[epDef.getPosition().getBytePos() + i]) & 0xFF;
            }
            Double d = (val * unit.getFactor() + unit.getOffset());
            return d.intValue();
        }
        return null;
    }

    @Override
    protected void write(Unit unit, Integer value) throws NetworkException {
        Double d = (value - unit.getOffset()) / unit.getFactor();
        value = d.intValue();
        byte bytes[];
        if (reg.hasValue()) {
            bytes = reg.getValue();
        } else {
            bytes = new byte[epDef.getRegister().getByteSize()];
        }
        long val = value.longValue();
        for (int i = epDef.getSize().getBytes() - 1; i >= 0; --i) {
            bytes[epDef.getPosition().getBytePos() + i] = (byte) (val & 0xFF);
            val = val >>> 8;
        }
        reg.setValue(bytes);
    }
}
