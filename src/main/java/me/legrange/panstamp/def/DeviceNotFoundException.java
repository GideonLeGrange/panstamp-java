package me.legrange.panstamp.def;

/**
 * Exception thrown if a panStamp device cannot be found 
 * @author gideon
 */
public final class DeviceNotFoundException extends DefinitionException {

    DeviceNotFoundException(String message) {
        super(message);
    }

    DeviceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
