package me.legrange.panstamp;

import me.legrange.panstamp.event.AbstractRegisterListener;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.definition.EndpointDefinition;
import me.legrange.panstamp.definition.Unit;

/**
 * Abstract implementation of an endpoint that can be extended into endpoints
 * supporting different data times.
 *
 * @param <T> The data type supported by the endpoint.
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
abstract class AbstractEndpoint<T> implements Endpoint<T> {

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
    public String getUnit() {
        return (unit != null) ? unit.getName() : "";
    }

    @Override
    public void setUnit(String unit) throws NoSuchUnitException {
        unit = (unit != null) ? unit.trim() : "";
        this.unit = unit.isEmpty() ? getUnit(unit) : null;
    }

    @Override
    public final boolean hasValue() {
        return reg.hasValue();
    }

    @Override
    public synchronized void addListener(EndpointListener<T> el) {
        listeners.add(el);
    }

    @Override
    public synchronized void removeListener(EndpointListener<T> el) {
        listeners.remove(el);
    }

    @Override
    public final T getValue() throws NetworkException {
        return read(unit);
    }

    @Override
    public final void setValue(T value) throws NetworkException {
        write(unit, value);
    }

    
    @Override
    public final T getValue(String unit) throws NetworkException {
        return read(getUnit(unit));
    }

    @Override
    public final void setValue(String unit, T value) throws NetworkException {
        write(getUnit(unit), value);
    }

    @Override
    public Register getRegister() {
        return reg;
    }

    /**
     * Write and transform the output value from a value in the given unit
     *
     * @param value The value to transform
     * @param unit The unit from which to transform it
     */
    protected abstract void write(Unit unit, T value) throws NetworkException;

    /**
     * Read and transform the input value to a value in the given unit
     *
     * @param value The value to transform
     * @param unit The unit to which to transform it
     * @return The transformed value
     */
    protected abstract T read(Unit unit) throws NetworkException;

    protected final Unit getUnit(String name) throws NoSuchUnitException {
        for (Unit u : epDef.getUnits()) {
            if (u.getName().equals(name)) {
                return u;
            }
        }
        throw new NoSuchUnitException(String.format("No unit '%s' found in endpoint '%s'", name, getName()));
    }

    protected AbstractEndpoint(Register reg, EndpointDefinition epDef) {
        this.reg = reg;
        this.epDef = epDef;
        this.listeners = new CopyOnWriteArrayList<>();
        reg.addListener(new AbstractRegisterListener() {
            @Override
            public void valueReceived(final Register reg, final byte[] value) {
                for (final EndpointListener<T> l : listeners) {
                    pool().submit(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                l.valueReceived(AbstractEndpoint.this, getValue());
                            } catch (NetworkException ex) {
                                Logger.getLogger(AbstractEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                    );
                }
            }
        });
        unit = !epDef.getUnits().isEmpty() ? epDef.getUnits().get(0) : null;
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

    protected final Register reg;
    protected final EndpointDefinition epDef;
    private final CopyOnWriteArrayList<EndpointListener<T>> listeners;
    private Unit unit = null;

}
