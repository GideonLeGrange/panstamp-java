package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.EndpointDef;

/**
 *
 * @author gideon
 */
class StringInputEndpoint extends AbstractInputEndpoint<String> {

    public StringInputEndpoint(PanStamp ps, EndpointDef epDef) {
        super(ps, epDef);
        
    }

    @Override
    public String getValue(String unit) throws GatewayException {
        byte bytes[]  = ps.getRegister(epDef.getRegister().getId()).getValue();
        byte keep[] = new byte[epDef.getSize().getBytes()];
        System.arraycopy(bytes, epDef.getPosition().getBytePos(), keep, 0, epDef.getSize().getBytes());
        return new String(keep);
    }
    
}
