package me.legrange.panstamp;


/**
 * Super class of exceptions thrown by modules.
 * @author gideon
 */
public class ModuleException extends Exception {

    public ModuleException(String message) {
        super(message);
    }

    public ModuleException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
