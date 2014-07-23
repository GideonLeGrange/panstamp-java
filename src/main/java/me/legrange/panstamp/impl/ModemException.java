package me.legrange.panstamp.impl;

import me.legrange.panstamp.GatewayException;

/**
 * Thrown when there is a problem with modem coms
 * @author gideon 
 */
public class ModemException extends GatewayException {

    public ModemException(String msg) {
        super(msg);
    }

    public ModemException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
