package me.legrange.example;

import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.EndpointDef;

/**
 * Some utility methods to make some tedious operations a bit easier.
 *
 * @author gideon
 */
public class Util {

    /** return the endpoint for the given endpoint definition from the given panstamp */
    public static Endpoint getEndpoint(PanStamp ps, EndpointDef def) throws GatewayException {
        return ps.getRegister(def.getRegister().getId()).getEndpoint(def.getName());
    }

}
