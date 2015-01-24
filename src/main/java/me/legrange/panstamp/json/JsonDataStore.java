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
import me.legrange.panstamp.GatewayException;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.impl.PanStampImpl;
import me.legrange.panstamp.impl.StandardRegister;
import me.legrange.panstamp.store.DataStore;
import me.legrange.panstamp.store.DataStoreException;
import me.legrange.panstamp.store.PanStampState;

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
    public void save(int addr, PanStampState state) throws DataStoreException {
        JsonObject jState = stateToJson(state);
        JsonObject jEntry = object(
                field("address", addr),
                field("registers", jState)
             );
    }

    @Override
    public PanStampState load(int addr) throws DataStoreException {
    }

    private JsonObject stateToJson(PanStampState state) {
        JsonObject jState = new JsonObject();
        for (StandardRegister sr : StandardRegister.ALL) {
            byte val[] = state.getState(sr);
            if (val.length > 0) {
                jState.put(sr.getName(), formatBytes(val));
            }
        }
        return jState;
    }
    
    private String formatBytes(byte bytes[]) { 
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02x", b));
        }
        return buf.toString();
    }

    private final String fileName;
}
