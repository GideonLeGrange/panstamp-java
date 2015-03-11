  package me.legrange.panstamp;

import me.legrange.panstamp.definition.DefinitionException;

/**
 * Exception thrown if a panStamp device cannot be found 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class DeviceNotFoundException extends DefinitionException {

    public DeviceNotFoundException(String message) {
        super(message);
    }

    public DeviceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
