package me.legrange.swap.serial;

import me.legrange.swap.SWAPException;

/**
 * Thrown when there is a Serial IO error
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class SerialException extends SWAPException {

    SerialException(String msg) {
        super(msg);
    }

    SerialException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
