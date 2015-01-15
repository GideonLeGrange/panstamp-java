package me.legrange.swap.serial;

import me.legrange.swap.SWAPException;

/**
 * Thrown when there is a Serial IO error
 * @author gideon
 */
public class SerialException extends SWAPException {

    SerialException(String msg) {
        super(msg);
    }

    SerialException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
