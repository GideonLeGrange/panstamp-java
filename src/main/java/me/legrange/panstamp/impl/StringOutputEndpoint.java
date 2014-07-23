package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.EndpointDef;

/**
 *
 * @author gideon
 */
class StringOutputEndpoint extends AbstractOutputEndpoint<String>  {

    public StringOutputEndpoint(PanStamp ps, EndpointDef epDef) {
        super(ps, epDef);
    }

    @Override
    public void setValue(String unit, String value) throws GatewayException {
        int len = epDef.getSize().getBytes();
        if (value.length() > len) {
            value = value.substring(0, len -1);
        } 
        byte bytes[] = new byte[len];
        System.arraycopy(len, len, len, len, len);
    }
    
}
