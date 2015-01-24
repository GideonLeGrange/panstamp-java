package me.legrange.panstamp.json;

import com.github.jsonj.JsonElement;
import com.github.jsonj.JsonObject;
import com.github.jsonj.tools.JsonBuilder;
import static com.github.jsonj.tools.JsonBuilder.field;
import static com.github.jsonj.tools.JsonBuilder.object;
import com.github.jsonj.tools.JsonParser;
import me.legrange.panstamp.Gateway;
import me.legrange.panstamp.impl.ModemException;
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
    public String toJson(Gateway gw) throws ConversionException {
        return object(
                field(SWAP_MODEM, modemToJson(gw.getSWAPModem())),
                field(NETWORK_CONFIG, configToJson(gw))
        ).prettyPrint();
    }

    @Override
    public Gateway fromJson(String text) throws ConversionException {
        JsonParser parser = new JsonParser();
        JsonElement els = parser.parse(text);
        if (els.isObject()) {
            SWAPModem modem = jsonToModem(els.asObject().getObject(SWAP_MODEM));
        }
        else {
            throw new ConversionException(String.format("Expected a JSON object, but found a %s", els.type()));
        }
       System.out.println(" is a " + els.type());
       return null;
    }
    
    private SWAPModem jsonToModem(JsonObject ob) throws ConversionException {
        SWAPModem.Type type = SWAPModem.Type.valueOf(ob.getString(TYPE));
        switch (type) {
            case SERIAL:
                return jsonToSerialModem(ob);
            case TCP_IP : 
                return jsonToTcpModem(ob);
        }
        throw new ConversionException(String.format("Could not determine modem type for '%s'", type));
    }
    
    private SWAPModem jsonToSerialModem(JsonObject ob) {
        String port = ob.getString(PORT);
        int baud = ob.getInt(BAUD);
        return new SerialModem(port, baud);
    }


    private SWAPModem jsonToTcpModem(JsonObject ob) {
        String host = ob.getString(HOST);
        int port = ob.getInt(PORT);
        return new TcpModem(host, port);
    }

    private JsonElement modemToJson(SWAPModem modem) {
        JsonBuilder o = object();
        o.put(TYPE, modem.getType().name());
        switch (modem.getType()) {
            case SERIAL:
                o.put(PORT, ((SerialModem) modem).getPort());
                o.put(BAUD, ((SerialModem) modem).getBaud());
                break;
            case TCP_IP:
                o.put(HOST, ((TcpModem) modem).getHost());
                o.put(PORT, ((TcpModem) modem).getPort());
                break;
        }
        return o.get();
    }

    private JsonElement configToJson(Gateway setup) throws ConversionException {
        try {
            return object(
                    field(NETWORK_ID, setup.getNetworkId()),
                    field(CHANNEL, setup.getChannel()),
                    field(DEVICE_ADDRESS, setup.getDeviceAddress()),
                    field(SECURITY_OPTION, setup.getSecurityOption())
            );
        } catch (ModemException ex) {
            throw new ConversionException(ex.getMessage(), ex);
        }
    }
    
    private static final String TYPE = "type";
    private static final String PORT = "port";
    private static final String BAUD = "baud";
    private static final String HOST = "host";
    private static final String SWAP_MODEM = "swapModem";
    private static final String NETWORK_CONFIG = "networkConfig";
    private static final String NETWORK_ID = "networkId";
    private static final String CHANNEL = "channel";
    private static final String DEVICE_ADDRESS = "deviceAddress";
    private static final String SECURITY_OPTION = "securityOption";


}
