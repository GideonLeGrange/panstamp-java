package me.legrange.panstamp;

import java.util.LinkedList;
import java.util.List;
import me.legrange.swap.SerialException;

/**
 * Abstraction of a SWAP register.
 *
 * @author gideon
 */
public class SWAPRegister {

    public static class RegisterEvent {

        public SWAPRegister getRegister() {
            return reg;
        }

        public byte[] getBytes() {
            return bytes;
        }

        private RegisterEvent(SWAPRegister reg, byte bytes[]) {
            this.reg = reg;
            this.bytes = bytes;
        }

        private final SWAPRegister reg;
        private final byte[] bytes;
    }

    /**
     * Implement this to receive updates when registers change.
     */
    public interface RegisterListener {

        void registerUpdated(RegisterEvent ev);
    }

    /**
     * return the time (in Java millis) since an update for the register was
     * last seen
     *
     * @return time last seen (in milliseconds)
     */
    public long getLastSeen() {
        return lastSeen;
    }

    /**
     * return the register value. If we do not have a recent value for the
     * register, one is requested and the call waits for a result
     *
     * @return the register value
     * @throws GatewayException if the value cannot be found
     */
    public byte[] getValue() throws GatewayException {
        if ((value == null) || ((System.currentTimeMillis() - lastSeen) > maxAge)) {
            try {
                mote.requestRegister(id);
            }
            catch (SerialException e) {
                throw new MoteException(e.getMessage(), e);
            }
            synchronized (this) {
                try {
                    wait(5000); // FIX ME, add adjustable timeout
                    if (value == null) {
                        throw new MoteException(String.format("Timed out waiting for register value"));
                    }
                } catch (InterruptedException e) {
                    throw new MoteException(String.format("Interrupted out waiting for register value"));
                }
            }
        }
        return value;
    }

    /**
     * Add a listener to receive register updates
     *
     * @param l listener to add
     */
    public void addListener(RegisterListener l) {
        listeners.add(l);
    }

    /**
     * remove a listener
     *
     * @param l listener to remove
     */
    public void removeListener(RegisterListener l) {
        listeners.remove(l);
    }

    /**
     * set the register value and send to remote node
     *
     * @param value the new value
     * @throws me.legrange.swap.GatewayException
     */
    public void setValue(byte value[]) throws GatewayException {
        this.value = value;
        try {
            mote.updateRegister(id, value);
        }
        catch (SerialException e) {
            throw new MoteException(e.getMessage(), e);
        }
    }

    /**
     * update the abstracted register value and notify listeners
     */
    void updateValue(byte value[]) {
        this.value = value;
        synchronized (this) {
            lastSeen = System.currentTimeMillis();
            notify();
        }
        RegisterEvent ev = new RegisterEvent(this, value);
        for (RegisterListener l : listeners) {
            l.registerUpdated(ev);
        }
    }

    /**
     * create a new register for the given mote and register address
     */
    SWAPRegister(SWAPMote mote, int id) {
        this.mote = mote;
        this.id = id;
    }

    private final SWAPMote mote;
    private final int id;
    private long lastSeen;
    private byte value[];
    private final List<RegisterListener> listeners = new LinkedList<>();
    private long maxAge;
}
