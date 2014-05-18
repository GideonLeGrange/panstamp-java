package me.legrange.panstamp;

import java.util.HashMap;
import java.util.Map;
import me.legrange.swap.SerialException;

/**
 * An abstraction of a SWAP mote (PanStamp)
 *
 * @author gideon
 */
public class SWAPMote {

    /**
     * @return address of this mote
     */
    public int getAddress() {
        return address;
    }

    /**
     * @return the register for the given id
     * @param id Register to read
     */
    public SWAPRegister getRegister(int id) {
        SWAPRegister reg = registers.get(id);
        if (reg == null) {
            reg = new SWAPRegister(this, id);
            registers.put(id, reg);
        }
        return reg;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    /**
     * request the register value from the remote node
     */
    void requestRegister(int id) throws ModemException  {
        network.getGateway().requestRegister(this, id);
    }

    /**
     * update the register value on the remote node
     *
     * @param value Value to send
     */
    void updateRegister(int id, byte[] value) throws ModemException {
        network.getGateway().setRegister(this, id, value);
    }

    /**
     * Update the state of a mote based on status message
     */
    void update(int id, byte[] value) throws MoteException {
        SWAPRegister reg = getRegister(id);
        reg.updateValue(value);
    }

    /**
     * create a new mote for the given address in the given network
     */
    SWAPMote(SWAPNetwork network, int address) {
        this.network = network;
        this.address = address;
    }

    /**
     * set the time the mote was last seen
     */
    void setLastSeen(long last) {
        this.lastSeen = last;
    }

    /**
     * set the route taken to reach this mote - this is the address of a mote
     * that will store and forward messages to this mote, usually in case where
     * a mote can sleep
     *
     * @param route the route to this mote.
     */
    void setRoute(int route) {
        this.route = route;
    }

    /**
     * get the route taken to reach this mote - this is the address of a mote
     * that will store and forward messages to this mote, usually in case where
     * a mote can sleep
     *
     * @return the route to this mote.
     */
    int getRoute() {
        return route;
    }
    private final int address;
    private final SWAPNetwork network;
    private long lastSeen;
    private int route;
    private final Map<Integer, SWAPRegister> registers = new HashMap<>();
    private static final long MAX_AGE = 60000;
}
