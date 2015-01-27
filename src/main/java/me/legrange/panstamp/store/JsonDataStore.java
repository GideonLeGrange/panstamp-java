package me.legrange.panstamp.store;

import com.github.jsonj.JsonObject;
import static com.github.jsonj.tools.JsonBuilder.field;
import static com.github.jsonj.tools.JsonBuilder.object;
import com.github.jsonj.tools.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.StandardRegister;

/**
 * A data store that saves and loads PanStamp information to and from a JSON
 * file
 *
 * @author gideon
 */
public class JsonDataStore implements DataStore {

    public JsonDataStore(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Integer> getAddresses(int networkId) throws DataStoreException {
        JsonObject entries = getStateEntries(networkId);
        List<Integer> addrs = new LinkedList<>();
        for (String key : entries.keySet()) {
            addrs.add(Integer.parseInt(key));
        }
        return addrs;
    }

    @Override
    public void save(RegisterState state) throws DataStoreException {
        JsonObject jState = stateToJson(state);
        JsonObject jEntry = object(
                field(ADDRESS, state.getAddress()),
                field(REGISTERS, jState)
             );
        JsonObject entries;
        try {
            entries = getStateEntries(state.getNetworkId());
        } catch (GatewayException ex) {
            throw new DataStoreException(ex.getMessage(), ex);
        }
        entries.put(key(state.getAddress()), jEntry);
        write();
    }

    @Override
    public RegisterState load(Integer networkId, Integer addr) throws DataStoreException {
        JsonObject entries = getStateEntries(networkId);
        if (entries.containsKey(key(addr))) {
            JsonObject jEntry = entries.getObject(key(addr));
            JsonObject jState = jEntry.getObject(REGISTERS);
            return new MapState(networkId, addr,  jsonToState(jState));
        }
        throw new DataStoreException(String.format("Could not find saved state for address %d", addr));
    }
    
    private JsonObject getStateEntries(int networkId) throws DataStoreException {
        JsonObject root = getRoot();
        if (!root.containsKey(NETWORKS)) {
            root.put(NETWORKS, object());
        }
        JsonObject networks = root.getObject(NETWORKS);
        if (!networks.containsKey("" + networkId)) {
            networks.put("" + networkId, object());
        } 
        JsonObject network = root.getObject("" + networkId);
        if (!network.containsKey(DEVICES)) {
            network.put(DEVICES, object());
        }
        return root.getObject(DEVICES);
    }

    private JsonObject getRoot() throws DataStoreException {
        if (data == null) {
            File file = new File(fileName);
            if (file.exists()) {
                try {
                    data = new JsonParser().parseObject(new FileReader(file));
                } catch (IOException ex) {
                    throw new DataStoreException(ex.getMessage(), ex);
                }
            }
            else {
                data = new JsonObject();
            }
        }
        return data;
    }
     
    private String key(int addr) {
        return "" + addr;
    }
    
    private Map<StandardRegister, byte[]>  jsonToState(JsonObject jState) {
        Map<StandardRegister, byte[]> state = new HashMap<>();
        for (StandardRegister sr : StandardRegister.ALL) {
            if (jState.containsKey(sr.getName())) {
                state.put(sr, parseBytes(jState.getString(sr.getName())));
            }
        }
        return state;
    }
    
    private JsonObject stateToJson(RegisterState state) {
        JsonObject jState = new JsonObject();
        for (StandardRegister sr : StandardRegister.ALL) {
            byte val[] = state.getState(sr);
            if (val.length > 0) {
                jState.put(sr.getName(), formatBytes(val));
            }
        }
        return jState;
    }
    
    private byte[] parseBytes(String text) {
        String parts[] = text.split(",");
        byte bytes[] = new byte[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            bytes[i] = Byte.parseByte(parts[i], 16);
        }
        return bytes;
    }
    
    private String formatBytes(byte bytes[]) { 
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            byte b = bytes[i];
            if (i > 0) {
                buf.append(",");
            }
            buf.append(String.format("%02x", b));
        }
        return buf.toString();
    }

    private void write() throws DataStoreException {
                // now write to file
        try {
            try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
                out.println(data.prettyPrint());
            }
        }
        catch (IOException ex) {
            throw new DataStoreException(String.format("IO error writing JSON file '%s': %s", fileName, ex.getMessage()), ex);
        }
    }
    
    private final String fileName;
    private JsonObject data;

    private static final String NETWORKS = "networks";
    private static final String DEVICES = "devices";
    private static final String ADDRESS = "address";
    private static final String REGISTERS = "registers";
}
