package me.legrange.panstamp.impl;

import java.util.HashMap;
import java.util.Map;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;

/**
 * An abstraction of a PanStampImpl
 *
 * @author gideon
 */
public class PanStampImpl implements PanStamp {

    /**
     * @return address of this mote
     */
    @Override
    public int getAddress() {
        return address;
    }

    /**
     * @return the register for the given id
     * @param id Register to read
     */
    @Override
    public Register getRegister(int id) {
        RegisterImpl reg = registers.get(id);
        if (reg == null) {
            reg = new RegisterImpl(this, id);
            registers.put(id, reg);
        }
        return reg;
    } 
    
    /** return the endpoint for the given name */
    @Override
    public Endpoint getEndpoint(String name) {
        Endpoint ep = endpoints.get(name);
        if (ep == null) {
        }
        return ep;
    }

    /** @return the timestamp the panstamp was last seen by the modem. */
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
        RegisterImpl reg = (RegisterImpl) getRegister(id);
        reg.updateValue(value);
    }

    /**
     * create a new mote for the given address in the given network
     */
    PanStampImpl(SerialGateway gw, int address) {
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
    private final SerialGateway gw;
    private long lastSeen;
    private int route;
    private final Map<Integer, RegisterImpl> registers = new HashMap<>();
    private final Map<String, Endpoint> endpoints = new HashMap<>();
}
