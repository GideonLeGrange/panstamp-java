package me.legrange.panstamp.core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointEvent;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterEvent;
import me.legrange.panstamp.RegisterListener;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Unit;

/**
 * Abstract implementation of an endpoint that can be extended into endpoints supporting
 * different data times. 
 * 
 * @author gideon
 * @param <T> The data type supported by the endpoint. 
 */
public abstract class AbstractEndpoint<T> implements Endpoint<T>, RegisterListener {

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

    @Override
    public final boolean hasValue() {
        return reg.hasValue();
    }

    @Override
    public synchronized void addListener(EndpointListener<T> el) {
        if (listeners.isEmpty()) {
            getRegister().addListener(this);
        }
        listeners.add(el);
    }

    @Override
    public synchronized void removeListener(EndpointListener<T> el) {
        listeners.remove(el);
        if (listeners.isEmpty()) {
            getRegister().removeListener(this);
        }
    }

    @Override
    public void registerUpdated(RegisterEvent ev) {
        switch (ev.getType()) {
            case VALUE_RECEIVED:
                EndpointEvent e = new EndpointEvent() {

                    @Override
                    public Endpoint getEndpoint() {
                        return AbstractEndpoint.this;
                    }

                    @Override
                    public EndpointEvent.Type getType() {
                        return EndpointEvent.Type.VALUE_RECEIVED;
                    }
                };
                for (EndpointListener<T> l : listeners) {
                    pool().submit(new ListenerTask(e, l));
                }
                break;
        }

    }

    @Override
    public final T getValue(String unit) throws GatewayException {
        return transformIn(getValue(), getUnit(unit));
    }

    @Override
    public void setValue(String unit, T value) throws GatewayException {
        setValue(transformOut(value, getUnit(unit)));
    }

    @Override
    public Register getRegister() {
        return reg;
    }

    /**
     * Transform the output value from a value in the given unit
     *
     * @param value The value to transform
     * @param unit The unit from which to transform it
     * @return The transformed value
     */
    protected abstract T transformOut(T value, Unit unit);

    /**
     * Transform the input value to a value in the given unit
     *
     * @param value The value to transform
     * @param unit The unit to which to transform it
     * @return The transformed value
     */
    protected abstract T transformIn(T value, Unit unit);

    protected final Unit getUnit(String name) throws NoSuchUnitException {
        for (Unit u : epDef.getUnits()) {
            if (u.getName().equals(name)) {
                return u;
            }
        }
        throw new NoSuchUnitException(String.format("No unit '%s' found in endpoint '%s'", name, getName()));
    }

    protected AbstractEndpoint(RegisterImpl reg, EndpointDef epDef) {
        this.reg = reg;
        this.epDef = epDef;
        this.listeners = new CopyOnWriteArrayList<>();

    }

    void destroy() {
        listeners.clear();
    }

    /**
     * Get the executor service used to service library threads
     */
    private ExecutorService pool() {
        return reg.getPool();
    }

    protected final RegisterImpl reg;
    protected final EndpointDef epDef;
    private final CopyOnWriteArrayList<EndpointListener<T>> listeners;

    /**
     * Runnable to execute event firing in the executor
     */
    private class ListenerTask implements Runnable {

        private ListenerTask(EndpointEvent e, EndpointListener l) {
            this.l = l;
            this.e = e;
        }

        @Override
        public void run() {
            try {
                l.endpointUpdated(e);
            } catch (Throwable ex) {
                Logger.getLogger(GatewayImpl.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

        private final EndpointListener<T> l;
        private final EndpointEvent<T> e;
    }

}
