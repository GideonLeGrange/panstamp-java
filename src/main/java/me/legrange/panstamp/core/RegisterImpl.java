package me.legrange.panstamp.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointNotFoundException;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Parameter;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterListener;

/**
 * Abstraction of a panStamp register.
 *
 * @author gideon
 */
public final class RegisterImpl implements Register {

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        DeviceDefinition def = dev.getDefinition();
        if (def != null) {
            if (def.hasRegister(id)) {
                return def.getRegister(id).getName();
            }
        }
        return name;
    }

    @Override
    public PanStamp getDevice() {
        return dev;
    }

    @Override
    public List<Endpoint> getEndpoints() throws GatewayException {
        List<Endpoint> all = new ArrayList<>();
        all.addAll(endpoints.values());
        return all;
    }

    @Override
    public void addListener(RegisterListener l) {
        listeners.add(l);
    }

    /**
     * remove a listener
     *
     * @param l listener to remove
     */
    @Override
    public void removeListener(RegisterListener l) {
        listeners.remove(l);
    }

    /**
     * set the register value and send to remote node
     *
     * @param value the new value
     * @throws me.legrange.panstamp.core.MoteException
     */
    @Override
    public void setValue(byte value[]) throws GatewayException {
        try {
            if (dev.getGateway().isOpen()) {
                dev.sendCommandMessage(id, value);
            } else {
                this.value = value;
            }
            fireValueSet(value);
        } catch (ModemException e) {
            throw new MoteException(e.getMessage(), e);
        }
    }

    /**
     *
     * @return @throws ModemException
     * @throws NoSuchRegisterException
     */
    @Override
    public byte[] getValue() throws GatewayException {
        synchronized (this) {
            if (value == null) {
                throw new NoSuchRegisterException(String.format("No value received for register %d", id));
            }
        }
        return value;
    }

    @Override
    public boolean hasValue() {
        synchronized (this) {
            return value != null;
        }
    }

    @Override
    public Endpoint getEndpoint(String name) throws EndpointNotFoundException {
        Endpoint ep = endpoints.get(name);
        if (ep == null) {
            throw new EndpointNotFoundException(String.format("Could not find endpoint '%s' in register %d", name, id));
        }
        return ep;
    }

    @Override
    public boolean hasEndpoint(String name) throws GatewayException {
        return endpoints.get(name) != null;
    }

    @Override
    public List<Parameter> getParameters() {
        List<Parameter> all = new ArrayList<>();
        all.addAll(parameters.values());
        return all;
    }

    @Override
    public boolean isStandard() {
        return (id <= StandardRegister.MAX.getId());
    }

    void destroy() {
        for (AbstractEndpoint ep : endpoints.values()) {
            ep.destroy();
        }
        listeners.clear();
        endpoints.clear();
        parameters.clear();
    }

    /**
     * update the abstracted register value and notify listeners
     */
    void valueReceived(final byte value[]) {
        synchronized (this) {
            this.value = value;
        }
        fireValueReceived(value);

    }

    void addEndpoint(EndpointDef def) throws NoSuchUnitException {
        AbstractEndpoint ep = makeEndpoint(def);
        endpoints.put(def.getName(), ep);
        fireEndpointAdded(ep);
    }

    void addParameter(Param def) throws NoSuchUnitException {
        AbstractParameter par = makeParameter(def);
        parameters.put(def.getName(), par);
        fireParameterAdded(par);
    }

    /**
     * Get the executor service used to service library threads
     */
    ExecutorService getPool() {
        return dev.getPool();
    }

    /**
     * create a new register for the given dev and register address
     */
    RegisterImpl(PanStampImpl mote, int id) {
        this.dev = mote;
        this.id = id;
    }

    RegisterImpl(PanStampImpl mote, StandardRegister reg) throws NoSuchUnitException {
        this(mote, reg.getId());
        name = reg.getName();
        for (EndpointDef sep : reg.getEndpoints()) {
            addEndpoint(sep);
        }
    }

    private void fireValueReceived(final byte[] value) {
        for (final RegisterListener l : listeners) {
            getPool().submit(
                    new Runnable() {

                        @Override
                        public void run() {
                            l.valueReceived(RegisterImpl.this, value);
                        }

                    }
            );
        }
    }
    
    private void fireValueSet(final byte[] value) {
        for (final RegisterListener l : listeners) {
            getPool().submit(
                    new Runnable() {

                        @Override
                        public void run() {
                            l.valueSet(RegisterImpl.this, value);
                        }

                    }
            );
        }
    }

    private void fireEndpointAdded(final Endpoint ep) {
        for (final RegisterListener l : listeners) {
            getPool().submit(
                    new Runnable() {

                        @Override
                        public void run() {
                            l.endpointAdded(RegisterImpl.this, ep);
                        }

                    }
            );
        }
    }

    private void fireParameterAdded(final Parameter par) {
        for (final RegisterListener l : listeners) {
            getPool().submit(
                    new Runnable() {

                        @Override
                        public void run() {
                            l.parameterAdded(RegisterImpl.this, par);
                        }

                    }
            );
        }
    }

    /**
     * make an endpoint object based on it's definition
     */
    private AbstractEndpoint makeEndpoint(EndpointDef epDef) throws NoSuchUnitException {
        switch (epDef.getType()) {
            case NUMBER:
                return new NumberEndpoint(this, epDef);
            case STRING:
                return new StringEndpoint(this, epDef);
            case BINARY:
                return new BinaryEndpoint(this, epDef);
            case INTEGER:
                return new IntegerEndpoint(this, epDef);
            default:
                throw new NoSuchUnitException(String.format("Unknown end point type '%s'. BUG!", epDef.getType()));
        }
    }

    /**
     * make a parameter object based on it's definition
     */
    private AbstractParameter makeParameter(Param def) throws NoSuchUnitException {
        switch (def.getType()) {
            case NUMBER:
                return new NumberParameter(this, def);
            case STRING:
                return new StringParameter(this, def);
            case BINARY:
                return new BinaryParameter(this, def);
            case INTEGER:
                return new IntegerParameter(this, def);
            default:
                throw new NoSuchUnitException(String.format("Unknown parameter type '%s'. BUG!", def.getType()));
        }
    }

    private final PanStampImpl dev;
    private final int id;
    private String name = "";
    private final Map<String, AbstractEndpoint> endpoints = new ConcurrentHashMap<>();
    private final Map<String, AbstractParameter> parameters = new ConcurrentHashMap<>();
    private final List<RegisterListener> listeners = new CopyOnWriteArrayList<>();
    private byte[] value;

}
