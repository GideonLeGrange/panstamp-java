package me.legrange.panstamp;

import me.legrange.panstamp.xml.ClassLoaderLibrary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.definition.DeviceDefinition;
import me.legrange.swap.MessageListener;
import me.legrange.swap.SwapException;
import me.legrange.swap.SwapModem;
import me.legrange.swap.SwapMessage;
import me.legrange.swap.UserMessage;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.SerialModem;
import me.legrange.swap.tcp.TcpModem;

/**
 * A gateway connecting a panStamp network to your code using the SWAP modem
 * supplied.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class Network implements AutoCloseable {

    /**
     * Create a new serial network (network attached to a serial port) with the
     * given port and speed, and with the default device library and data store.
     *
     * @param port The serial port to open, for example COM1 or /dev/ttyS0
     * @param baud The speed at which to open it, for example 34800
     * @return The newly created network.
     * @throws me.legrange.panstamp.NetworkException Thrown if there is a
     * problem creating the network.
     */
    public static Network openSerial(String port, int baud) throws NetworkException {
        SerialModem sm = new SerialModem(port, baud);
        Network nw = create(sm);
        nw.open();
        return nw;
    }

    /**
     * Create a new TCP/IP network (network attached to a remote TCP service)
     * with the given host and port, and with the default device library and
     * data store.
     *
     * @param host The host name to which to connect, for example 'localhost' or
     * '192.168.1.1'
     * @param port The TCP port to which to connect.
     * @return The newly created network
     * @throws me.legrange.panstamp.NetworkException Thrown if there is a
     * problem creating the network.
     */
    public static Network openTcp(String host, int port) throws NetworkException {
        TcpModem tm = new TcpModem(host, port);
        Network nw = create(tm);
        nw.open();
        return nw;
    }

    /**
     * Create a new network with the given pre-existing SWAP modem.
     *
     * @param modem The SWAP modem to use in the network.
     * @return The newly created network.
     */
    public static Network create(SwapModem modem) {
        return new Network(modem);
    }

    /**
     * Check if the network is open (is connected to a panStamp network).
     *
     * @return True if the network is running.
     */
    public boolean isOpen() {
        return modem.isOpen();
    }

    /**
     * Open the network. This will open the underlying modem and internal
     * processes that are needed.
     *
     * @throws NetworkException Thrown if there is a problem opening the modem.
     */
    public void open() throws NetworkException {
        modem.addListener(receiver);
        try {
            if (!modem.isOpen()) {
                modem.open();
            }
            getSetup();
        } catch (SwapException ex) {
            throw new NetworkException(String.format("Error opening SWAP modem: %s", ex.getMessage()), ex);
        }
        fireNetworkOpened();
    }

    /**
     * Disconnect the connection and close the network
     *
     * @throws me.legrange.panstamp.ModemException Thrown if there is a problem
     * closing the modem supporting the network.
     */
    @Override
    public void close() throws ModemException {
        try {
            modem.close();
        } catch (SwapException ex) {
            throw new ModemException(ex.getMessage(), ex);

        } finally {
            modem.removeListener(receiver);
        }
        fireNetworkClosed();
    }

    /**
     * use to check if a device with the given address is known
     *
     * @param address Address of the device we're looking for.
     * @return True if a device with the given address is known
     */
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
     * @throws me.legrange.panstamp.NodeNotFoundException Thrown if the device
     * with the given address cannot be found.
     */
    public PanStamp getDevice(int address) throws NodeNotFoundException {
        synchronized (devices) {
            PanStamp dev = devices.get(address);
            if (dev == null) {
                throw new NodeNotFoundException(String.format("No device found for address %02x", address));
            }
            return dev;
        }
    }

    /**
     * return all the devices associated with this network
     *
     * @return The list of devices
     */
    public List<PanStamp> getDevices() {
        List<PanStamp> res = new ArrayList<>();
        res.addAll(devices.values());
        return res;
    }

    /**
     * Add a user-created device to the panStamp network.
     *
     * @param dev The device to add.
     */
    public void addDevice(final PanStamp dev) throws MoteException {
        synchronized (devices) {
            devices.put(dev.getAddress(), dev);
            for (StandardRegister sr : StandardRegister.ALL) {
                Register reg;
                if (!dev.hasRegister(sr.getId())) {
                    reg = dev.addRegister(sr.getId());
                } else {
                    reg = dev.getRegister(sr.getId());
                }
                if (!reg.hasValue()) {
                    if (store.hasRegisterValue(reg)) {
                        reg.valueReceived(store.getRegisterValue(reg));
                    }
                }
            }
            fireDeviceDetected(dev);
        }
    }

    /**
     * Removes the device with the given address from the network
     *
     * @param address The address of the device to remove.
     */
    public void removeDevice(int address) {
        final PanStamp ps = devices.get(address);
        if (ps != null) {
            ps.destroy();
            devices.remove(address);
            fireDeviceRemoved(ps);
        }
    }

    /**
     * add listener to receive new device events
     *
     * @param l The listener to add
     */
    public void addListener(NetworkListener l) {
        listeners.add(l);
    }

    /**
     * remove a listener from the network
     *
     * @param l The listener to remove
     */
    public void removeListener(NetworkListener l) {
        listeners.remove(l);
    }

    /**
     * return the SWAP modem to gain access to the lower layer
     *
     * @return The SWAP modem supporting this network
     */
    public SwapModem getSWAPModem() {
        return modem;
    }

    /**
     * Return the device library being used to lookup device definitions.
     *
     * @return The current library.
     */
    public DeviceLibrary getDeviceLibrary() {
        return lib;
    }

    /**
     * Return the device store used to save device state.
     *
     * @return The current store.
     */
    public DeviceStateStore getDeviceStore() {
        return store;
    }

    /**
     * Set the device library used to lookup device definitions.
     *
     * @param lib The library to use.
     */
    public void setDeviceLibrary(DeviceLibrary lib) {
        this.lib = lib;
    }

    /**
     * Set the device store to use to lookup persisted device registers.
     *
     * @param store The store to use.
     */
    public void setDeviceStore(DeviceStateStore store) {
        this.store = store;
    }

    /**
     * return the network ID for the network supported by this network
     *
     * @return The network ID
     * @throws me.legrange.panstamp.ModemException Thrown if there is problem
     * determining the network ID
     */
    public int getNetworkId() throws ModemException {
        return getSetup().getNetworkID();
    }

    /**
     * Get the frequency channel
     *
     * @return The channel
     * @throws me.legrange.panstamp.ModemException Thrown if the channel could
     * not be determined.
     */
    public int getChannel() throws ModemException {
        return getSetup().getChannel();
    }

    /**
     * get the gateway panStamp's address
     *
     * @return the network address
     * @throws me.legrange.panstamp.ModemException Thrown if the device address
     * could not be determined.
     */
    public int getDeviceAddress() throws ModemException {
        return getSetup().getDeviceAddress();
    }

    /**
     * Get the security option
     *
     * @return the security option value.
     */
    public int getSecurityOption() {
        return 0; // FIX ME
    }

    /**
     * Set the network ID for the network accessed by this network
     *
     * @param id The network ID
     * @throws NetworkException thrown if there is a problem setting the network
     * ID
     */
    public void setNetworkId(int id) throws NetworkException {
        getSetup().setNetworkID(id);
    }

    /**
     * Set the device address for the network panStamp
     *
     * @param addr Address to set for the modem device.
     * @throws NetworkException Thrown if there is a problem setting the modem
     * device address
     */
    public void setDeviceAddress(int addr) throws NetworkException {
        getSetup().setDeviceAddress(addr);
    }

    /**
     * Set the frequency channel
     *
     * @param channel The channel to use.
     * @throws NetworkException Thrown if there is an error setting the channel.
     */
    public void setChannel(int channel) throws NetworkException {
        getSetup().setChannel(channel);
    }

    /**
     * Set the security option
     *
     * @param secOpt Security option to use.
     */
    public void setSecurityOption(int secOpt) {
        // FIX ME
    }

    /**
     * send a command message to a remote device
     */
    void sendCommandMessage(PanStamp dev, int register, byte[] value) throws ModemException {
        UserMessage msg = new UserMessage(dev.hasExtendedAddress(), SwapMessage.Type.COMMAND, getSetup().getDeviceAddress(), dev.getAddress(), register, value);
        msg.setRegisterAddress(dev.getAddress());
        send(msg);
    }

    /**
     * send a query message to a remote device
     */
    void sendQueryMessage(PanStamp dev, int register) throws ModemException {
        UserMessage msg = new UserMessage(dev.hasExtendedAddress(), SwapMessage.Type.QUERY, 0xFF, dev.getAddress(), register, new byte[]{});
        msg.setRegisterAddress(dev.getAddress());
        send(msg);
    }

    DeviceDefinition getDeviceDefinition(int manId, int prodId) throws NetworkException {
        return lib.getDeviceDefinition(manId, prodId);
    }

    /**
     * Get the executor service used to service library threads
     */
    ExecutorService getPool() {
        return pool;
    }

    /**
     * Create an new network implementation using the given modem
     * implementation, XML library and data store.
     *
     * @param modem The SWAP modem to use to connect to the panStamp wireless
     * network.
     */
    private Network(SwapModem modem) {
        this.modem = modem;
        lib = new ClassLoaderLibrary();
        store = new MemoryStore();
        receiver = new Receiver();
    }

    private void fireDeviceDetected(final PanStamp dev) {
        for (final NetworkListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.deviceDetected(Network.this, dev);
                }
            });
        }
    }

    private void fireDeviceRemoved(final PanStamp dev) {
        for (final NetworkListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.deviceRemoved(Network.this, dev);
                }
            });
        }
    }

    private void fireNetworkOpened() {
        for (final NetworkListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.networkOpened(Network.this);
                }
            });
        }
    }

    private void fireNetworkClosed() {
        for (final NetworkListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.networkClosed(Network.this);
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
        } catch (SwapException ex) {
            throw new ModemException(ex.getMessage(), ex);
        }
    }

    /**
     * update the network based on a received message
     */
    private void updateNetwork(SwapMessage msg) throws NetworkException {
        int address = msg.getSender();
        synchronized (devices) {
            if (!hasDevice(address)) {
                addDevice(new PanStamp(this, address));
            }
        }
    }

    /**
     * process a status message received from the modem
     */
    private void processStatusMessage(SwapMessage msg) {
        try {
            PanStamp dev = (PanStamp) getDevice(msg.getRegisterAddress());
            dev.statusMessageReceived(msg);
            if (msg.isStandardRegister()) {
                StandardRegister sr = StandardRegister.forId(msg.getRegisterID());
                if (dev.hasRegister(msg.getRegisterID())) {
                    Register reg = dev.getRegister(msg.getRegisterID());
                    if (reg.hasValue()) {
                        store.setRegisterValue(reg, reg.getValue());
                    }
                }
            }
        } catch (NetworkException ex) {
            java.util.logging.Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private synchronized ModemSetup getSetup() throws ModemException {
        if (setup == null) {
            try {
                setup = modem.getSetup();
            } catch (SwapException ex) {
                throw new ModemException(ex.getMessage(), ex);
            }
        }
        return setup;
    }

    private final SwapModem modem;
    private final Receiver receiver;
    private DeviceLibrary lib;
    private DeviceStateStore store;
    private final Map<Integer, PanStamp> devices = new HashMap<>();
    private final Set<NetworkListener> listeners = new CopyOnWriteArraySet<>();
    private static final Logger logger = Logger.getLogger(Network.class.getName());
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
            try {
                updateNetwork(msg);
            } catch (NetworkException ex) {
                Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (msg.getType() == SwapMessage.Type.STATUS) {
                processStatusMessage(msg);
            }

        }

        @Override
        public void messageSent(SwapMessage msg) {
        }
    }

}
