package me.legrange.panstamp.def;

/**
 *
 * @author gideon
 */
public class NoSuchEndpointException extends DefinitionException {

   public NoSuchEndpointException(String message) {
        super(message);
    }

    NoSuchEndpointException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
