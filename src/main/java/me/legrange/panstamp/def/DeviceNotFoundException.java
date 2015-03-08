  package me.legrange.panstamp.def;

/**
 * Exception thrown if a panStamp device cannot be found 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class DeviceNotFoundException extends DefinitionException {

    DeviceNotFoundException(String message) {
        super(message);
    }

    DeviceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
