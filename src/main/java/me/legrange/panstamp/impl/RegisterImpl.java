package me.legrange.panstamp.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterListener;

/**
 * Abstraction of a panStamp register.
 *
 * @author gideon
 */
public class RegisterImpl implements Register {

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<Endpoint> getEndpoints() throws GatewayException {
        return dev.getEndpoints(id);
    }
    
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
        synchronized (this) {
            if (value == null) {
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

    @Override
    public boolean hasValue() {
        synchronized(this) {
            return value != null;
        }
    }
   

    /**
     * update the abstracted register value and notify listeners
     */
    void valueReceived(byte value[]) {
        synchronized (this) {
            this.value = value;
            notify();
        }
        fireEvent();
    }

    /**
     * create a new register for the given dev and register address
     */
    RegisterImpl(PanStampImpl mote, int id) {
        this.dev = mote;
        this.id = id;
    }

    private void fireEvent() {
        for (RegisterListener l : listeners) {
            pool.submit(new UpdateTask(l));
        }
    }

    private final PanStampImpl dev;
    private final int id;
    private final List<RegisterListener> listeners = new LinkedList<>();
    private byte[] value;
    private final ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Register Event Task");
            t.setDaemon(true);
            return t;
        }
    });

    private class UpdateTask implements Runnable {

        private UpdateTask(RegisterListener l) {
            this.l = l;
        }

        @Override
        public void run() {
            try {
                l.registerUpdated(RegisterImpl.this );
            } catch (Throwable e) {
                Logger.getLogger(SerialGateway.class.getName()).log(Level.SEVERE, null, e);

            }
        }

        private final RegisterListener l;
    }

}
