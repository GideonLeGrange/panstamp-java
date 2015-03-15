package me.legrange.panstamp;

/**
 * Thrown by a panStamp device if a register does not have a value but one is requested. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
class NoValueException extends MoteException {

    NoValueException(String msg) {
        super(msg);
    }

    NoValueException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
