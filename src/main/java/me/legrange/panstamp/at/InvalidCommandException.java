package me.legrange.panstamp.at;

import me.legrange.panstamp.MoteException;

/**
 *
 * @author gideon
 */
public class InvalidCommandException extends MoteException {

    public InvalidCommandException(String msg) {
        super(msg);
    }

    public InvalidCommandException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
