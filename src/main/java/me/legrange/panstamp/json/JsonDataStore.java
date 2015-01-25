package me.legrange.panstamp.json;

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
import me.legrange.panstamp.impl.StandardRegister;
import me.legrange.panstamp.store.DataStore;
import me.legrange.panstamp.store.DataStoreException;
import me.legrange.panstamp.store.RegisterState;

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
    public List<Integer> getAddresses() throws DataStoreException {
        JsonObject entries = getStateEntries();
        List<Integer> addrs = new LinkedList<>();
        for (String key : entries.keySet()) {
            addrs.add(Integer.parseInt(key));
        }
        return addrs;
    }

    @Override
    public void save(Integer addr, RegisterState state) throws DataStoreException {
        JsonObject jState = stateToJson(state);
        JsonObject jEntry = object(
                field(ADDRESS, addr),
                field(REGISTERS, jState)
             );
        JsonObject entries = getStateEntries();
        entries.put(key(addr), jEntry);
        write();
    }

    @Override
    public RegisterState load(Integer addr) throws DataStoreException {
        JsonObject entries = getStateEntries();
        if (entries.containsKey(key(addr))) {
            JsonObject jEntry = entries.getObject(key(addr));
            JsonObject jState = jEntry.getObject(REGISTERS);
            return jsonToState(jState);
        }
        throw new DataStoreException(String.format("Could not find saved state for address %d", addr));
    }
    
    private JsonObject getStateEntries() throws DataStoreException {
        JsonObject root = getRoot();
        if (!root.containsKey(ENTRIES)) {
            root.put(ENTRIES, object());
        } 
        return root.getObject(ENTRIES);
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
    
    private RegisterState jsonToState(JsonObject jState) {
        Map<StandardRegister, byte[]> state = new HashMap<>();
        for (StandardRegister sr : StandardRegister.ALL) {
            if (jState.containsKey(sr.getName())) {
                state.put(sr, parseBytes(jState.getString(sr.getName())));
            }
        }
        return new MapState(state);
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
    
    private static final String ENTRIES = "motes";
    private static final String ADDRESS = "address";
    private static final String REGISTERS = "registers";
}
