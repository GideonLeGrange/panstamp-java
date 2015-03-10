package me.legrange.panstamp.core;

import me.legrange.panstamp.core.StandardEndpoint;
import me.legrange.panstamp.core.RegisterDef;

/**
 * This class provides register definitions for the standard SWAP registers to they can 
 * be used in the same way as other registers and endpoints. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class StandardRegister extends RegisterDef {

    public static final StandardRegister PRODUCT_CODE = new StandardRegister(0, "Product code");
    public static final StandardRegister HARDWARE_VERSION = new StandardRegister(1, "Hardware version");
    public static final StandardRegister FIRMWARE_VERSION = new StandardRegister(2, "Firmware version");
    public static final StandardRegister SYSTEM_STATE = new StandardRegister(3, "System state");
    public static final StandardRegister FREQUENCY_CHANNEL = new StandardRegister(4, "Frequency channel");
    public static final StandardRegister SECURITY_OPTION = new StandardRegister(5, "Security option");
    public static final StandardRegister SECURITY_PASSWORD = new StandardRegister(6, "Security password");
    public static final StandardRegister SECURITY_NONCE = new StandardRegister(7, "Security nonce");
    public static final StandardRegister NETWORK_ID = new StandardRegister(8, "Network ID");
    public static final StandardRegister DEVICE_ADDRESS = new StandardRegister(9, "Device address");
    public static final StandardRegister PERIODIC_TX_INTERVAL = new StandardRegister(10, "Periodic TX interval");
    public static final StandardRegister[] ALL = {PRODUCT_CODE, HARDWARE_VERSION, FIRMWARE_VERSION, SYSTEM_STATE,
        FREQUENCY_CHANNEL, SECURITY_OPTION, SECURITY_PASSWORD, SECURITY_NONCE, NETWORK_ID, DEVICE_ADDRESS, PERIODIC_TX_INTERVAL};
    public static final StandardRegister MAX = PERIODIC_TX_INTERVAL;
    
    static {
        final StandardEndpoint[] all = StandardEndpoint.ALL; 
        // hack. Sorry. Need to ensure that all standard endpoints are loaded
        // before we start using standard registers and I don't want to waste time on fixing the
        // chicken/egg relationship between those two now.
    }

    public  static StandardRegister forId(int id) {
        return ALL[id];
    }
    
    StandardRegister(int id, String name) {
        super(id, name);
    }

    void addEndpoint(StandardEndpoint ep) {
        super.addEndpoint(ep); 
    }
    
    
}
