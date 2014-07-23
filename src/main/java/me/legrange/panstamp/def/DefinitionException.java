package me.legrange.panstamp.def;

import me.legrange.panstamp.GatewayException;

/**
 * Thrown when there is a problem with panstamp device defintions.
 * @author gideon
 */
public abstract class DefinitionException extends GatewayException {

    public DefinitionException(String message) {
        super(message);
    }

    public DefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
