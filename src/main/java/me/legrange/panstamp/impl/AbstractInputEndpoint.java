package me.legrange.panstamp.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.InputEndpoint;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.Register.RegisterListener;
import me.legrange.panstamp.def.EndpointDef;

/**
 *
 * @author gideon
 * @param <T>
 */
public abstract class AbstractInputEndpoint<T> extends AbstractEndpoint<T> implements InputEndpoint<T>, RegisterListener {

    @Override
    public void addListener(String unit, EndpointListener<T> el) throws NoSuchUnitException {
        if (listeners.isEmpty()) {
           ps.getRegister(epDef.getRegister().getId()).addListener(this);
        }
        listeners.put(el, unit);
    }

    @Override
    public void registerUpdated(Register.RegisterEvent ev) {
        for (EndpointListener<T> l : listeners.keySet()) {
            try {
                l.valueReceived(getValue(listeners.get(l)));
            } catch (GatewayException ex) {
                java.util.logging.Logger.getLogger(SerialGateway.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    AbstractInputEndpoint(PanStamp ps, EndpointDef epDef) {
        super(ps, epDef);
        this.listeners = new HashMap<>();
    }

    private final Map<EndpointListener<T>, String> listeners;

}
