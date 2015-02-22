package me.legrange.panstamp.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
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
import me.legrange.panstamp.StandardEndpoint;
import me.legrange.panstamp.StandardRegister;
import me.legrange.panstamp.def.DeviceDef;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Param;
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

    @Override
    public int getChannel() throws GatewayException {
        Integer v = getIntValue(StandardEndpoint.FREQUENCY_CHANNEL);
        if (v != null) {
            return v;
        }
        return getGateway().getChannel();
    }

    @Override
    public int getTxInterval() throws GatewayException {
        return getIntValue(StandardEndpoint.PERIODIC_TX_INTERVAL, 0);
    }

    @Override
    public int getSecurityOption() throws GatewayException {
        return getIntValue(StandardEndpoint.SECURITY_OPTION, 0);
    }

    /**
     *
     * @return The network address
     * @throws GatewayException Thrown if there is an error trying to figure out
     * the network address.
     */
    @Override
    public int getNetwork() throws GatewayException {
        Integer v = getIntValue(StandardEndpoint.NETWORK_ID);
        if (v != null) {
            return v;
        }
        return getGateway().getNetworkId();
    }

    @Override
    public void setAddress(int addr) throws GatewayException {
        if (addr != getAddress()) {
            setIntValue(StandardEndpoint.DEVICE_ADDRESS, addr);
        }
    }

    @Override
    public void setNetwork(int network) throws GatewayException {
        if (network != getNetwork()) {
            setIntValue(StandardEndpoint.NETWORK_ID, network);
        }
    }

    @Override
    public void setChannel(int channel) throws GatewayException {
        if (channel != getChannel()) {
            setIntValue(StandardEndpoint.FREQUENCY_CHANNEL, channel);
        }
    }

    @Override
    public void setSecurityOption(int option) throws GatewayException {
        if (option != getSecurityOption()) {
            setIntValue(StandardEndpoint.SECURITY_OPTION, option);
        }
    }

    @Override
    public void setTxInterval(int txInterval) throws GatewayException {
        if (txInterval != getTxInterval()) {
            setIntValue(StandardEndpoint.PERIODIC_TX_INTERVAL, txInterval);
        }
    }

    @Override
    public int getManufacturerId() throws GatewayException {
        return manufacturerId;
    }

    public void setProductCode(int manId, int prodId) throws GatewayException {
        if (manId != getManufacturerId()) {
            manufacturerId = manId;
            setIntValue(StandardEndpoint.MANUFACTURER_ID, manId);
        }
        if (prodId != getProductId()) {
            productId = prodId;
            setIntValue(StandardEndpoint.PRODUCT_ID, prodId);
        }
        if ((manufacturerId > 0) && (productId > 0)) {
           loadDefinition();
        }
    }

    @Override
    public int getProductId() throws GatewayException {
        return productId;
    }

    @Override
    public GatewayImpl getGateway() {
        return gw;
    }

    @Override
    public String getName() {
        if (def != null) {
            return def.getProduct();
        }
        return "Unknown";
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
    
    void destroy() {
        for (RegisterImpl reg : registers.values()) {
            reg.destroy();
        }
        listeners.clear();
        registers.clear();
    }

    DeviceDef getDefinition() {
        return def;
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
    void sendCommandMessage(int id, byte[] value) throws GatewayException {
        if (isSleeper()) {
            queue(id, value);
        } else {
            gw.sendCommandMessage(this, id, value);
        }
    }

    /**
     * Receive a status message from the remote node.
     */
    void statusMessageReceived(SwapMessage msg) {
        RegisterImpl reg = (RegisterImpl) getRegister(msg.getRegisterID());
        boolean isNew = !reg.hasValue();
        reg.valueReceived(msg.getRegisterValue());
        if (isNew) {
            fireEvent(Type.REGISTER_DETECTED, reg);
        }
    }

    ExecutorService getPool() {
        return gw.getPool();
    }

    /**
     * create a new mote for the given address in the given network
     */
    public PanStampImpl(GatewayImpl gw, int address) throws GatewayException {
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
                    break;
                }
            }
        }
    }

    private int getIntValue(StandardEndpoint epDef, int defaultValue) throws GatewayException {
        Integer v = getIntValue(epDef);
        if (v != null) {
            return v;
        }
        return defaultValue;

    }

    private void setIntValue(StandardEndpoint epDef, int val) throws GatewayException {
        Register reg = getRegister(epDef.getRegister().getId());
        Endpoint<Integer> ep = reg.getEndpoint(epDef.getName());
        ep.setValue(val);
    }

    private Integer getIntValue(StandardEndpoint epDef) throws GatewayException {
        Register reg = getRegister(epDef.getRegister().getId());
        if (reg.hasValue()) {
            Endpoint<Integer> ep = reg.getEndpoint(epDef.getName());
            return ep.getValue();
        }
        return null;
    }

    private void queue(int id, byte[] value) {
        addListener(new UpdateOnSync(id, value));
        fireEvent(Type.SYNC_REQUIRED, getRegister(id));
    }

    private boolean isSleeper() throws GatewayException {
        if (def != null) {
            return def.isPowerDownMode();
        } else {
            Endpoint<Integer> ep = getRegister(StandardRegister.SYSTEM_STATE.getId()).getEndpoint(StandardEndpoint.SYSTEM_STATE.getName());
            if (!ep.hasValue()) { // if we can't confirm sleep mode, we assume it is true so we rather ask for sync
                return true;
            }
            int v = ep.getValue();
            return (v != 3) && (v != 1);
        }
    }

    private void fireEvent(final PanStampEvent.Type type) {
        fireEvent(type, null, this.syncState);
    }

    private void fireEvent(final PanStampEvent.Type type, final Register reg) {
        fireEvent(type, reg, this.syncState);
    }

    private void fireEvent(final PanStampEvent.Type type, int syncState) {
        fireEvent(type, null, syncState);
    }

    private void fireEvent(final PanStampEvent.Type type, final Register reg, final int syncState) {
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

            @Override
            public int getSyncState() {
                return syncState;
            }

        };
        for (PanStampListener l : listeners) {
            getPool().submit(new UpdateTask(ev, l));
        }
    }

    private EndpointListener systemStateListener() {
        return new EndpointListener<Integer>() {

            @Override
            public void endpointUpdated(EndpointEvent<Integer> ev) {
                switch (ev.getType()) {
                    case VALUE_RECEIVED:
                        try {
                            syncState = ev.getEndpoint().getValue();
                            fireEvent(Type.SYNC_STATE_CHANGE);
                        } catch (GatewayException ex) {
                            Logger.getLogger(PanStampImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
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
                            if ((mfId != getManufacturerId()) || (pdId != getProductId())) {
                                setProductCode(mfId, pdId);
                                fireEvent(Type.PRODUCT_CODE_UPDATE);
                            }
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
    private void loadDefinition() throws GatewayException {
        def = getDeviceDefinition();
        List<RegisterDef> rpDefs = def.getRegisters();
        for (RegisterDef rpDef : rpDefs) {
            RegisterImpl reg = (RegisterImpl) getRegister(rpDef.getId());
            for (EndpointDef epDef : rpDef.getEndpoints()) {
                reg.addEndpoint(epDef);
            }
            for (Param par : rpDef.getParameters()) {
                reg.addParameter(par);
            }
        }
    }

    /**
     * get the device definition for this panStamp
     */
    private DeviceDef getDeviceDefinition() throws GatewayException {
        return gw.getDeviceDefinition(getManufacturerId(), getProductId());
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
    private transient DeviceDef def;
    private transient final GatewayImpl gw;
    private int manufacturerId;
    private int productId;
    private int syncState;
    private final Map<Integer, RegisterImpl> registers = new HashMap<>();
    private transient final List<PanStampListener> listeners = new CopyOnWriteArrayList<>();

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
                Logger.getLogger(GatewayImpl.class.getName()).log(Level.SEVERE, null, e);

            }
        }

        private final PanStampListener l;
        private final PanStampEvent ev;
    }

    private class UpdateOnSync implements PanStampListener {

        private UpdateOnSync(int id, byte[] val) {
            this.id = id;
            this.val = val;
        }
        private final int id;
        private final byte[] val;

        @Override
        public void deviceUpdated(PanStampEvent ev) {
            if (ev.getType() == Type.SYNC_STATE_CHANGE) {
                switch (ev.getSyncState()) {
                    case 1:
                    case 3:
                        try {
                            gw.sendCommandMessage(PanStampImpl.this, id, val);
                        } catch (ModemException ex) {
                            Logger.getLogger(PanStampImpl.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            removeListener(this);
                        }
                        break;
                    default:
                }
            }
        }
    }
}
