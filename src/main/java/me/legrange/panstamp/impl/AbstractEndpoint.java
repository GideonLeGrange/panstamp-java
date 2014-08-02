package me.legrange.panstamp.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Unit;

/**
 *
 * @author gideon
 * @param <T>
 */
public abstract class AbstractEndpoint<T> implements Endpoint<T>, Register.RegisterListener {

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

    AbstractEndpoint(PanStampImpl ps, EndpointDef epDef) {
        this.ps = ps;
        this.epDef = epDef;
        this.listeners = new HashMap<>();

    }

    @Override
    public synchronized void addListener(String unit, EndpointListener<T> el) throws GatewayException {
        if (listeners.isEmpty()) {
            ps.getRegister(epDef.getRegister().getId()).addListener(this);
        }
        listeners.put(el, unit);
    }

    @Override
    public void addListener(EndpointListener<T> el) throws GatewayException {
        addListener(null, el);
    }

    @Override
    public void registerUpdated(Register.RegisterEvent ev) {
        for (EndpointListener<T> l : listeners.keySet()) {
            pool.submit(new ListenerTask(l));
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

    protected abstract T transformOut(T value, Unit unit);

    protected abstract T transformIn(T value, Unit unit);

    protected final PanStampImpl ps;
    protected final EndpointDef epDef;

    private final Map<EndpointListener<T>, String> listeners;
    private final ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Endpoint Notification Task");
            t.setDaemon(true);
            return t;
        }
    });

    private class ListenerTask implements Runnable {

        private ListenerTask(EndpointListener l) {
            this.l = l;
        }

        @Override
        public void run()  {
            try {
                String unit = listeners.get(l);
                T val;
                if (unit != null) {
                    val = getValue(unit);
                } else {
                    val = getValue();
                }
                System.out.printf("unit %s => %s\n", unit, val);
                l.valueReceived(val);
            } catch (Throwable e) {
                Logger.getLogger(SerialGateway.class.getName()).log(Level.SEVERE, null, e);

            }
        }

        private final EndpointListener l;

    }

}
