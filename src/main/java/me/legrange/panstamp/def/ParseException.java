package me.legrange.panstamp.def;

/**
 * Thrown when there is an error parsing the XML device files. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class ParseException extends DefinitionException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    
}
