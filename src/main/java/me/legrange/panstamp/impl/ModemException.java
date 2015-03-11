package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;

/**
 * Thrown when there is a problem with modem communication.
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class ModemException extends GatewayException {

    ModemException(String msg) {
        super(msg);
    }

    ModemException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
