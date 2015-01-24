package me.legrange.panstamp.json;

import me.legrange.panstamp.GatewayException;

/**
 * Thrown when the Json converter cannot convert data correctly. 
 * @author gideon
 */
public class ConversionException extends GatewayException {

    public ConversionException(String msg) {
        super(msg);
    }

    public ConversionException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
    
}
