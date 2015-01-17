package me.legrange.panstamp.json;

import com.github.jsonj.JsonArray;
import com.github.jsonj.JsonObject;
import com.github.jsonj.tools.JsonBuilder;
import me.legrange.panstamp.Gateway;
import static com.github.jsonj.tools.JsonBuilder.array;
import static com.github.jsonj.tools.JsonBuilder.field;
import static com.github.jsonj.tools.JsonBuilder.object;
import java.util.List;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.impl.ModemException;
import me.legrange.panstamp.impl.StandardRegister;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.SWAPException;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.serial.SerialModem;
import me.legrange.swap.tcp.TcpModem;

/**
 *
 * @author gideon
 */
public class JsonStore {

    public static String networkAsJson(Gateway gw) throws ModemException, SWAPException, GatewayException {
        return object(
                field("networkId", gw.getNetworkId()),
                field("swapModem", modemAsJson(gw.getSWAPModem())),
                field("devices", devicesAsJson(gw.getDevices()))
        ).prettyPrint();
    }

    private static JsonObject modemAsJson(SWAPModem modem) throws SWAPException {
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

    private static JsonObject modemSetupAsJson(ModemSetup setup) {
        return object(
                field("networkId", setup.getNetworkID()),
                field("channel", setup.getChannel()),
                field("deviceAddress", setup.getDeviceAddress())
        );
    }

    private static JsonArray devicesAsJson(List<PanStamp> devices) throws GatewayException {
        JsonArray arr = array();
        for (PanStamp ps : devices) {
            arr.add(panStampAsJson(ps));
        }
        return arr;
    }

    private static JsonObject panStampAsJson(PanStamp ps) throws GatewayException {
        JsonBuilder o = object();
        o.put("address", ps.getAddress());
        for (StandardRegister sr : StandardRegister.ALL) {
            int id = sr.getId();
            if (ps.hasRegister(id)) {
                Register reg = ps.getRegister(id);
                if (reg.hasValue()) {
                    o.put(reg.getName(), bytesAsString(reg.getValue()));
                }
            }
        }
        return o.get();
    }
    
    private static String bytesAsString(byte[] bytes) {
        StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (byte b : bytes) {
            if (first) { 
                first = false;
            }
            else {
                buf.append(",");
            }
            buf.append(String.format("0x%02x",b));
        }
        return buf.toString();
    }

}
