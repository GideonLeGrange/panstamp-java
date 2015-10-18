package me.legrange.panstamp;

import me.legrange.panstamp.definition.EndpointDefinition;
import me.legrange.panstamp.definition.Unit;

/**
 * An endpoint that supports the "bstr" type from the XML definitions and maps
 * these to byte arrays.
 *
 * @since 2.1
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class ByteArrayEndpoint extends AbstractEndpoint<byte[]> {

    ByteArrayEndpoint(Register reg, EndpointDefinition epDef) {
        super(reg, epDef);

    }

    @Override
    public Type getType() {
        return Type.BYTE_ARRAY;
    }

    @Override
    protected byte[] read(Unit unit) throws NoValueException {
        byte bytes[] = reg.getValue();
        byte keep[] = new byte[epDef.getSize().getBytes()];
        System.arraycopy(bytes, epDef.getPosition().getBytePos(), keep, 0, epDef.getSize().getBytes());
        return keep;
    }

    @Override
    protected void write(Unit unit, byte[] value) throws NetworkException {
        int len = epDef.getSize().getBytes();
        byte bytes[] = new byte[len];
        if (value.length < len) {
           len = value.length;
        } 
        System.arraycopy(value, 0, bytes, epDef.getPosition().getBytePos(), len);
        reg.setValue(bytes);
    }

}
