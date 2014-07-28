package me.legrange.panstamp.impl;

import java.util.LinkedList;
import java.util.List;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.Register;

/**
 * Abstraction of a SWAP register.
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
            dev.updateRegister(id, value);
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
                    dev.requestRegister(id);
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
    void updateValue(byte value[]) {
        synchronized (this) {
            this.value = value;
            notify();
        }
        RegisterEvent ev = new RegisterEvent(this, value);
        for (RegisterListener l : listeners) {
            l.registerUpdated(ev);
        }
    }

    /**
     * create a new register for the given dev and register address
     */
    RegisterImpl(PanStampImpl mote, int id) {
        this.dev = mote;
        this.id = id;
    }

    private final PanStampImpl dev;
    private final int id;
    private final List<RegisterListener> listeners = new LinkedList<>();
    private byte[] value;

}
