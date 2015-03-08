package me.legrange.panstamp;

import java.util.HashMap;
import java.util.Map;
import me.legrange.panstamp.def.Direction;
import me.legrange.panstamp.def.EndpointDef;
import me.legrange.panstamp.def.Position;
import me.legrange.panstamp.def.Size;
import me.legrange.panstamp.def.Type;

/**
 * This class provides endpoint definitions for the standard SWAP registers to they can 
 * be used in the same way as other registers and endpoints. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class StandardEndpoint extends EndpointDef {
    private static final Map<String, StandardEndpoint> nameMap = new HashMap<>();

    public static final StandardEndpoint MANUFACTURER_ID = new StandardEndpoint(StandardRegister.PRODUCT_CODE, "Manufacturer Id", Direction.IN, Type.INTEGER);
    public static final StandardEndpoint PRODUCT_ID = new StandardEndpoint(StandardRegister.PRODUCT_CODE, "Product Id", Direction.IN, Type.INTEGER);
    public static final StandardEndpoint HARDWARE_VERSION = new StandardEndpoint(StandardRegister.HARDWARE_VERSION, "Hardware version", Direction.IN, Type.INTEGER);
    public static final StandardEndpoint FIRMWARE_VERSION = new StandardEndpoint(StandardRegister.FIRMWARE_VERSION, "Firmware version", Direction.IN, Type.INTEGER);
    public static final StandardEndpoint SYSTEM_STATE = new StandardEndpoint(StandardRegister.SYSTEM_STATE, "System state", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint FREQUENCY_CHANNEL = new StandardEndpoint(StandardRegister.FREQUENCY_CHANNEL, "Frequency channel", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint SECURITY_OPTION = new StandardEndpoint(StandardRegister.SECURITY_OPTION, "Security option", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint SECURITY_PASSWORD = new StandardEndpoint(StandardRegister.SECURITY_PASSWORD, "Security password", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint SECURITY_NONCE = new StandardEndpoint(StandardRegister.SECURITY_NONCE, "Security nonce", Direction.IN, Type.INTEGER);
    public static final StandardEndpoint NETWORK_ID = new StandardEndpoint(StandardRegister.NETWORK_ID, "Network ID", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint DEVICE_ADDRESS = new StandardEndpoint(StandardRegister.DEVICE_ADDRESS, "Device address", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint PERIODIC_TX_INTERVAL = new StandardEndpoint(StandardRegister.PERIODIC_TX_INTERVAL, "Periodic Tx interval", Direction.OUT, Type.INTEGER);

    public static final StandardEndpoint ALL[] = {MANUFACTURER_ID, PRODUCT_ID, HARDWARE_VERSION, FIRMWARE_VERSION,
        SYSTEM_STATE, FREQUENCY_CHANNEL, SECURITY_OPTION, SECURITY_PASSWORD, SECURITY_NONCE, NETWORK_ID, DEVICE_ADDRESS,
        PERIODIC_TX_INTERVAL};
    
    public static StandardEndpoint forName(String name) {
        return nameMap.get(name);
    }

    static {
        MANUFACTURER_ID.setSize(new Size(4, 0));
        MANUFACTURER_ID.setPosition(new Position(0));
        PRODUCT_ID.setSize(new Size(4, 0));
        PRODUCT_ID.setPosition(new Position(4));
        HARDWARE_VERSION.setSize(new Size(4, 0));
        FIRMWARE_VERSION.setSize(new Size(4, 0));
        NETWORK_ID.setSize(new Size(2, 0));
        DEVICE_ADDRESS.setSize(new Size(1, 0));
        PERIODIC_TX_INTERVAL.setSize(new Size(2, 0));
        for (StandardEndpoint sep : ALL) {
            nameMap.put(sep.getName(), sep);
        }
        
    }

    private StandardEndpoint(StandardRegister reg, String name, Direction direction, Type type) {
        super(reg, name, direction, type);
        setSize(new Size(1, 0));
        setPosition(new Position(0));
        reg.addEndpoint(this);
    }

}
