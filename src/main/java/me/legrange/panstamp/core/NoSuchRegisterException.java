package me.legrange.panstamp.core;

/**
 *
 * @author gideon
 */
public class NoSuchRegisterException extends MoteException {

    public NoSuchRegisterException(String msg) {
        super(msg);
    }

    public NoSuchRegisterException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
