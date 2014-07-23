package me.legrange.panstamp;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.def.ClassLoaderLibrary;
import me.legrange.panstamp.def.DeviceLibrary;
import me.legrange.swap.CommandMessage;
import me.legrange.swap.Message;
import me.legrange.swap.MessageListener;
import me.legrange.swap.QueryMessage;
import me.legrange.swap.SWAPException;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.SerialException;
import me.legrange.swap.StatusMessage;

/**
 * A gateway connecting a PanStamp network to your code using the PanStamp modem connected 
 * to the local serial port.
 * @author gideon
 */
public final class Gateway {
    
    public static void main(String...args) throws Exception {
        Gateway gw = Gateway.open("/dev/tty.usbserial-A800HNMV", 38400);
        while (true) {
            for (PanStamp p : gw.getDevices()) {
                System.out.printf("%s %s\n", p.getRegister(11), p.getRegister(12));
            }
            Thread.sleep(1000);
        }
    }

    public static Gateway open(String port, int baud) throws ModemException {
        Gateway gw = new Gateway(new ClassLoaderLibrary());
        gw.start(port, baud);
        return gw;
    }

    /** Disconnect from the modem and close the gateway
     * @throws me.legrange.panstamp.ModemException If there is a problem closing the device */
    public void close() throws ModemException {
        try {
            modem.close();
            devices.clear();
        } catch (SerialException e) {
            throw new ModemException(e.getMessage(), e);
        }
    }

    /**
     * use to check if a device with the given address is known
     * @param address Address of the device we're looking for.
     * @return True if a device with the given address is known 
     */
    public boolean hasDevice(int address) {
        return devices.get(address) != null;
    }

    /** return the device with the given name
     * @param address Address of device to fins
     * @return The device
     * @throws me.legrange.panstamp.NodeNotFoundException */
    public PanStamp getDevice(int address) throws NodeNotFoundException {
        PanStamp mote = devices.get(address);
        if (mote == null) {
            throw new NodeNotFoundException(String.format("No device found for address %02x", address));
        }
        return mote;
    }

    /** return the devices associated with this gateway
     * @return  The collection of devices */
    public Collection<PanStamp> getDevices() {
        return Collections.unmodifiableCollection(devices.values());
    }
    
    /**
     * set a remote register
     */
    void setRegister(PanStamp mote, int register, byte[] value) throws ModemException {
        Message msg = new CommandMessage();
        msg.setReceiver(mote.getAddress());
        msg.setRegisterAddress(mote.getAddress());
        msg.setRegisterID(register);
        msg.setRegisterValue(value);
        send(msg);
    }

    /**
     * request a remote register
     */
    void requestRegister(PanStamp mote, int register) throws ModemException {
        Message msg = new QueryMessage();
        msg.setReceiver(mote.getAddress());
        msg.setRegisterAddress(mote.getAddress());
        msg.setRegisterID(register);
        send(msg);
    }

    /**
     * send a message to a mote
     */
    void send(Message msg) throws ModemException {
        System.out.printf("send: %s\n", msg);
        try {
            modem.send(msg);
        } catch (SerialException ex) {
            throw new ModemException(ex.getMessage(), ex);
        }
    }

    private Gateway(DeviceLibrary lib) {
        this.lib = lib;
    }

    private void start(String port, int baud) throws ModemException {
        devices = new HashMap<>();
        receiver = new Receiver();
        try {
            modem = SWAPModem.open(port, baud);
        } catch (SWAPException ex) {
            throw new ModemException(ex.getMessage(), ex);
        }
        modem.addMessageListener(receiver);
    }

    /**
     * update the network based on a received message
     */
    private void updateNetwork(Message msg) {
        updateMote(msg.getSender(), 0);
        if (msg.getRegisterAddress() != msg.getSender()) {
            updateMote(msg.getRegisterAddress(), msg.getSender());
        }
    }

    /**
     * update the mote
     */
    private void updateMote(int address, int route) {
        PanStamp mote;
        if (hasDevice(address)) {
            try {
                mote = getDevice(address);
            } catch (NodeNotFoundException ex) {
                logger.severe(String.format("Could not find node %02x for update.", address));
                return;
            }
        } else {
            mote = new PanStamp(this, address);
            devices.put(address, mote);
        }
        mote.setLastSeen(System.currentTimeMillis());
        mote.setRoute(route);
    }

    private SWAPModem modem;
    private Receiver receiver;
    private Map<Integer, PanStamp> devices;
    private static final Logger logger = Logger.getLogger(Gateway.class.getName());
    private final DeviceLibrary lib;

    /**
     * A receiver for incoming messages
     */
    private class Receiver implements MessageListener {

        @Override
        public void queryReceived(QueryMessage msg) {
            updateNetwork(msg);
        }

        @Override
        public void statusReceived(StatusMessage msg) {
            try {
                updateNetwork(msg);
                PanStamp mote = getDevice(msg.getSender());
                mote.update(msg.getRegisterID(), msg.getRegisterValue());
            } catch (MoteException | NodeNotFoundException ex) {
                java.util.logging.Logger.getLogger(Gateway.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void commandReceived(CommandMessage msg) {
            updateNetwork(msg);
        }
    }
}
