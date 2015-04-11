package me.legrange.panstamp;

import me.legrange.panstamp.definition.EndpointDefinition;
import me.legrange.panstamp.definition.Unit;

/**
 * An endpoint that supports the "binary" type from the XML definitions and maps
 * these to booleans. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class BinaryEndpoint extends AbstractEndpoint<Boolean> {

    BinaryEndpoint(Register reg, EndpointDefinition epDef) {
        super(reg, epDef);
    }

    @Override
    public Type getType() {
        return Type.BINARY;
    }

    @Override
    protected Boolean read(Unit unit) throws NoValueException {
        byte val[] = reg.getValue();
        int byteIdx = epDef.getPosition().getBytePos();
        int bitIdx = epDef.getPosition().getBitPos();
        return (val[byteIdx] & (0b1 << bitIdx)) != 0;    }

    @Override
    protected void write(Unit unit, Boolean value) throws NetworkException {
        byte val[] = reg.getValue();
        int byteIdx = epDef.getPosition().getBytePos();
        int bitIdx = epDef.getPosition().getBitPos();
        val[byteIdx] = (byte) (val[byteIdx] & ~(0b1 << bitIdx) | ((byte) (value ? 0b1 : 0b0) << bitIdx));
        reg.setValue(val);
    }

}
