package me.legrange.panstamp.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.Register;

/**
 * Abstraction of a panStamp register.
 *
 * @author gideon
 */
public class RegisterImpl implements Register {

    /**
     * Add a listener to receive register updates
     *
     * @param l listener to add
     */
    @Override
    public void addListener(RegisterListener l) {
        listeners.add(l);
    }

    /**
     * remove a listener
     *
     * @param l listener to remove
     */
    @Override
    public void removeListener(RegisterListener l) {
        listeners.remove(l);
    }

    /**
     * set the register value and send to remote node
     *
     * @param value the new value
     * @throws me.legrange.panstamp.impl.MoteException
     */
    @Override
    public void setValue(byte value[]) throws GatewayException {
        try {
            dev.sendCommandMessage(id, value);
        } catch (ModemException e) {
            throw new MoteException(e.getMessage(), e);
        }
    }

    /**
     *
     * @return @throws ModemException
     * @throws NoSuchRegisterException
     */
    @Override
    public byte[] getValue() throws GatewayException {
        if (value == null) {
            synchronized (this) {
                try {
                    dev.sendQueryMessage(id);
                    wait();
                } catch (InterruptedException ex) {
                    throw new NoSuchRegisterException(String.format("Interrupted while waiting for register %d to update", id));
                }
            }
        }
        return value;
    }

    /**
     * update the abstracted register value and notify listeners
     */
    void valueReceived(byte value[]) {
        synchronized (this) {
            this.value = value;
            notify();
        }
        fireEvent(new RegisterEvent(this, value));
    }

    /**
     * create a new register for the given dev and register address
     */
    RegisterImpl(PanStampImpl mote, int id) {
        this.dev = mote;
        this.id = id;
    }

    private void fireEvent(RegisterEvent ev) {
        for (RegisterListener l : listeners) {
            pool.submit(new UpdateTask(l, ev));
        }
    }

    private final PanStampImpl dev;
    private final int id;
    private final List<RegisterListener> listeners = new LinkedList<>();
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private byte[] value;

    private class UpdateTask implements Callable {

        private UpdateTask(RegisterListener l, RegisterEvent e) {
            this.l = l;
            this.e = e;
        }

        @Override
        public Object call() {
            try {
                l.registerUpdated(e);
            } catch (Throwable e) {
                Logger.getLogger(SerialGateway.class.getName()).log(Level.SEVERE, null, e);

            }
            return true;
        }

        private final RegisterListener l;
        private final RegisterEvent e;
    }

}
