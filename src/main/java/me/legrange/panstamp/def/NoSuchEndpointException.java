package me.legrange.panstamp.def;

/**
 * Thrown if an endpoint cannot be found. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class NoSuchEndpointException extends DefinitionException {

   public NoSuchEndpointException(String message) {
        super(message);
    }

    NoSuchEndpointException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
