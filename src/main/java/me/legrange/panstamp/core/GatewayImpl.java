package me.legrange.panstamp.core;

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
import me.legrange.panstamp.DeviceLibrary;
import me.legrange.panstamp.DeviceStateStore;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.Gateway;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.GatewayListener;
import me.legrange.panstamp.NodeNotFoundException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.StandardEndpoint;
import me.legrange.panstamp.StandardRegister;
import me.legrange.swap.MessageListener;
import me.legrange.swap.SWAPException;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.SwapMessage;
import me.legrange.swap.UserMessage;
import me.legrange.swap.ModemSetup;

/**
 * A gateway connecting a panStamp network to your code using the SWAP modem
 * supplied.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class GatewayImpl implements Gateway {

    /**
     * Create an new gateway implementation using the given modem
     * implementation, XML library and data store.
     *
     * @param modem The SWAP modem to use to connect to the panStamp wireless
     * network.
     */
    public GatewayImpl(SWAPModem modem) {
        this.modem = modem;
        this.lib = new ClassLoaderLibrary();
        this.store = new MemoryStore();
        receiver = new Receiver();
    }

    @Override
    public void open() throws GatewayException {
        modem.addListener(receiver);
        try {
            modem.open();
            getSetup();
        } catch (SWAPException ex) {
            throw new GatewayException(String.format("Error opening SWAP modem: %s", ex.getMessage()), ex);
        }
    }

    /**
     * Disconnect from the modem and close the gateway
     *
     * @throws me.legrange.panstamp.core.ModemException
     */
    @Override
    public void close() throws ModemException {
        try {
            modem.close();
        } catch (SWAPException ex) {
            throw new ModemException(ex.getMessage(), ex);

        } finally {
            modem.removeListener(receiver);
            devices.clear();
        }
    }

    /**
     * use to check if a device with the given address is known
     *
     * @param address Address of the device we're looking for.
     * @return True if a device with the given address is known
     */
    @Override
    public boolean hasDevice(int address) {
        synchronized (devices) {
            return devices.get(address) != null;
        }
    }

    /**
     * return the device with the given name
     *
     * @param address Address of device to fins
     * @return The device
     * @throws me.legrange.panstamp.NodeNotFoundException
     */
    @Override
    public PanStamp getDevice(int address) throws NodeNotFoundException {
        synchronized (devices) {
            PanStampImpl dev = devices.get(address);
            if (dev == null) {
                throw new NodeNotFoundException(String.format("No device found for address %02x", address));
            }
            return dev;
        }
    }

    /**
     * return the devices associated with this gateway
     *
     * @return The collection of devices
     */
    @Override
    public List<PanStamp> getDevices() {
        List<PanStamp> res = new ArrayList<>();
        res.addAll(devices.values());
        return res;
    }

    public void addDevice(final PanStampImpl ps) {
        devices.put(ps.getAddress(), ps);
        fireDeviceDetected(ps);
        ps.getDefinition();
    }

    @Override
    public void removeDevice(int address) {
        final PanStampImpl ps = devices.get(address);
        if (ps != null) {
            fireDeviceRemoved(ps);
            ps.destroy();
            devices.remove(address);
        }
    }

    @Override
    public void addListener(GatewayListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListener(GatewayListener l) {
        listeners.remove(l);
    }

    @Override
    public SWAPModem getSWAPModem() {
        return modem;
    }

    @Override
    public DeviceLibrary getDeviceLibrary() {
        return lib;
    }

    @Override
    public DeviceStateStore getDeviceStore() {
        return store;
    }

    @Override
    public void setDeviceLibrary(DeviceLibrary lib) {
        this.lib = lib;
    }

    @Override
    public void setDeviceStore(DeviceStateStore store) {
        this.store = store;
    }

    @Override
    public boolean isOpen() {
        return modem.isOpen();
    }

    @Override
    public int getNetworkId() throws ModemException {
        return getSetup().getNetworkID();
    }

    @Override
    public int getChannel() throws ModemException {
        return getSetup().getChannel();
    }

    @Override
    public int getDeviceAddress() throws ModemException {
        return getSetup().getDeviceAddress();
    }

    @Override
    public int getSecurityOption() {
        return 0; // FIX ME
    }

    @Override
    public void setNetworkId(int id) throws GatewayException {
        getSetup().setNetworkID(id);
    }

    @Override
    public void setDeviceAddress(int addr) throws GatewayException {
        getSetup().setDeviceAddress(addr);
    }

    @Override
    public void setChannel(int channel) throws GatewayException {
        getSetup().setChannel(channel);
    }

    @Override
    public void setSecurityOption(int secOpt) throws GatewayException {
        // FIX ME
    }

    /**
     * send a command message to a remote device
     */
    void sendCommandMessage(PanStampImpl dev, int register, byte[] value) throws ModemException {
        UserMessage msg = new UserMessage(SwapMessage.Type.COMMAND, getSetup().getDeviceAddress(), dev.getAddress(), register, value);
        msg.setRegisterAddress(dev.getAddress());
        send(msg);
    }

    /**
     * send a query message to a remote device
     */
    void sendQueryMessage(PanStampImpl dev, int register) throws ModemException {
        UserMessage msg = new UserMessage(SwapMessage.Type.QUERY, 0xFF, dev.getAddress(), register, new byte[]{});
        msg.setRegisterAddress(dev.getAddress());
        send(msg);
    }

    DeviceDefinition getDeviceDefinition(int manId, int prodId) throws GatewayException {
        return lib.getDeviceDefinition(manId, prodId);
    }

    /** Get the executor service used to service library threads */
    ExecutorService getPool() {
        return pool;
    }
    
    private void fireDeviceDetected(final PanStampImpl dev) {
        for (final GatewayListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.deviceDetected(GatewayImpl.this, dev);
                }
            });
        }
    }
    
        private void fireDeviceRemoved(final PanStampImpl dev) {
        for (final GatewayListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.deviceRemoved(GatewayImpl.this, dev);
                }
            });
        }
    }

    /**
     * send a message to a mote
     */
    private void send(SwapMessage msg) throws ModemException {
        try {
            modem.send(msg);
        } catch (SWAPException ex) {
            throw new ModemException(ex.getMessage(), ex);
        }
    }

    /**
     * update the network based on a received message
     */
    private void updateNetwork(SwapMessage msg) throws GatewayException {
        int address = msg.getSender();
        synchronized (devices) {
            if (!hasDevice(address)) {
                try {
                    PanStampImpl dev = new PanStampImpl(this, address);
                    addDevice(dev);
                    for (StandardEndpoint sep : StandardEndpoint.ALL) {
                        if (store.hasEndpointValue(address, sep)) {
                            Endpoint ep = dev.getRegister(sep.getRegister().getId()).getEndpoint(sep.getName());
                            if (!ep.hasValue()) {
                                ep.setValue(store.getEndpointValue(address, sep));
                            }
                        }
                    }
                } catch (NoSuchUnitException ex) {
                    throw new ModemException(ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * process a status message received from the modem
     */
    private void processStatusMessage(SwapMessage msg) {
        try {
            updateNetwork(msg);
            PanStampImpl dev = (PanStampImpl) getDevice(msg.getRegisterAddress());
            dev.statusMessageReceived(msg);
            if (msg.isStandardRegister()) {
                StandardRegister sr = StandardRegister.forId(msg.getRegisterID());
                Register reg = dev.getRegister(msg.getRegisterID());
                for (EndpointDef ed : sr.getEndpoints()) {
                    store.setEndpointValue(dev.getAddress(), (StandardEndpoint)ed, (Integer)(reg.getEndpoint(((StandardEndpoint)ed).getName()).getValue()));
                }
            }
        } catch (GatewayException ex) {
            java.util.logging.Logger.getLogger(GatewayImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private synchronized ModemSetup getSetup() throws ModemException {
        if (setup == null) {
            try {
                setup = modem.getSetup();
            } catch (SWAPException ex) {
                throw new ModemException(ex.getMessage(), ex);
            }
        }
        return setup;
    }

    private final SWAPModem modem;
    private final Receiver receiver;
    private DeviceLibrary lib;
    private DeviceStateStore store;
    private final Map<Integer, PanStampImpl> devices = new HashMap<>();
    private final List<GatewayListener> listeners = new LinkedList<>();
    private static final Logger logger = Logger.getLogger(GatewayImpl.class.getName());
    private ModemSetup setup;
    private final ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "PanStamp Library Task");
            t.setDaemon(true);
            return t;
        }
    });

    /**
     * A receiver for incoming messages
     */
    private class Receiver implements MessageListener {

        @Override
        public void messageReceived(SwapMessage msg) {
            switch (msg.getType()) {
                case COMMAND:
                case QUERY:
                    try {
                        updateNetwork(msg);
                    } catch (GatewayException ex) {
                        Logger.getLogger(GatewayImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case STATUS:
                    processStatusMessage(msg);
                    break;
            }

        }

        @Override
        public void messageSent(SwapMessage msg) {
        }
    }

}
