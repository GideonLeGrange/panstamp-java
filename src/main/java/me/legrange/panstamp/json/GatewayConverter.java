package me.legrange.panstamp.json;

import com.github.jsonj.JsonElement;
import com.github.jsonj.JsonObject;
import com.github.jsonj.tools.JsonBuilder;
import static com.github.jsonj.tools.JsonBuilder.field;
import static com.github.jsonj.tools.JsonBuilder.object;
import me.legrange.panstamp.Gateway;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.serial.SerialModem;
import me.legrange.swap.tcp.TcpModem;

/**
 * Convert Gateway objects to and from JSON
 *
 * @author gideon
 */
public class GatewayConverter implements Converter<Gateway> {

    @Override
    public String toJson(Gateway gw) {
        return object(
                field(SWAP_MODEM, modemToJson(gw.getSWAPModem())),
                field(NETWORK_CONFIG, configToJson(gw.getNetworkConfig()))
        ).prettyPrint();
    }

    @Override
    public Gateway fromJson(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private JsonElement modemToJson(SWAPModem modem) {
        JsonBuilder o = object();
        o.put("type", modem.getType().name());
        switch (modem.getType()) {
            case SERIAL:
                o.put("port", ((SerialModem) modem).getPort());
                o.put("baud", ((SerialModem) modem).getBaud());
                break;
            case TCP_IP:
                o.put("host", ((TcpModem) modem).getHost());
                o.put("port", ((TcpModem) modem).getPort());
                break;
        }
        o.put("setup", modemSetupAsJson(modem.getSetup()));
        return o.get();
    }

    private static JsonObject configAsJson(ModemSetup setup) {
        return object(
                field(NETWORK_ID, setup.getNetworkID()),
                field(CHANNEL, setup.getChannel()),
                field(DEVICE_ADDRESS, setup.getDeviceAddress())
        );
    }
    
    private static final String SWAP_MODEM = "swapModem";
    private static final String NETWORK_CONFIG = "networkConfig";
    private static final String NETWORK_ID = "networkId";
    private static final String CHANNEL = "channel";
    private static final String DEVICE_ADDRESS = "deviceAddress";


}
