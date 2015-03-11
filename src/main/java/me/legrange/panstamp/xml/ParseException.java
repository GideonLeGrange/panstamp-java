package me.legrange.panstamp.xml;

import me.legrange.panstamp.definition.DefinitionException;

/**
 * Thrown when there is an error parsing the XML device files. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class ParseException extends DefinitionException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    
}
