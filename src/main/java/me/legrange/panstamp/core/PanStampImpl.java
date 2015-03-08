package me.legrange.panstamp.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointListener;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.PanStampListener;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterListener;
import me.legrange.panstamp.StandardEndpoint;
import me.legrange.panstamp.StandardRegister;
import me.legrange.panstamp.def.DeviceDefinition;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Param;
import me.legrange.panstamp.def.RegisterDef;
import me.legrange.swap.SwapMessage;

/**
 * An implementation of a panStamp abstraction. Instances of this class
 * represent instances of panStamp devices connected to the network behind the
 * gateway.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
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

    DeviceDefinition getDefinition() {
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
            fireRegisterDetected(reg);
        }
    }

    ExecutorService getPool() {
        return gw.getPool();
    }

    /**
     * create a new mote for the given address in the given network
     *
     * @param gw The gateway to which this device is connected
     * @param address The address of the device
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a
     * problem creating the device.
     */
    public PanStampImpl(GatewayImpl gw, int address) throws GatewayException {
        this.gw = gw;
        this.address = address;
        for (StandardRegister reg : StandardRegister.ALL) {
            RegisterImpl impl = new RegisterImpl(this, reg);
            registers.put(reg.getId(), impl);
            if (StandardRegister.PRODUCT_CODE.getId() == reg.getId()) {
                impl.addListener(productCodeListener());

            } else if (StandardRegister.SYSTEM_STATE.getId() == reg.getId()) {
                impl.getEndpoint(StandardEndpoint.SYSTEM_STATE.getName()).addListener(systemStateListener());

            }
        }
    }

    private void fireRegisterDetected(final Register reg) {
        for (final PanStampListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.registerDetected(PanStampImpl.this, reg);
                }
            });
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
        fireSyncRequired();
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

    private EndpointListener systemStateListener() {
        return new EndpointListener<Integer>() {

            @Override
            public void valueReceived(Endpoint<Integer> ep, Integer syncState) {
                fireSyncStateChanged(syncState);
            }
        };
    }

    private void fireSyncRequired() {
        for (final PanStampListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.syncStateChange(PanStampImpl.this, syncState);
                }
            });
        }
    }

    private void fireSyncStateChanged(final int syncState) {
        for (final PanStampListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.syncStateChange(PanStampImpl.this, syncState);
                }
            });
        }
    }

    private void fireProductCodeChange(final int manufacturerId, final int productId) {
        System.out.printf("fireProductCodeChange(%d,%d)\n", manufacturerId, productId);
        for (final PanStampListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.productCodeChange(PanStampImpl.this, manufacturerId, productId);
                }
            });
        }
    }

    private RegisterListener productCodeListener() {
        return new AbstractRegisterListener() {
            @Override
            public void valueReceived(Register reg, byte value[]) {
                try {
                    int mfId = getManufacturerIdFromRegister();
                    int pdId = getProductIdFromRegister();
                    if ((mfId != getManufacturerId()) || (pdId != getProductId())) {
                        manufacturerId = mfId;
                        productId = pdId;
                        if ((manufacturerId != 0) && (productId != 0)) {
                            loadDefinition();
                        }
                        fireProductCodeChange(mfId, pdId);
                    }
                } catch (GatewayException ex) {
                    Logger.getLogger(PanStampImpl.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            @Override
            public void valueSet(Register reg, byte[] value) {
                try {
                    int mfId = getManufacturerIdFromRegister();
                    int pdId = getProductIdFromRegister();
                    if ((mfId != getManufacturerId()) || (pdId != getProductId())) {
                        manufacturerId = mfId;
                        productId = pdId;
                        if ((manufacturerId != 0) && (productId != 0)) {
                            loadDefinition();
                        }
                    }
                } catch (GatewayException ex) {
                    Logger.getLogger(PanStampImpl.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };

    }

    /**
     * load all endpoints and parameters
     */
    private void loadDefinition() throws GatewayException {
        def = gw.getDeviceDefinition(getManufacturerId(), getProductId());
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
     * get the manufacturer id for this panStamp from the actual register
     */
    private int getManufacturerIdFromRegister() throws GatewayException {
        Register reg = getRegister(StandardRegister.PRODUCT_CODE.getId());
        byte val[] = reg.getValue();
        return val[0] << 24 | val[1] << 16 | val[2] << 8 | val[3];
    }

    /**
     * get the product id for this panStamp from the actual register
     */
    private int getProductIdFromRegister() throws GatewayException {
        Register reg = getRegister(StandardRegister.PRODUCT_CODE.getId());
        byte val[] = reg.getValue();
        return val[4] << 24 | val[5] << 16 | val[6] << 8 | val[7];
    }

    private final int address;
    private DeviceDefinition def;
    private final GatewayImpl gw;
    private int manufacturerId;
    private int productId;
    private int syncState;
    private final Map<Integer, RegisterImpl> registers = new ConcurrentHashMap<>();
    private transient final List<PanStampListener> listeners = new CopyOnWriteArrayList<>();

    private class UpdateOnSync extends AbstractPanStampListener {

        private UpdateOnSync(int id, byte[] val) {
            this.id = id;
            this.val = val;
        }
        private final int id;
        private final byte[] val;

        @Override
        public void syncStateChange(PanStamp dev, int syncState) {
            switch (syncState) {
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
