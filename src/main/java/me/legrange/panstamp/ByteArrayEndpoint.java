package me.legrange.panstamp;

import me.legrange.panstamp.definition.EndpointDefinition;
import me.legrange.panstamp.definition.Unit;

/**
 * An endpoint that supports the "str" type from the XML definitions and maps
 * these to Strings.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class StringEndpoint extends AbstractEndpoint<String> {

    StringEndpoint(Register reg, EndpointDefinition epDef) {
        super(reg, epDef);

    }

    @Override
    public Type getType() {
        return Type.STRING;
    }

    @Override
    protected String read(Unit unit) throws NoValueException {
        byte bytes[] = reg.getValue();
        byte keep[] = new byte[epDef.getSize().getBytes()];
        System.arraycopy(bytes, epDef.getPosition().getBytePos(), keep, 0, epDef.getSize().getBytes());
        return new String(keep);
    }

    @Override
    protected void write(Unit unit, String value) throws NetworkException {
        int len = epDef.getSize().getBytes();
        if (value.length() > len) {
            value = value.substring(0, len - 1);
        } else {
        }
        byte bytes[] = new byte[len];
        System.arraycopy(value.getBytes(), 0, bytes, epDef.getPosition().getBytePos(), value.length());
        reg.setValue(bytes);
    }

}
