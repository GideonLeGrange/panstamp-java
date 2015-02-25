package me.legrange.panstamp;

import me.legrange.panstamp.def.Direction;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Position;
import me.legrange.panstamp.def.Size;
import me.legrange.panstamp.def.Type;

/**
 * // FIX ME - need to make standard register def contain standard endpoint def to be completely compatible with custom/loaded.
 * @author gideon
 */
public class StandardEndpoint extends EndpointDef {

    public static final StandardEndpoint MANUFACTURER_ID;
    public static final StandardEndpoint PRODUCT_ID;
    public static final StandardEndpoint HARDWARE_VERSION;
    public static final StandardEndpoint FIRMWARE_VERSION;
    public static final StandardEndpoint SYSTEM_STATE;
    public static final StandardEndpoint FREQUENCY_CHANNEL;
    public static final StandardEndpoint SECURITY_OPTION;
    public static final StandardEndpoint SECURITY_PASSWORD; 
    public static final StandardEndpoint SECURITY_NONCE;
    public static final StandardEndpoint NETWORK_ID;
    public static final StandardEndpoint DEVICE_ADDRESS;
    public static final StandardEndpoint PERIODIC_TX_INTERVAL;

    static {
        MANUFACTURER_ID = new StandardEndpoint(StandardRegister.PRODUCT_CODE, "Manufacturer Id", Direction.IN, Type.INTEGER);
        MANUFACTURER_ID.setSize(new Size(4, 0));
        MANUFACTURER_ID.setPosition(new Position(0));

        PRODUCT_ID = new StandardEndpoint(StandardRegister.PRODUCT_CODE, "Product Id", Direction.IN, Type.INTEGER);
        PRODUCT_ID.setSize(new Size(4, 0));
        PRODUCT_ID.setPosition(new Position(4))
                ;
        HARDWARE_VERSION = new StandardEndpoint(StandardRegister.HARDWARE_VERSION, "Hardware version", Direction.IN, Type.INTEGER);
        HARDWARE_VERSION.setSize(new Size(4, 0));
        
        FIRMWARE_VERSION = new StandardEndpoint(StandardRegister.FIRMWARE_VERSION, "Firmware version", Direction.IN, Type.INTEGER);
        FIRMWARE_VERSION.setSize(new Size(4, 0));
        
        SYSTEM_STATE = new StandardEndpoint(StandardRegister.SYSTEM_STATE, "System state", Direction.OUT, Type.INTEGER);
        
        FREQUENCY_CHANNEL = new StandardEndpoint(StandardRegister.FREQUENCY_CHANNEL, "Frequency channel", Direction.OUT, Type.INTEGER);
        
        SECURITY_OPTION = new StandardEndpoint(StandardRegister.SECURITY_OPTION, "Security option", Direction.OUT, Type.INTEGER);

        SECURITY_PASSWORD = new StandardEndpoint(StandardRegister.SECURITY_PASSWORD, "Security password", Direction.OUT, Type.INTEGER);

        SECURITY_NONCE = new StandardEndpoint(StandardRegister.SECURITY_NONCE, "Security nonce", Direction.IN, Type.INTEGER);

        NETWORK_ID = new StandardEndpoint(StandardRegister.NETWORK_ID, "Network ID", Direction.OUT, Type.INTEGER);
        NETWORK_ID.setSize(new Size(4, 0));

        DEVICE_ADDRESS = new StandardEndpoint(StandardRegister.DEVICE_ADDRESS, "Device address", Direction.OUT, Type.INTEGER);
        DEVICE_ADDRESS.setSize(new Size(1, 0));
       
        PERIODIC_TX_INTERVAL = new StandardEndpoint(StandardRegister.PERIODIC_TX_INTERVAL, "Periodic Tx interval", Direction.OUT, Type.INTEGER);
        PERIODIC_TX_INTERVAL.setSize(new Size(2, 0));
    }

    StandardEndpoint(StandardRegister reg, String name, Direction direction, Type type) {
        super(reg, name, direction, type);
        setSize(new Size(1,0));
        setPosition(new Position(0));
        reg.addEndpoint(this);
    }

}