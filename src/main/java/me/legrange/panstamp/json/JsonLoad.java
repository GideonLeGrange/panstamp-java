package me.legrange.panstamp.json;

import com.github.jsonj.JsonElement;
import com.github.jsonj.JsonObject;
import com.github.jsonj.JsonType;
import com.github.jsonj.tools.JsonParser;
import me.legrange.panstamp.Gateway;

import static com.github.jsonj.JsonType.object;
import me.legrange.swap.SWAPModem;
import me.legrange.swap.serial.SerialModem;

/**
 *
 * @author gideon
 */
public class JsonLoad {
    
    public  Gateway loadJson(String text) throws JsonException {
        JsonParser parser = new JsonParser();
        JsonElement el = parser.parse(text);
        return parseNetwork(el);
    }
    
    private Gateway parseNetwork(JsonElement el) throws JsonException {
        expect(el, object);
        JsonObject o = el.asObject();
        SWAPModem modem = parseSwapModem(o.get("swapModem"));
        return null;
    }
    
    private SWAPModem parseSwapModem(JsonElement el) throws JsonException {
        expect(el, object);
        JsonObject o = el.asObject();
        String type = o.getString("type");
        switch (type) {
            case "SERIAL" : 
                String port = o.getString("port");
                int baud = o.getInt("baud");
        }
        return null;
    }
    
    private void expect(JsonElement el, JsonType...types) throws JsonException {
        StringBuilder expected = new StringBuilder();
        boolean first = true;
        for (JsonType type : types) {
            if (type == el.type()) {
                return;
            }
            if (first) {
                first = false;
            }
            else {
                expected.append(", ");
            }
            expected.append(type);
        }
        throw new JsonException(String.format("Found element of type '%s', expected '%s'", el.type().name(), expected));
    }
    
}
