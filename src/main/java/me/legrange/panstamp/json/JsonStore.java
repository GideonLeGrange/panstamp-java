package me.legrange.panstamp.store;

import com.github.jsonj.JsonArray;
import com.github.jsonj.JsonObject;
import me.legrange.panstamp.Gateway;
import static com.github.jsonj.tools.JsonBuilder.array;
import static com.github.jsonj.tools.JsonBuilder.field;
import static com.github.jsonj.tools.JsonBuilder.object;
import java.util.List;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.impl.ModemException;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.SWAPException;
import me.legrange.swap.SWAPModem;

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
        ).asString();
    }
    
    private static JsonObject modemAsJson(SWAPModem modem) throws SWAPException {
        return object(
                field("type", modem.getType().name()),
                field("setup",modemSetupAsJson(modem.getSetup()))
        );
    }
    
    private static JsonObject modemSetupAsJson(ModemSetup setup) {
        return object(
                field("networkId",setup.getNetworkID()),
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
        return object(
                field("address",ps.getAddress()),
                field("name",ps.getName())
        );
    }
    
}
