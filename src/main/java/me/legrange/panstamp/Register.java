package me.legrange.panstamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import me.legrange.panstamp.definition.DeviceDefinition;
import me.legrange.panstamp.definition.EndpointDefinition;
import me.legrange.panstamp.definition.ParameterDefinition;

/**
 * An abstraction of a panStamp register.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public final class Register {
    
    /**
     * return the register ID
     *
     * @return The id of this register.
     */
    public int getId() {
        return id;
    }

    /** Get the register name as defined in XML.
     * 
     * @return The name of the register.
     */
    public String getName() {
        DeviceDefinition def = dev.getDefinition();
        if (def != null) {
            if (def.hasRegister(id)) {
                return def.getRegister(id).getName();
            }
        }
        return name;
    }
    
    /** Get the device to which this register belongs. 
     * 
     * @return The panStamp device.  
     */
    public PanStamp getDevice() {
        return dev;
    }

   /**
     * return the endpoints defined for this register
     *
     * @return The endpoints
     */
    public List<Endpoint> getEndpoints() {
        List<Endpoint> all = new ArrayList<>();
        all.addAll(endpoints.values());
        return all;
    }

  /**
     * Add a listener to receive register updates
     *
     * @param l listener to add
     */
    public void addListener(RegisterListener l) {
        listeners.add(l);
    }


    /**
     * remove a listener
     *
     * @param l listener to remove
     */
    public void removeListener(RegisterListener l) {
        listeners.remove(l);
    }

    /**
     * set the register value and send to remote node
     *
     * @param value the new value
     * @throws me.legrange.panstamp.NetworkException Thrown if there is a
     * problem updating the register
     */
    public void setValue(byte value[]) throws NetworkException {
        try {
            if (dev.getNetwork().isOpen()) {
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
     * get the value of the register
     *
     * @return The value of the register
     * @throws me.legrange.panstamp.NoValueException Thrown if the register value is requested but no value is available.
     */
    public byte[] getValue() throws NoValueException { 
        synchronized (this) {
            if (value == null) {
                throw new NoValueException(String.format("No value received for register %d", id));
            }
        }
        return value;
    }

    /**
     * return true if the register has a currently known value
     * @return True if the register's value is known.
     */
    public boolean hasValue() {
        synchronized (this) {
            return value != null;
        }
    }

    /**
     * return the endpoint for the given name
     *
     * @param name The name of the endpoint needed
     * @return The endpoint object
     * @throws me.legrange.panstamp.EndpointNotFoundException Thrown if an endpoint with that name could not be found.
     */
    public Endpoint getEndpoint(String name) throws EndpointNotFoundException {
        Endpoint ep = endpoints.get(name);
        if (ep == null) {
            throw new EndpointNotFoundException(String.format("Could not find endpoint '%s' in register %d", name, id));
        }
        return ep;
    }


    /**
     * returns true if the device has an endpoint with the given name
     *
     * @param name Name of endpoint we're querying
     * @return True if the endpoint is known.
     * @throws me.legrange.panstamp.NetworkException Thrown if an error is
     * experienced
     */
    public boolean hasEndpoint(String name) throws NetworkException {
        return endpoints.get(name) != null;
    }
    
    /** 
     * Returns the parameters (if any) for this endpoint 
     * @return The list of parameters
     */
    public List<Parameter> getParameters() {
        List<Parameter> all = new ArrayList<>();
        all.addAll(parameters.values());
        return all;
    }

    
    /** 
     * return true if the register is one of the panStamp standard registers. 
     * 
     * @return True if it is standard
     */
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

    void addEndpoint(EndpointDefinition def) {
        AbstractEndpoint ep = makeEndpoint(def);
        endpoints.put(def.getName(), ep);
        fireEndpointAdded(ep);
    }

    void addParameter(ParameterDefinition def) {
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
    Register(PanStamp mote, int id) {
        this.dev = mote;
        this.id = id;
    }

    Register(PanStamp mote, StandardRegister reg) throws NoSuchUnitException {
        this(mote, reg.getId());
        name = reg.getName();
        for (EndpointDefinition sep : reg.getEndpoints()) {
            addEndpoint(sep);
        }
    }

    private void fireValueReceived(final byte[] value) {
        for (final RegisterListener l : listeners) {
            getPool().submit(
                    new Runnable() {

                        @Override
                        public void run() {
                            l.valueReceived(Register.this, value);
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
                            l.valueSet(Register.this, value);
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
                            l.endpointAdded(Register.this, ep);
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
                            l.parameterAdded(Register.this, par);
                        }

                    }
            );
        }
    }

    /**
     * make an endpoint object based on it's definition
     */
    private AbstractEndpoint makeEndpoint(EndpointDefinition epDef) {
        switch (epDef.getType()) {
            case NUMBER:
                return new NumberEndpoint(this, epDef);
            case STRING:
                return new StringEndpoint(this, epDef);
            case BINARY:
                return new BinaryEndpoint(this, epDef);
            case INTEGER:
                return new IntegerEndpoint(this, epDef);
            case BSTRING : 
                return new ByteArrayEndpoint(this, epDef);
            default:
                throw new RuntimeException(String.format("Unknown end point type '%s'. BUG!", epDef.getType()));
        }
    }

    /**
     * make a parameter object based on it's definition
     */
    private AbstractParameter makeParameter(ParameterDefinition def) {
        switch (def.getType()) {
            case NUMBER:
                return new NumberParameter(this, def);
            case STRING:
                return new StringParameter(this, def);
            case BINARY:
                return new BinaryParameter(this, def);
            case INTEGER:
                return new IntegerParameter(this, def);
            case BSTRING : 
                return new ByteArrayParameter(this, def);
            default:
                throw new RuntimeException(String.format("Unknown parameter type '%s'. BUG!", def.getType()));
        }
    }

    private final PanStamp dev;
    private final int id;
    private String name = "";
    private final Map<String, AbstractEndpoint> endpoints = new ConcurrentHashMap<>();
    private final Map<String, AbstractParameter> parameters = new ConcurrentHashMap<>();
    private final Set<RegisterListener> listeners = new CopyOnWriteArraySet<>();
    private byte[] value;

}
