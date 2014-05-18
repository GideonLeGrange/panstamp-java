package me.legrange.panstamp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A SWAP network.
 *
 * @author gideon
 */
public class SWAPNetwork implements Iterable<SWAPMote> {

    /**
     * create a new network for devices behind the given gateway
     */
    SWAPNetwork(SWAPGateway gw) {
        this.gw = gw;
    }

    /** Check if the network has a mode with the given address
     * @param addr Address we're looking for.
     * @return  Did we find it. */
    public boolean hasMote(int addr) {
        return map.get(addr) != null;

    }

    /** find the mote object for the given address */
    public SWAPMote getMote(int addr) throws NodeNotFoundException {
        SWAPMote mote = map.get(addr);
        if (mote == null) {
            throw new NodeNotFoundException(String.format("No SWAP devive with ID '%x' was found.", addr));
        }
        return mote;
    }
    
    
    @Override
    public Iterator<SWAPMote> iterator() {
        return map.values().iterator();
    }


    /** create a mote object for the given address */
    SWAPMote createMote(int addr) {
        SWAPMote mote = new SWAPMote(this, addr);
        map.put(addr, mote);
        return mote;
    }
    
    /** return the gateway supporting the network */
    SWAPGateway getGateway() {
        return gw;
    }

    private SWAPGateway gw;
    private Map<Integer, SWAPMote> map = new HashMap<>();

}
