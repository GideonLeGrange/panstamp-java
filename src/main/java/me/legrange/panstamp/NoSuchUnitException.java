package me.legrange.panstamp;


/**
 * Thrown when an endpoint is accessed with a unit that is not known.
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
class NoSuchUnitException extends MoteException {

    NoSuchUnitException(String msg) {
        super(msg);
    }

    NoSuchUnitException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
