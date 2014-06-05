package me.legrange.panstamp.device;

/**
 * Exception thrown if a panstamp device cannot be found 
 * @author gideon
 */
public final class DeviceNotFoundException extends Exception {

    DeviceNotFoundException(String message) {
        super(message);
    }

    DeviceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
