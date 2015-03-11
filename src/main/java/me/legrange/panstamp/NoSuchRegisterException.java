package me.legrange.panstamp;

/**
 * Thrown by a panStamp device if a register that is requested cannot be found. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
class NoSuchRegisterException extends MoteException {

    NoSuchRegisterException(String msg) {
        super(msg);
    }

    NoSuchRegisterException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
