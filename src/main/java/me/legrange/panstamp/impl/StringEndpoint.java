package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Unit;

/**
 *
 * @author gideon
 */
class StringEndpoint extends AbstractEndpoint<String> {

    public StringEndpoint(PanStamp ps, EndpointDef epDef) {
        super(ps, epDef);
        
    }

    @Override
    protected String transformIn(String value, Unit unit) {
        return value;
    }

    @Override
    public String getValue() throws GatewayException {
        byte bytes[]  = ps.getRegister(epDef.getRegister().getId()).getValue();
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
