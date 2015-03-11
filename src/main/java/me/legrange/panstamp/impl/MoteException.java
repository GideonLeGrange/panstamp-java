package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;

/**
 * Thrown by a mote object when it experiences problems.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
class MoteException extends GatewayException {

    MoteException(String msg) {
        super(msg);
    }

    MoteException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
