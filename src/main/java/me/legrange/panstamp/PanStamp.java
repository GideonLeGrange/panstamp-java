package me.legrange.panstamp;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstraction of a PanStamp
 *
 * @author gideon
 */
public class PanStamp {

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
    public Register getRegister(int id) {
        Register reg = registers.get(id);
        if (reg == null) {
            reg = new Register(this, id);
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
        gw.requestRegister(this, id);
    }

    /**
     * update the register value on the remote node
     *
     * @param value Value to send
     */
    void updateRegister(int id, byte[] value) throws ModemException {
        gw.setRegister(this, id, value);
    }

    /**
     * Update the state of a mote based on status message
     */
    void update(int id, byte[] value) throws MoteException {
        Register reg = getRegister(id);
        reg.updateValue(value);
    }

    /**
     * create a new mote for the given address in the given network
     */
    PanStamp(Gateway gw, int address) {
        this.gw = gw;
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
    private final Gateway gw;
    private long lastSeen;
    private int route;
    private final Map<Integer, Register> registers = new HashMap<>();
}
