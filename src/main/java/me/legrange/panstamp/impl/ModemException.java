package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;

/**
 * Thrown when there is a problem with modem communication.
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class ModemException extends GatewayException {

    public ModemException(String msg) {
        super(msg);
    }

    public ModemException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
