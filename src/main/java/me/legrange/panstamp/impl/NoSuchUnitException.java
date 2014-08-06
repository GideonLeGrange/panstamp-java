package me.legrange.panstamp.impl;

/**
 * Thrown when an endpoint is accessed with a unit that is not known.
 * @author gideon
 */
public class NoSuchUnitException extends MoteException {

    public NoSuchUnitException(String msg) {
        super(msg);
    }

    public NoSuchUnitException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
