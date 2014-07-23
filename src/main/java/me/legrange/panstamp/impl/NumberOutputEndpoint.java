/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Unit;

/**
 *
 * @author gideon
 */
class NumberOutputEndpoint extends AbstractOutputEndpoint<Double> {

    public NumberOutputEndpoint(PanStamp ps, EndpointDef epDef) {
        super(ps, epDef);
    }

    @Override
    public void setValue(String unit, Double value) throws GatewayException {
        Unit u = getUnit(unit);
        Double adj = (value  - u.getOffset()) / u.getFactor();
        long val = adj.longValue();
        byte bytes[] = new byte[epDef.getSize().getBytes()];
        for (int i = epDef.getSize().getBytes() -1; i >=0; --i) {
            bytes[i] = (byte)(val & 0xF);
            val = val >>> 8;
        }
        ps.getRegister(epDef.getRegister().getId()).setValue(bytes);
    }

}