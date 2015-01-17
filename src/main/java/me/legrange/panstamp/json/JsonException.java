package me.legrange.panstamp.json;

/**
 * Exception thrown if there is a problem serializing Panstamp structures to Json.
 * @author gideon
 */
public class JsonException extends Exception {

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
