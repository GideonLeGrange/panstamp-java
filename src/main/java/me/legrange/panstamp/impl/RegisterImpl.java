package me.legrange.panstamp.impl;

import java.util.ArrayList;
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
import me.legrange.panstamp.EndpointNotFoundException;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterEvent;
import me.legrange.panstamp.RegisterEvent.Type;
import me.legrange.panstamp.RegisterListener;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.swap.Registers;

/**
 * Abstraction of a panStamp register.
 *
 * @author gideon
 */
public class RegisterImpl implements Register {

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
    
    

    @Override
    public List<Endpoint> getEndpoints() throws GatewayException {
        List<Endpoint> all = new ArrayList<>();
        all.addAll(endpoints.values());
        return all;
    }

    /**
     * Add a listener to receive register updates
     *
     * @param l listener to add
     */
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
     * @throws me.legrange.panstamp.impl.MoteException
     */
    @Override
    public void setValue(byte value[]) throws GatewayException {
        try {
            dev.sendCommandMessage(id, value);
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
    public Endpoint getEndpoint(String name) throws GatewayException {
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

    /**
     * update the abstracted register value and notify listeners
     */
    void valueReceived(byte value[]) {
        synchronized (this) {
            this.value = value;
            notify();
        }
        fireEvent(RegisterEvent.Type.VALUE_RECEIVED);
    }

    void addEndpoint(EndpointDef def) throws NoSuchUnitException {
        Endpoint ep = makeEndpoint(def);
        endpoints.put(def.getName(), ep);
        fireEvent(Type.ENDPOINT_ADDED, ep);
    }

    /**
     * create a new register for the given dev and register address
     */
    RegisterImpl(PanStampImpl mote, int id) {
        this.dev = mote;
        this.id = id;
    }

    RegisterImpl(PanStampImpl mote, Registers.Register reg) throws NoSuchUnitException {
        this(mote, reg.position());
        name = reg.toString();
        addStandardEndpoints(reg);
    }


    private void fireEvent(final RegisterEvent.Type type) {
        fireEvent(type, null);
    }

    private void fireEvent(final RegisterEvent.Type type, final Endpoint ep) {
        RegisterEvent ev = new RegisterEvent() {

            @Override
            public RegisterEvent.Type getType() {
                return type;
            }

            @Override
            public Register getRegister() {
                return RegisterImpl.this;
            }

            @Override
            public Endpoint getEndpoint() {
                return ep;
            }
            
            
        };
        for (RegisterListener l : listeners) {
            pool.submit(new UpdateTask(ev,l));
        }
    }

    /**
     * make an endpoint object based on it's definition
     */
    private Endpoint makeEndpoint(EndpointDef epDef) throws NoSuchUnitException {
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

    private void addStandardEndpoints(Registers.Register reg) throws NoSuchUnitException {
        switch (reg) {
            case DEVICE_ADDRESS:
                addEndpoint(StandardEndpoint.DEVICE_ADDRESS);
                break;
            case FIRMWARE_VERSION:
                addEndpoint(StandardEndpoint.FIRMWARE_VERSION);
                break;
            case FREQUENCY_CHANNEL:
                addEndpoint(StandardEndpoint.FREQUENCY_CHANNEL);
                break;
            case HARDWARE_VERSION:
                addEndpoint(StandardEndpoint.HARDWARE_VERSION);
                break;
            case NETWORK_ID:
                addEndpoint(StandardEndpoint.NETWORK_ID);
                break;
            case PERIODIC_TX_INTERVAL:
                addEndpoint(StandardEndpoint.PERIODIC_TX_INTERVAL);
                break;
            case PRODUCT_CODE:
                addEndpoint(StandardEndpoint.MANUFACTURER_ID);
                addEndpoint(StandardEndpoint.PRODUCT_ID);
                break;
            case SECURITY_NONCE:
                addEndpoint(StandardEndpoint.SECURITY_NONCE);
                break;
            case SECURITY_OPTION:
                addEndpoint(StandardEndpoint.SECURITY_OPTION);
                break;
            case SECURITY_PASSWORD:
                addEndpoint(StandardEndpoint.SECURITY_PASSWORD);
                break;
            case SYSTEM_STATE:
                addEndpoint(StandardEndpoint.SYSTEM_STATE);
                break;
        }
    }

    private final PanStampImpl dev;
    private final int id;
    private String name = "";
    private final Map<String, Endpoint> endpoints = new HashMap<>();
    private final List<RegisterListener> listeners = new LinkedList<>();
    private byte[] value;
    private final ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Register Event Task");
            t.setDaemon(true);
            return t;
        }
    });

    private class UpdateTask implements Runnable {

        private UpdateTask(RegisterEvent e,RegisterListener l) {
            this.l = l;
            this.e = e;
        }

        @Override
        public void run() {
            try {
                l.registerUpdated(e);
            } catch (Throwable e) {
                Logger.getLogger(SerialGateway.class.getName()).log(Level.SEVERE, null, e);

            }
        }

        private final RegisterEvent e;
        private final RegisterListener l;
    }

}
