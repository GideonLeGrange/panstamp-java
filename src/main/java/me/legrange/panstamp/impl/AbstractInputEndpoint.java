package me.legrange.panstamp.impl;

import java.util.HashMap;
import java.util.Map;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.InputEndpoint;
import me.legrange.panstamp.PanStamp;
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
    }

    private final Map<String, EndpointListener> listeners;

}
