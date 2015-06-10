package example;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import me.legrange.panstamp.DeviceStateStore;
import me.legrange.panstamp.Register;

/**
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class ConfigDeviceStateStore implements DeviceStateStore {

    ConfigDeviceStateStore() {
        // NB NB NB 
        // hard code your panstamp registers here
        // example: I want to set register 0 for device 3 to 00010004 which means "product code for device 3 is temp sensor"
        data.put(makeKey(3,0), new byte[]{0,0,0,1,0,0,0,4});
    }
    
    // assume productCodes is filled in from the config file by the application
    private final Map<String, byte[]> data = new HashMap<>();

    @Override
    public boolean hasRegisterValue(Register reg) {
        String key = makeKey(reg);
        boolean res = data.containsKey(key);
        System.out.printf("hasRegisterValue(%d for %d) == %b\n", reg.getId(), reg.getDevice().getAddress(), res);
        return res;
    }

    @Override
    public byte[] getRegisterValue(Register reg) {
        byte val[] = data.getOrDefault(makeKey(reg), new byte[]{});
        System.out.printf("getRegisterValue(%d for %d) == %s\n", reg.getId(), reg.getDevice().getAddress(), formatBytes(val));
        return val;
    }

    @Override
    public void setRegisterValue(Register reg, byte[] value) {
        System.out.printf("setRegisterValue(%d for %d) = %s\n", reg.getId(), reg.getDevice().getAddress(), formatBytes(value));
        data.put(makeKey(reg), value);
    }
    
    private String makeKey(Register reg) {
        return makeKey(reg.getDevice().getAddress(), reg.getId());
    }
    private String makeKey(int addr, int id) {
        return String.format("%d=>%d", addr, id);
    }

    
    private String formatBytes(byte val[]) {
        return new BigInteger(val).toString(16);
    }
}
