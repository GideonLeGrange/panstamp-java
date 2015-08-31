package me.legrange.panstamp;

/**
 * Exception thrown if a duplicate panStamp is added to the network. 
 * @author gideon
 * @since 3.0
 */
class DuplicateDeviceException extends MoteException {

    public DuplicateDeviceException(String msg) {
        super(msg);
    }

    public DuplicateDeviceException(String msg, Throwable cause) {
        super(msg, cause);
    }


    
}
