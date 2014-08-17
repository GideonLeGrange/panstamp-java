package me.legrange.panstamp.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.def.Device;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.NoSuchEndpointException;

/**
 * An implementation of a panStamp abstraction. Instances of this class
 * represent instances of panStamp devices connected to the network behind the
 * gateway.
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
        RegisterImpl reg;
        synchronized (registers) {
            reg = registers.get(id);
            if (reg == null) {
                reg = new RegisterImpl(this, id);
                registers.put(id, reg);
            }
        }
        return reg;
    }

    @Override
    public List<Register> getRegisters() throws GatewayException {
        checkEndpoints();
        Set<Register> regs = new HashSet<>();
        for (Endpoint ep : endpoints.values()) {
            regs.add(ep.getRegister());
        }
        return new ArrayList<>(regs);
    }

    /**
     * return the endpoint for the given name
     *
     * @throws me.legrange.panstamp.GatewayException
     */
    @Override
    public Endpoint getEndpoint(String name) throws GatewayException {
        checkEndpoints();
        Endpoint ep = endpoints.get(name);
        if (ep == null) {
            throw new NoSuchEndpointException(String.format("No endpoint '%s' was found", name));
        }
        return ep;
    }

    @Override
    public boolean hasEndpoint(String name) throws GatewayException {
        checkEndpoints();
        return endpoints.get(name) != null;
    }

    /**
     * returns all the endpoints for this panStamp
     *
     * @return Return the list of all endpoints for the device
     * @throws me.legrange.panstamp.GatewayException Thrown if there is a
     * problem determining endpoint information
     */
    @Override
    public List<Endpoint> getEndpoints() throws GatewayException {
        checkEndpoints();
        List<Endpoint> res = new ArrayList<>();
        res.addAll(endpoints.values());
        return res;
    }

    List<Endpoint> getEndpoints(final int registerId) throws GatewayException {
        synchronized (endpoints) {
            if (endpoints.isEmpty()) {
                loadEndpoints();
            }
        }
        List<Endpoint> res = new LinkedList<>();
        for (Endpoint ep : endpoints.values()) {
            if (ep.getRegister().getId() == registerId) {
                res.add(ep);
            }
        }
        return res;
    }

    /**
     * send a query message to the remote node
     */
    void sendQueryMessage(int id) throws ModemException {
        gw.sendQueryMessage(this, id);
    }

    /**
     * send a command message to the remote node
     *
     * @param value Value to send
     */
    void sendCommandMessage(int id, byte[] value) throws ModemException {
        gw.sendCommandMessage(this, id, value);
    }

    /**
     * Receive a status message from the remote node.
     */
    void statusMessageReceived(int id, byte[] value) throws GatewayException {
        RegisterImpl reg = (RegisterImpl) getRegister(id);
        reg.valueReceived(value);
    }

    /**
     * create a new mote for the given address in the given network
     */
    PanStampImpl(SerialGateway gw, int address) {
        this.gw = gw;
        this.address = address;
    }

    /**
     * load all endpoints
     */
    private void loadEndpoints() throws GatewayException {
        Device def = getDeviceDefinition();
        List<EndpointDef> epDefs = def.getEndpoints();
        for (EndpointDef epDef : epDefs) {
            endpoints.put(epDef.getName(), makeEndpoint(epDef));
        }
    }

    /**
     * make an endpoint object based on it's definition
     */
    private Endpoint makeEndpoint(EndpointDef epDef) throws NoSuchUnitException {
        switch (epDef.getType()) {
            case NUMBER:
                return new NumberEndpoint(this, epDef);
            case STRING:
                return new StringEndpoint(this, epDef);
            case BINARY:
                return new BinaryEndpoint(this, epDef);
            default:
                throw new NoSuchUnitException(String.format("Unknown end point type '%s'. BUG!", epDef.getType()));
        }
    }

    /**
     * get the device definition for this panStamp
     */
    private Device getDeviceDefinition() throws GatewayException {
        return gw.getDeviceDefinition(getManufacturerId(), getProductId());
    }

    /**
     * get the manufacturer id for this panStamp
     */
    private int getManufacturerId() throws GatewayException {
        Register reg = getRegister(0);
        byte val[] = reg.getValue();
        return val[0] << 24 | val[1] << 16 | val[2] << 8 | val[3];
    }

    /**
     * get the product id for this panStamp
     */
    private int getProductId() throws GatewayException {
        Register reg = getRegister(0);
        byte val[] = reg.getValue();
        return val[4] << 24 | val[5] << 16 | val[6] << 8 | val[7];

    }

    private synchronized void checkEndpoints() throws GatewayException {
        if (endpoints.isEmpty()) {
            loadEndpoints();
        }
    }

    private final int address;
    private final SerialGateway gw;
    private final Map<Integer, RegisterImpl> registers = new HashMap<>();
    private final Map<String, Endpoint> endpoints = new HashMap<>();

}
