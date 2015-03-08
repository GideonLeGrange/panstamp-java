package me.legrange.panstamp.core;

import me.legrange.panstamp.GatewayException;

/**
 * An endpoint that supports the "binary" type from the XML definitions and maps
 * these to booleans. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
class BinaryEndpoint extends AbstractEndpoint<Boolean> {

    public BinaryEndpoint(RegisterImpl reg, EndpointDef epDef) {
        super(reg, epDef);
    }

    @Override
    public Type getType() {
        return Type.BINARY;
    }

    @Override
    public Boolean getValue() throws GatewayException {
        byte val[] = reg.getValue();
        int byteIdx = epDef.getPosition().getBytePos();
        int bitIdx = epDef.getPosition().getBitPos();
        return (val[byteIdx] & ~(0b1 << bitIdx)) != 0;
    }

    @Override
    public void setValue(Boolean value) throws GatewayException {
        byte val[] = reg.getValue();
        int byteIdx = epDef.getPosition().getBytePos();
        int bitIdx = epDef.getPosition().getBitPos();
        val[byteIdx] = (byte) (val[byteIdx] & ~(0b1 << bitIdx) | ((byte) (value ? 0b1 : 0b0) << bitIdx));
        reg.setValue(val);
    }

    @Override
    protected Boolean transformIn(Boolean value, Unit unit) {
        return value;
    }

    @Override
    protected Boolean transformOut(Boolean value, Unit unit) {
        return value;
    }

}
