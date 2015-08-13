package me.legrange.panstamp;

import me.legrange.panstamp.event.AbstractPanStampListener;
import me.legrange.panstamp.event.AbstractRegisterListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.definition.DeviceDefinition;
import me.legrange.panstamp.definition.EndpointDefinition;
import me.legrange.panstamp.definition.ParameterDefinition;
import me.legrange.panstamp.definition.RegisterDefinition;
import me.legrange.swap.SwapMessage;

/**
 * An implementation of a panStamp abstraction. Instances of this class
 * represent instances of panStamp devices connected to the network behind the
 * gateway.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class PanStamp {

    /**
     * Get the address of this device.
     *
     * @return The address
     */
    public int getAddress() {
        return address;
    }

    /**
     * Return the network channel
     *
     * @return The channel
     * @throws NetworkException Thrown if there is a problem getting the channel
     * value
     */
    public int getChannel() throws NetworkException {
        Integer v = getIntValue(StandardEndpoint.FREQUENCY_CHANNEL);
        if (v != null) {
            return v;
        }
        return getNetwork().getChannel();
    }

    /**
     * Return the transmit interval
     *
     * @return The interval
     * @throws NetworkException Thrown if there is a problem getting the
     * interval value
     */
    public int getTxInterval() throws NetworkException {
        return getIntValue(StandardEndpoint.PERIODIC_TX_INTERVAL, 0);
    }

    /**
     * Return the current security option
     *
     * @return The security option
     * @throws NetworkException Thrown if there is a problem getting the
     * security option value
     */
    public int getSecurityOption() throws NetworkException {
        return getIntValue(StandardEndpoint.SECURITY_OPTION, 0);
    }
    
    /** 
     * Return the current sync state 
     * 
     * @return The sync state
     * @throws NetworkException
     * @since 1.2
     */
    public int getSyncState() throws NetworkException {
        return getIntValue(StandardEndpoint.SYSTEM_STATE, 2);
    }

    /**
     * Return the network ID
     *
     * @return The network ID
     * @throws NetworkException Thrown if there is a problem getting the network
     * ID value
     */
    public int getNetworkId() throws NetworkException {
        Integer v = getIntValue(StandardEndpoint.NETWORK_ID);
        if (v != null) {
            return v;
        }
        return getNetwork().getNetworkId();
    }

    /**
     * Set the address of the panStamp
     *
     * @param addr The address to set
     * @throws NetworkException Thrown if there is a problem reading the
     * interval.
     */
    public void setAddress(int addr) throws NetworkException {
        if (addr != getAddress()) {
            setIntValue(StandardEndpoint.DEVICE_ADDRESS, addr);
        }
    }

    /**
     * Set the network id of the device
     *
     * @param networkId The network id
     * @throws NetworkException Thrown if there is a problem setting the ID
     */
    public void setNetworkId(int networkId) throws NetworkException {
        if (networkId != getNetworkId()) {
            setIntValue(StandardEndpoint.NETWORK_ID, networkId);
        }
    }

    /**
     * Set the network channel of the device
     *
     * @param channel The channel to set.
     * @throws NetworkException Thrown if there is a problem setting the channel
     */
    public void setChannel(int channel) throws NetworkException {
        if (channel != getChannel()) {
            setIntValue(StandardEndpoint.FREQUENCY_CHANNEL, channel);
        }
    }

    /**
     * Set the security option of the device.
     *
     * @param option The security option to set.
     * @throws NetworkException Thrown if there is a problem setting the option.
     */
    public void setSecurityOption(int option) throws NetworkException {
        if (option != getSecurityOption()) {
            setIntValue(StandardEndpoint.SECURITY_OPTION, option);
        }
    }

    /**
     * Set the transmit interval (in seconds) of the device.
     *
     * @param txInterval The interval to set.
     * @throws NetworkException Thrown if there is a problem setting the
     * interval.
     */
    public void setTxInterval(int txInterval) throws NetworkException {
        if (txInterval != getTxInterval()) {
            setIntValue(StandardEndpoint.PERIODIC_TX_INTERVAL, txInterval);
        }
    }

    /**
     * Get the device manufacturer id.
     *
     * @return The manufacturer Id
     * @throws NetworkException Thrown if there is a problem reading the id.
     */
    public int getManufacturerId() throws NetworkException {
        return manufacturerId;
    }

    /**
     * Get the device product id.
     *
     * @return The product Id
     * @throws NetworkException Thrown if there is a problem reading the id.
     */
    public int getProductId() throws NetworkException {
        return productId;
    }

    /**
     * Get the network this device is attached to.
     *
     * @return The network
     */
    public Network getNetwork() {
        return nw;
    }

    /**
     * Get the device name (as defined by the endpoint definition).
     *
     * @return The name of the device
     */
    public String getName() {
        if (def != null) {
            return def.getProduct();
        }
        return "Unknown";
    }

    /**
     * Get the register with the given register ID for this device.
     *
     *
     * @return the register for the given id
     * @param id ID of register to return
     */
    public Register getRegister(int id) {
        Register reg;
        synchronized (registers) {
            reg = registers.get(id);
            if (reg == null) {
                reg = new Register(this, id);
                registers.put(id, reg);
            }
        }
        return reg;
    }

    /**
     * Get the list of registers defined for this device
     *
     * @return The list of registers.
     */
    public List<Register> getRegisters() {
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

    /**
     * Determine if the device has a register with the given ID.
     *
     * @param id The id of the register required.
     * @return True if the panStamp has the register.
     */
    public boolean hasRegister(int id) {
        return registers.get(id) != null;
    }

    /**
     * add an event listener
     *
     * @param l The listener to add
     */
    public void addListener(PanStampListener l) {
        listeners.add(l);
    }

    /**
     * remove an event listener
     *
     * @param l The listener to remove
     */
    public void removeListener(PanStampListener l) {
        listeners.remove(l);
    }

    /**
     * create a new mote for the given address in the given network
     *
     * @param gw The gateway to which this device is connected
     * @param address The address of the device
     * @throws me.legrange.panstamp.NetworkException Thrown if there is a
     * problem creating the device.
     */
    public PanStamp(Network gw, int address) throws NetworkException {
        this.nw = gw;
        this.address = address;
        extended = address > 255;
        for (StandardRegister reg : StandardRegister.ALL) {
            Register impl = new Register(this, reg);
            registers.put(reg.getId(), impl);
        }
        getRegister(StandardRegister.PRODUCT_CODE.getId()).addListener(productCodeListener());
        getRegister(StandardRegister.SYSTEM_STATE.getId()).getEndpoint(StandardEndpoint.SYSTEM_STATE.getName()).addListener(systemStateListener());
    }

    void destroy() {
        for (Register reg : registers.values()) {
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
        nw.sendQueryMessage(this, id);
    }

    /**
     * send a command message to the remote node
     *
     * @param value Value to send
     */
    void sendCommandMessage(int id, byte[] value) throws NetworkException {
        if (isSleeper()) {
            queue(id, value);
        } else {
            nw.sendCommandMessage(this, id, value);
        }
    }

    /**
     * Receive a status message from the remote node.
     */
    void statusMessageReceived(SwapMessage msg) {
        Register reg = (Register) getRegister(msg.getRegisterID());
        boolean isNew = !reg.hasValue();
        reg.valueReceived(msg.getRegisterValue());
        if (isNew) {
            fireRegisterDetected(reg);
        }
    }
    
    boolean hasExtendedAddress() {
        return extended;
    }

    ExecutorService getPool() {
        return nw.getPool();
    }

    private void fireRegisterDetected(final Register reg) {
        for (final PanStampListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.registerDetected(PanStamp.this, reg);
                }
            });
        }
    }

    private int getIntValue(StandardEndpoint epDef, int defaultValue) throws NetworkException {
        Integer v = getIntValue(epDef);
        if (v != null) {
            return v;
        }
        return defaultValue;

    }

    private void setIntValue(StandardEndpoint epDef, int val) throws NetworkException {
        Register reg = getRegister(epDef.getRegister().getId());
        Endpoint<Integer> ep = reg.getEndpoint(epDef.getName());
        ep.setValue(val);
    }

    private Integer getIntValue(StandardEndpoint epDef) throws NetworkException {
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

    private boolean isSleeper() throws NetworkException {
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
                    l.syncRequired(PanStamp.this);
                }
            });
        }
    }

    private void fireSyncStateChanged(final int syncState) {
        for (final PanStampListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.syncStateChange(PanStamp.this, syncState);
                }
            });
        }
    }

    private void fireProductCodeChange(final int manufacturerId, final int productId) {
        for (final PanStampListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.productCodeChange(PanStamp.this, manufacturerId, productId);
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
                } catch (NetworkException ex) {
                    Logger.getLogger(PanStamp.class.getName()).log(Level.SEVERE, null, ex);
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
                        fireProductCodeChange(manufacturerId, productId);
                    }
                } catch (NetworkException ex) {
                    Logger.getLogger(PanStamp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

    }

    /**
     * load all endpoints and parameters
     */
    private void loadDefinition() throws NetworkException {
        def = nw.getDeviceDefinition(getManufacturerId(), getProductId());
        List<RegisterDefinition> rpDefs = def.getRegisters();
        for (RegisterDefinition rpDef : rpDefs) {
            Register reg = (Register) getRegister(rpDef.getId());
            for (EndpointDefinition epDef : rpDef.getEndpoints()) {
                reg.addEndpoint(epDef);
            }
            for (ParameterDefinition par : rpDef.getParameters()) {
                reg.addParameter(par);
            }
        }
    }

    /**
     * get the manufacturer id for this panStamp from the actual register
     */
    private int getManufacturerIdFromRegister() throws NetworkException {
        Register reg = getRegister(StandardRegister.PRODUCT_CODE.getId());
        if (reg.hasValue()) {
            byte val[] = reg.getValue();
            return val[0] << 24 | val[1] << 16 | val[2] << 8 | val[3];
        }
        return 0;
    }

    /**
     * get the product id for this panStamp from the actual register
     */
    private int getProductIdFromRegister() throws NetworkException {
        Register reg = getRegister(StandardRegister.PRODUCT_CODE.getId());
        if (reg.hasValue()) {
            byte val[] = reg.getValue();
            return val[4] << 24 | val[5] << 16 | val[6] << 8 | val[7];
        }
        return 0;
    }

    private final int address;
    private DeviceDefinition def;
    private final Network nw;
    private int manufacturerId;
    private int productId;
    private int syncState;
    private final boolean extended;
    private final Map<Integer, Register> registers = new ConcurrentHashMap<>();
    private transient final Set<PanStampListener> listeners = new CopyOnWriteArraySet<>(); // wish I knew why this was transient...

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
                        nw.sendCommandMessage(PanStamp.this, id, val);
                    } catch (ModemException ex) {
                        Logger.getLogger(PanStamp.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        removeListener(this);
                    }
                    break;
                default:
            }
        }
    }
}
