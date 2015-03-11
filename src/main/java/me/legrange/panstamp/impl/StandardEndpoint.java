package me.legrange.panstamp.impl;

import java.util.Collections;
import me.legrange.panstamp.definition.EndpointDefinition;
import java.util.List;
import me.legrange.panstamp.definition.Direction;
import me.legrange.panstamp.definition.Position;
import me.legrange.panstamp.definition.RegisterDefinition;
import me.legrange.panstamp.definition.Size;
import me.legrange.panstamp.definition.Type;
import me.legrange.panstamp.definition.Unit;

/**
 * This class provides endpoint definitions for the standard SWAP registers to they can 
 * be used in the same way as other registers and endpoints. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class StandardEndpoint implements EndpointDefinition {

    public static final StandardEndpoint MANUFACTURER_ID = new StandardEndpoint(StandardRegister.PRODUCT_CODE, "Manufacturer Id", Direction.IN, Type.INTEGER, new Size(4, 0));
    public static final StandardEndpoint PRODUCT_ID = new StandardEndpoint(StandardRegister.PRODUCT_CODE, "Product Id", Direction.IN, Type.INTEGER, new Size(4, 0), new Position(4,0));
    public static final StandardEndpoint HARDWARE_VERSION = new StandardEndpoint(StandardRegister.HARDWARE_VERSION, "Hardware version", Direction.IN, Type.INTEGER, new Size(4, 0));
    public static final StandardEndpoint FIRMWARE_VERSION = new StandardEndpoint(StandardRegister.FIRMWARE_VERSION, "Firmware version", Direction.IN, Type.INTEGER);
    public static final StandardEndpoint SYSTEM_STATE = new StandardEndpoint(StandardRegister.SYSTEM_STATE, "System state", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint FREQUENCY_CHANNEL = new StandardEndpoint(StandardRegister.FREQUENCY_CHANNEL, "Frequency channel", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint SECURITY_OPTION = new StandardEndpoint(StandardRegister.SECURITY_OPTION, "Security option", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint SECURITY_PASSWORD = new StandardEndpoint(StandardRegister.SECURITY_PASSWORD, "Security password", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint SECURITY_NONCE = new StandardEndpoint(StandardRegister.SECURITY_NONCE, "Security nonce", Direction.IN, Type.INTEGER);
    public static final StandardEndpoint NETWORK_ID = new StandardEndpoint(StandardRegister.NETWORK_ID, "Network ID", Direction.OUT, Type.INTEGER, new Size(2, 0));
    public static final StandardEndpoint DEVICE_ADDRESS = new StandardEndpoint(StandardRegister.DEVICE_ADDRESS, "Device address", Direction.OUT, Type.INTEGER);
    public static final StandardEndpoint PERIODIC_TX_INTERVAL = new StandardEndpoint(StandardRegister.PERIODIC_TX_INTERVAL, "Periodic Tx interval", Direction.OUT, Type.INTEGER, new Size(2, 0));

    public static final StandardEndpoint ALL[] = {MANUFACTURER_ID, PRODUCT_ID, HARDWARE_VERSION, FIRMWARE_VERSION,
        SYSTEM_STATE, FREQUENCY_CHANNEL, SECURITY_OPTION, SECURITY_PASSWORD, SECURITY_NONCE, NETWORK_ID, DEVICE_ADDRESS,
        PERIODIC_TX_INTERVAL};
    
    static {
//        FIRMWARE_VERSION.setSize(new Size(4, 0));
    }
    private StandardEndpoint(StandardRegister reg, String name, Direction direction, Type type) {
        this(reg, name, direction, type, new Size(1, 0));
    }
    
    private StandardEndpoint(StandardRegister reg, String name, Direction direction, Type type, Size size) {
        this(reg, name, direction, type, size, new Position(0));
    }
    
    private StandardEndpoint(StandardRegister reg, String name, Direction direction, Type type, Size size, Position position) {
        this.regDef = reg;
        this.name = name;
        this.direction = direction;
        this.type = type;
        this.size = size;
        this.position = position;
        reg.addEndpoint(this);
    }

    @Override
    public RegisterDefinition getRegister() {
        return regDef;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public List<Unit> getUnits() {
        return Collections.EMPTY_LIST;
    }
    
    private final RegisterDefinition regDef;
    private final String name;
    private final Type type;
    private final Direction direction;
    private Size size;
    private Position position;

}
