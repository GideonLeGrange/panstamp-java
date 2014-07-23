package me.legrange.panstamp.impl;

import me.legrange.panstamp.impl.MoteException;

/**
 *
 * @author gideon
 */
class NoSuchRegisterException extends MoteException {

    public NoSuchRegisterException(String msg) {
        super(msg);
    }

    public NoSuchRegisterException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
