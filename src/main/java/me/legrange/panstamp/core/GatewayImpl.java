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
import me.legrange.panstamp.Gateway;
import me.legrange.panstamp.GatewayEvent;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.GatewayListener;
import me.legrange.panstamp.NodeNotFoundException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.DeviceDef;
import me.legrange.swap.MessageListener;
import me.legrange.swap.SWAPException;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.SwapMessage;
import me.legrange.swap.UserMessage;
import me.legrange.swap.ModemSetup;

/**
 * A gateway connecting a panStamp network to your code using the
 * SWAP modem supplied. 
 *
 * @author gideon
 */
public final class GatewayImpl implements Gateway {

    /** Create an new gateway implementation using the given modem implementation,
     * XML library and data store. 
     * 
     * @param modem The SWAP modem to use to connect to the panStamp wireless network.
     * @param lib The XML library to use to find device definitions.
     * @param store The data store to which to store panStamp state.
     */
    public GatewayImpl(SWAPModem modem, DeviceLibrary lib) {
        this.modem = modem;
        this.lib = lib;
        receiver = new Receiver();
    }
    
    @Override
    public void open() throws GatewayException {
        modem.addListener(receiver);
        try {
            modem.open();
            getSetup();
            int networkId = getNetworkId();
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

        }
        finally {
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
    
    public void addDevice(PanStampImpl ps) {
        devices.put(ps.getAddress(), ps);
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
    
    /** Check if the gateway is open (meaning connected to the network) 
     * 
     * @return
     * @throws ModemException 
     */
    boolean isOpen() { 
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

    DeviceDef getDeviceDefinition(int manId, int prodId) throws GatewayException {
        return lib.getDeviceDefinition(manId, prodId);
    }

    ExecutorService getPool() { 
        return pool;
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
                    final PanStampImpl dev = new PanStampImpl(this, address);
                    devices.put(address, dev);
                    fireEvent(new GatewayEvent() {

                        @Override
                        public GatewayEvent.Type getType() {
                            return GatewayEvent.Type.DEVICE_DETECTED;
                        }

                        @Override
                        public PanStamp getDevice() {
                            return dev;
                        }

                    });
            } catch (NoSuchUnitException ex) {
                    throw new ModemException(ex.getMessage(), ex);
                }
            }
        }
    }

    private void fireEvent(GatewayEvent ev) {
        for (GatewayListener l : listeners) {
            pool.submit(new ListenerTask(l, ev));
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
    private final  DeviceLibrary lib;
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

    /**
     * A runnable task that sends a panStamp to a listener
     */
    private class ListenerTask implements Runnable {

        private ListenerTask(GatewayListener l, GatewayEvent ev) {
            this.l = l;
            this.ev = ev;
        }

        @Override
        public void run() {
            try {
                l.gatewayUpdated(ev);
            } catch (Throwable e) {
                Logger.getLogger(GatewayImpl.class.getName()).log(Level.SEVERE, null, e);

            }
        }

        private final GatewayListener l;
        private final GatewayEvent ev;
    }
}
