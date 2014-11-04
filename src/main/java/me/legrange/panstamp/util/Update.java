package me.legrange.panstamp.util;

import me.legrange.panstamp.Endpoint;

/**
 *
 * @author gideon
 */
public class Update {
    
    public Update(Endpoint ep, Object value) {
        this.ep = ep;
        this.value = value;
    }

    public Endpoint getEndpoint() {
        return ep;
    }

    public Object getValue() {
        return value;
    }
    
    
    
    private final Endpoint ep;
    private final Object value;
    
}
