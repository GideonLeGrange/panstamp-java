package me.legrange.panstamp.def;

/**
 * Thrown when there is an error parsing the device files. 
 * @author gideon
 */
public class ParseException extends Exception {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    
}
