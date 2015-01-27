package me.legrange.panstamp.core;

import me.legrange.panstamp.GatewayException;

/**
 * Thrown by a mote object when it experiences problems.
 * @author gideon
 */
public class MoteException extends GatewayException {

    public MoteException(String msg) {
        super(msg);
    }

    public MoteException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
