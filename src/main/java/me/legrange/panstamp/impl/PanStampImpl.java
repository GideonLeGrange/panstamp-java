package me.legrange.panstamp.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import me.legrange.panstamp.EndpointEvent;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampEvent;
import me.legrange.panstamp.PanStampEvent.Type;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterEvent;
import me.legrange.panstamp.RegisterListener;
import me.legrange.panstamp.def.Device;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.RegisterDef;
import me.legrange.swap.Registers;
import me.legrange.swap.SwapMessage;

/**
 * An implementation of a panStamp abstraction. Instances of this class
 * represent instances of panStamp devices connected to the network behind the
 * gateway.
 *
 * @author gideon
 */
public class PanStampImpl implements PanStamp {

    /**
     * @return address of this mote
     */
    @Override
    public int getAddress() {
        return address;
    }

    /**
     * @return the register for the given id
     * @param id Register to read
     */
    @Override
    public Register getRegister(int id) {
        RegisterImpl reg;
        synchronized (registers) {
            reg = registers.get(id);
            if (reg == null) {
                reg = new RegisterImpl(this, id);
                registers.put(id, reg);
            }
        }
        return reg;
    }

    @Override
    public List<Register> getRegisters() throws GatewayException {
        List<Register> all = new ArrayList<>();
        all.addAll(registers.values());
        Collections.sort(all, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                return ((Register) o1).getId() - ((Register) o2).getId();
            }
        });
        return all;
    }

    @Override
    public boolean hasRegister(int id) throws GatewayException {
        return registers.get(id) != null;
    }

    @Override
    public void addListener(PanStampListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListener(PanStampListener l) {
        listeners.remove(l);
    }

    /**
     * send a query message to the remote node
     */
    void sendQueryMessage(int id) throws ModemException {
        gw.sendQueryMessage(this, id);
    }

    /**
     * send a command message to the remote node
     *
     * @param value Value to send
     */
    void sendCommandMessage(int id, byte[] value) throws ModemException {
        gw.sendCommandMessage(this, id, value);
    }

    /**
     * Receive a status message from the remote node.
     */
    void statusMessageReceived(SwapMessage msg) {
       RegisterImpl reg =  (RegisterImpl) getRegister(msg.getRegisterID());
       boolean isNew = reg.hasValue();
       reg.valueReceived(msg.getRegisterValue());
       if (isNew) {
           fireEvent(Type.REGISTER_DETECTED, reg);
       }
    }

    private void fireEvent(final PanStampEvent.Type type) {
        fireEvent(type, null);
    }
    
    private void fireEvent(final PanStampEvent.Type type, final Register reg) {
        PanStampEvent ev = new PanStampEvent() {

            @Override
            public Type getType() {
                return type;
            }

            @Override
            public PanStamp getDevice() {
                return PanStampImpl.this;
            }

            @Override
            public Register getRegister() {
                return reg;
            }
            
            
        };
        for (PanStampListener l : listeners) {
            pool.submit(new UpdateTask(ev, l));
        }
    }

    /**
     * create a new mote for the given address in the given network
     */
    PanStampImpl(SerialGateway gw, int address) throws GatewayException {
        this.gw = gw;
        this.address = address;
        for (Registers.Register reg : Registers.Register.values()) {
            RegisterImpl impl = new RegisterImpl(this, reg);
            registers.put(reg.position(), impl);
            switch (reg) {
                case PRODUCT_CODE:
                    impl.addListener(productCodeListener());
                    break;
                case SYSTEM_STATE: {
                    Endpoint<Integer> ep = impl.getEndpoint(StandardEndpoint.SYSTEM_STATE.getName());
                    ep.addListener(systemStateListener());
                }
            }
        }
    }

    private EndpointListener systemStateListener() { 
        return new EndpointListener<Integer>() {

            @Override
            public void endpointUpdated(EndpointEvent<Integer> ev) {
                switch (ev.getType()) {
                    case VALUE_RECEIVED  :
                try {
                    int state = ev.getEndpoint().getValue();
                    if (state != syncState) {
                        syncState = state;
                        fireEvent(Type.SYNC_STATE_CHANGE);
                    }
                } catch (GatewayException ex) {
                    Logger.getLogger(PanStampImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            }
        };
    }
    private RegisterListener productCodeListener() {
        return new RegisterListener() {
            @Override
            public void registerUpdated(RegisterEvent ev) {
                try {
                    switch (ev.getType()) {
                        case VALUE_RECEIVED:
                            Register reg = ev.getRegister();
                            int mfId = getManufacturerId(reg);
                            int pdId = getProductId(reg);
                            if ((mfId != manufacturerId) || (pdId != productId)) {
                                manufacturerId = mfId;
                                productId = pdId;
                                loadEndpoints();
                            }
                            fireEvent(Type.PRODUCT_CODE_UPDATE);
                            break;
                    }
                } catch (GatewayException ex) {
                    Logger.getLogger(PanStampImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    /**
     * load all endpoints
     */
    private void loadEndpoints() throws GatewayException {
        Device def = getDeviceDefinition();
        List<RegisterDef> rpDefs = def.getRegisters();
        for (RegisterDef rpDef : rpDefs) {
            RegisterImpl reg = (RegisterImpl) getRegister(rpDef.getId());
            for (EndpointDef epDef : rpDef.getEndpoints()) {
                reg.addEndpoint(epDef);
            }
        }
    }

    /**
     * get the device definition for this panStamp
     */
    private Device getDeviceDefinition() throws GatewayException {
        return gw.getDeviceDefinition(manufacturerId, productId);
    }

    /**
     * get the manufacturer id for this panStamp
     */
    private int getManufacturerId(Register reg) throws GatewayException {
        byte val[] = reg.getValue();
        return val[0] << 24 | val[1] << 16 | val[2] << 8 | val[3];
    }

    /**
     * get the product id for this panStamp
     */
    private int getProductId(Register reg) throws GatewayException {
        byte val[] = reg.getValue();
        return val[4] << 24 | val[5] << 16 | val[6] << 8 | val[7];
    }

    private final int address;
    private final SerialGateway gw;
    private int manufacturerId;
    private int productId;
    private int syncState;
    private final Map<Integer, RegisterImpl> registers = new HashMap<>();
    private final List<PanStampListener> listeners = new LinkedList<>();
    private final ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "PanStamp Event Task");
            t.setDaemon(true);
            return t;
        }
    });

    private static class UpdateTask implements Runnable {

        private UpdateTask(PanStampEvent ev, PanStampListener l) {
            this.l = l;
            this.ev = ev;
        }

        @Override
        public void run() {
            try {
                l.deviceUpdated(ev);
            } catch (Throwable e) {
                Logger.getLogger(SerialGateway.class.getName()).log(Level.SEVERE, null, e);

            }
        }

        private final PanStampListener l;
        private final PanStampEvent ev;
    }

}
