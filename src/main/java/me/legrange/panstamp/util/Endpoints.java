package me.legrange.panstamp.util;

import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.EndpointDef;

/**
 *
 * @author gideon
 */
public class Endpoints {
    
    public static Endpoint getEndpoint(PanStamp ps, EndpointDef def) throws GatewayException {
        return ps.getRegister(def.getRegister().getId()).getEndpoint(def.getName());
    }
    
}
