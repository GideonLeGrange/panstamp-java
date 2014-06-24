package me.legrange.panstamp;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstraction of a SWAP register.
 *
 * @author gideon
 */
public class Register {

    public static class RegisterEvent {

        public Register getRegister() {
            return reg;
        }

        public byte[] getBytes() {
            return bytes;
        }

        private RegisterEvent(Register reg, byte bytes[]) {
            this.reg = reg;
            this.bytes = bytes;
        }

        private final Register reg;
        private final byte[] bytes;
    }

    /**
     * Implement this to receive updates when registers change.
     */
    public interface RegisterListener {

        void registerUpdated(RegisterEvent ev);
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
     * @throws me.legrange.panstamp.MoteException
     */
    public void setValue(byte value[]) throws MoteException {
        try {
            mote.updateRegister(id, value);
        } catch (ModemException e) {
            throw new MoteException(e.getMessage(), e);
        }
    }

    /**
     * update the abstracted register value and notify listeners
     */
    void updateValue(byte value[]) {
        RegisterEvent ev = new RegisterEvent(this, value);
        for (RegisterListener l : listeners) {
            l.registerUpdated(ev);
        }
    }

    /**
     * create a new register for the given mote and register address
     */
    Register(PanStamp mote, int id) {
        this.mote = mote;
        this.id = id;
    }

    private final PanStamp mote;
    private final int id;
    private final List<RegisterListener> listeners = new LinkedList<>();
}
