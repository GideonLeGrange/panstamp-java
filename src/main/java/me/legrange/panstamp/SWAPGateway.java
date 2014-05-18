package me.legrange.panstamp;

import java.util.logging.Level;
import me.legrange.swap.CommandMessage;
import me.legrange.swap.Message;
import me.legrange.swap.MessageListener;
import me.legrange.swap.QueryMessage;
import me.legrange.swap.SWAPException;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.SerialException;
import me.legrange.swap.StatusMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author gideon
 */
public class SWAPGateway {

    public SWAPGateway() {
    }

    public void start(String port, int baud) throws ModemException  {
        receiver = new Receiver();
        network = new SWAPNetwork(this);
        try {
            modem = SWAPModem.open(port, baud);
        } catch (SWAPException ex) {
            throw new ModemException(ex.getMessage(), ex);
        }
        modem.addMessageListener(receiver);
    }

    public void stop() throws ModemException {
        try {
            modem.close();
        }
        catch (SerialException e) {
            throw new ModemException(e.getMessage(), e);
        }
    }

    public SWAPNetwork getNetwork() {
        return network;
    }

    /**
     * set a remote register
     */
    void setRegister(SWAPMote mote, int register, byte[] value) throws ModemException  {
        Message msg = new CommandMessage();
        msg.setReceiver(mote.getAddress());
        msg.setRegisterAddress(mote.getAddress());
        msg.setRegisterID(register);
        msg.setRegisterValue(value);
        send(msg);
    }
    
    /** request a remote register */
    void requestRegister(SWAPMote mote, int register) throws ModemException {
        Message msg = new QueryMessage();
        msg.setReceiver(mote.getAddress());
        msg.setRegisterAddress(mote.getAddress());
        msg.setRegisterID(register);
        send(msg);
    }

    /**
     * send a message to a mote
     */
    void send(Message msg) throws ModemException  {
        System.out.printf("send: %s\n", msg);
        try {
            modem.send(msg);
        } catch (SerialException ex) {
            throw new ModemException(ex.getMessage(), ex);
        }
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
        SWAPMote mote;
        if (network.hasMote(address)) {
            try {
                mote = network.getMote(address);
            } catch (NodeNotFoundException ex) {
                logger.error(String.format("Could not find node %02x for update.", address));
                return;
            }
        } else {
            mote = network.createMote(address);
        }
        mote.setLastSeen(System.currentTimeMillis());
        mote.setRoute(route);
    }
    private SWAPModem modem;
    private Receiver receiver;
    private SWAPNetwork network;
    private static final Logger logger = Logger.getLogger(SWAPGateway.class);

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
                SWAPMote mote = network.getMote(msg.getSender());
                mote.update(msg.getRegisterID(), msg.getRegisterValue());
            } catch (MoteException | NodeNotFoundException ex) {
                java.util.logging.Logger.getLogger(SWAPGateway.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void commandReceived(CommandMessage msg) {
            updateNetwork(msg);
        }
    }
}
