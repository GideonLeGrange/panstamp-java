package me.legrange.panstamp;

import me.legrange.panstamp.xml.ClassLoaderLibrary;
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
import me.legrange.panstamp.definition.DeviceDefinition;
import me.legrange.swap.MessageListener;
import me.legrange.swap.SWAPException;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.SwapMessage;
import me.legrange.swap.UserMessage;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.serial.SerialModem;
import me.legrange.swap.tcp.TcpModem;

/**
 * A gateway connecting a panStamp network to your code using the SWAP modem
 * supplied.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class Gateway {

/** Create a new serial gateway (gateway attached to a serial port) with the given port and speed, 
     * and with the default device library and data store. 
     * 
     * @param port The serial port to open, for example COM1 or /dev/ttyS0
     * @param baud The speed at which to open it, for example 34800 
     * @return The newly created gateway. 
     */
    public static Gateway createSerial(String port, int baud) {
        SerialModem sm = new SerialModem(port, baud);
        return create(sm);
    }

    /** Create a new TCP/IP gateway (gateway attached to a remote TCP service) with the given host and port, 
     * and with the default device library and data store. 
     * 
     * @param host The host name to which to connect, for example 'localhost' or '192.168.1.1'
     * @param port The TCP port to which to connect.
     * @return The newly created gateway 
     */
    public static Gateway createTcp(String host, int port) {
        TcpModem tm = new TcpModem(host, port);
        return create(tm);
    }
    
    /** Create a new gateway with the given pre-existing SWAP modem. 
     * 
     * @param modem
     * @return 
     */
    public static Gateway create(SWAPModem modem) {
        return new Gateway(modem);
    }
     
    /** 
     * Check if the gateway is open (is connected to a panStamp network). 
     * 
     * @return True if the network is running.
     */
    public boolean isOpen() {
        return modem.isOpen();
    }
    
    /**
     * Open the gateway. This will open the underlying modem and internal
     * processes that are needed.
     *
     * @throws GatewayException Thrown if there is a problem opening the modem.
     */
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
     * Disconnect the connection and close the gateway
     *
     * @throws me.legrange.panstamp.GatewayException
     */
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
     * return all the devices associated with this gateway
     *
     * @return The list of devices
     */
    public List<PanStamp> getDevices() {
        List<PanStamp> res = new ArrayList<>();
        res.addAll(devices.values());
        return res;
    }

    /** Add a user-created device to the panStamp network. 
     * 
     * @param ps The device to add. 
     */
    public void addDevice(final PanStamp ps) {
        devices.put(ps.getAddress(), ps);
        fireDeviceDetected(ps);
        ps.getDefinition();
    }

    /** Removes the device with the given address from the network
     * 
     * @param address The address of the device to remove.
     */
    public void removeDevice(int address) {
        final PanStamp ps = devices.get(address);
        if (ps != null) {
            fireDeviceRemoved(ps);
            ps.destroy();
            devices.remove(address);
        }
    }

    /**
     * add listener to receive new device events
     *
     * @param l The listener to add
     */
    public void addListener(GatewayListener l) {
        listeners.add(l);
    }
    
    /**
     * remove a listener from the gateway
     *
     * @param l The listener to remove
     */
    public void removeListener(GatewayListener l) {
        listeners.remove(l);
    }

    /**
     * return the SWAP modem to gain access to the lower layer
     *
     * @return The SWAP modem supporting this gateway
     */
    public SWAPModem getSWAPModem() {
        return modem;
    }

    /** 
     * Return the device library being used to lookup device definitions. 
     * @return The current library.
     */
    public DeviceLibrary getDeviceLibrary() {
        return lib;
    }

    /** Return the device store used to save device state. 
     * 
     * @return The current store.
     */
    public DeviceStateStore getDeviceStore() {
        return store;
    }

    /** 
     * Set the device library used to lookup device definitions.
     * @param lib The library to use.
     */
    public void setDeviceLibrary(DeviceLibrary lib) {
        this.lib = lib;
    }

    
    /** 
     * Set the device store to use to lookup persisted device registers. 
     * @param store The store to use.
     */
    public void setDeviceStore(DeviceStateStore store) {
        this.store = store;
    }
    
    /**
     * return the network ID for the network supported by this gateway
     *
     * @return The network ID
     * @throws me.legrange.panstamp.core.ModemException Thrown if there is a
     * problem determining the network ID
     */
    public int getNetworkId() throws ModemException {
        return getSetup().getNetworkID();
    }


    /**
     * Get the frequency channel
     *
     * @return The channel
     * @throws me.legrange.panstamp.core.ModemException
     */
    public int getChannel() throws ModemException {
        return getSetup().getChannel();
    }
    /**
     * get the gateway panStamp's address
     *
     * @return the gateway address
     * @throws me.legrange.panstamp.core.ModemException
     */
    public int getDeviceAddress() throws ModemException {
        return getSetup().getDeviceAddress();
    }

     /**
     * Get the security option
     *
     * @return the security option value.
     * @throws me.legrange.panstamp.core.ModemException
     */
    public int getSecurityOption() {
        return 0; // FIX ME
    }


    /** Set the network ID for the network accessed by this gateway
     * 
     * @param id The network ID
     * @throws GatewayException thrown if there is a problem setting the network ID 
     */
    public void setNetworkId(int id) throws GatewayException {
        getSetup().setNetworkID(id);
    }

    /** 
     * Set the device address for the gateway panStamp 
     * @param addr
     * @throws GatewayException 
     */
    public void setDeviceAddress(int addr) throws GatewayException {
        getSetup().setDeviceAddress(addr);
    }

 /** 
     * Set the frequency channel 
     * 
     * @param channel The channel to use. 
     * @throws GatewayException 
     */
    public void setChannel(int channel) throws GatewayException {
        getSetup().setChannel(channel);
    }


    /** 
     * Set the security option 
     * 
     * @param secOpt
     * @throws GatewayException 
     */
    public void setSecurityOption(int secOpt) throws GatewayException {
        // FIX ME
    }

    /**
     * send a command message to a remote device
     */
    void sendCommandMessage(PanStamp dev, int register, byte[] value) throws ModemException {
        UserMessage msg = new UserMessage(SwapMessage.Type.COMMAND, getSetup().getDeviceAddress(), dev.getAddress(), register, value);
        msg.setRegisterAddress(dev.getAddress());
        send(msg);
    }

    /**
     * send a query message to a remote device
     */
    void sendQueryMessage(PanStamp dev, int register) throws ModemException {
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
    
     /**
     * Create an new gateway implementation using the given modem
     * implementation, XML library and data store.
     *
     * @param modem The SWAP modem to use to connect to the panStamp wireless
     * network.
     */
    private Gateway(SWAPModem modem) {
        this.modem = modem;
        this.lib = new ClassLoaderLibrary();
        this.store = new MemoryStore();
        receiver = new Receiver();
    }
    
    private void fireDeviceDetected(final PanStamp dev) {
        for (final GatewayListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.deviceDetected(Gateway.this, dev);
                }
            });
        }
    }
    
        private void fireDeviceRemoved(final PanStamp dev) {
        for (final GatewayListener l : listeners) {
            getPool().submit(new Runnable() {

                @Override
                public void run() {
                    l.deviceRemoved(Gateway.this, dev);
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
                    PanStamp dev = new PanStamp(this, address);
                    addDevice(dev);
                    for (StandardRegister sr : StandardRegister.ALL) {
                        Register reg = dev.getRegister(sr.getId());
                        if (!reg.hasValue()) {
                            if (store.hasRegisterValue(reg)) {
                                reg.setValue(store.getRegisterValue(reg));
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
            PanStamp dev = (PanStamp) getDevice(msg.getRegisterAddress());
            dev.statusMessageReceived(msg);
            if (msg.isStandardRegister()) {
                StandardRegister sr = StandardRegister.forId(msg.getRegisterID());
                Register reg = dev.getRegister(msg.getRegisterID());
                store.setRegisterValue(reg, reg.getValue());
            }
        } catch (GatewayException ex) {
            java.util.logging.Logger.getLogger(Gateway.class.getName()).log(Level.SEVERE, null, ex);
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
    private final Map<Integer, PanStamp> devices = new HashMap<>();
    private final List<GatewayListener> listeners = new LinkedList<>();
    private static final Logger logger = Logger.getLogger(Gateway.class.getName());
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
                        Logger.getLogger(Gateway.class.getName()).log(Level.SEVERE, null, ex);
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
