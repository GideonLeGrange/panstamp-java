package me.legrange.panstamp;

import me.legrange.panstamp.NetworkException;

/**
 * Thrown by a mote object when it experiences problems.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
class MoteException extends NetworkException {

    MoteException(String msg) {
        super(msg);
    }

    MoteException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
