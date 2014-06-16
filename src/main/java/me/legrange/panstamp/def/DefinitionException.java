package me.legrange.panstamp.def;

/**
 * Thrown when there is a problem with panstamp device defintions.
 * @author gideon
 */
public abstract class DefinitionException extends Exception {

    public DefinitionException(String message) {
        super(message);
    }

    public DefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
