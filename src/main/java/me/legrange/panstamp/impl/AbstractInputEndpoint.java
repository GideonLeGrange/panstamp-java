package me.legrange.panstamp.impl;

import java.util.HashMap;
import java.util.Map;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.InputEndpoint;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.def.EndpointDef;

/**
 *
 * @author gideon
 * @param <T>
 */
public abstract class AbstractInputEndpoint<T> extends AbstractEndpoint<T>  implements InputEndpoint<T> {

    @Override
    public void addListener(String unit, EndpointListener<T> el) throws NoSuchUnitException {
        listeners.put(unit, el);
    }

    AbstractInputEndpoint(PanStamp ps, EndpointDef epDef) {
        super(ps, epDef);
        this.listeners = new HashMap<>();
        ps.getRegister(epDef.getRegister().getId()).addListener(new Register.RegisterListener() {

            @Override
            public void registerUpdated(Register.RegisterEvent ev) {
                for (EndpointListener l : listeners.values()) {
                    l.valueReceived(getValue()); // TODO - convert incoming data and send on to end point listeners.
                }
            }
        });
    }

    private final Map<String, EndpointListener> listeners; // FIXME - This is flawed. We can't map unit => listener, we need to map unit => set of listener
    // because we may have more than one listener listening to an endpoint!
    // re-think.

}
