package me.legrange.panstamp.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.InputEndpoint;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Unit;

/**
 *
 * @author gideon
 * @param <T>
 */
public abstract class AbstractInputEndpoint<T> implements InputEndpoint<T> {

    @Override
    public void addListener(String unit, EndpointListener<T> el) throws NoSuchUnitException {
        listeners.put(unit, el);
    }

    @Override
    public String getName() {
        return epDef.getName();
    }

    @Override
    public final List<String> getUnits() {
        List<String> res = new LinkedList<>();
        for (Unit u : epDef.getUnits()) {
            res.add(u.getName());
        }
        return res;
    }
    
    protected final Unit getUnit(String name) throws NoSuchUnitException {
        for (Unit u : epDef.getUnits()) {
            if (u.getName().equals(name)) {
                return u;
            }
        }
        throw new NoSuchUnitException(String.format("No unit '%s' found in endpoint '%s'", name, getName()));
    }

    AbstractInputEndpoint(PanStamp ps, EndpointDef epDef) {
        this.ps = ps;
        this.epDef = epDef;
        this.listeners = new HashMap<>();
    }

    protected final PanStamp ps;
    protected final EndpointDef epDef;
    private final Map<String, EndpointListener> listeners;

}
