package me.legrange.panstamp.core;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Unit;

/**
 * An endpoint that supports the "str" type from the XML definitions and maps
 * these to Strings. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
class StringEndpoint extends AbstractEndpoint<String> {

    public StringEndpoint(RegisterImpl reg, EndpointDef epDef) {
        super(reg, epDef);
        
    }

    @Override
    public Type getType() {
        return Type.STRING;
    }
    
    @Override
    protected String transformIn(String value, Unit unit) {
        return value;
    }

    @Override
    public String getValue() throws GatewayException {
        byte bytes[]  = reg.getValue();
        byte keep[] = new byte[epDef.getSize().getBytes()];
        System.arraycopy(bytes, epDef.getPosition().getBytePos(), keep, 0, epDef.getSize().getBytes());
        return new String(keep);
    }
    
    @Override
    protected String transformOut(String value, Unit unit) {
        return value;
    }

    @Override
    public void setValue(String value) throws GatewayException {
        int len = epDef.getSize().getBytes();
        if (value.length() > len) {
            value = value.substring(0, len -1);
        } 
        else {
        }
        byte bytes[] = new byte[len];
        System.arraycopy(value.getBytes(), 0, bytes, epDef.getPosition().getBytePos(), value.length());
    }
    
}
