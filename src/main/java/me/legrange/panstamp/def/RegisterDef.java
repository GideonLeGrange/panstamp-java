package me.legrange.panstamp.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A device register
 * @author gideon
 */
public class RegisterDef {

    public RegisterDef(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public List<EndpointDef> getEndpoints() {
        List<EndpointDef> all = new ArrayList<>();
        all.addAll(endpoints.values());
        return all;
    }
    
    public List<Param> getParameters() { 
        List<Param> all = new ArrayList<>();
        all.addAll(params.values());
        return all;
    }
    
    public int getByteSize() {
        int size = 0;
        for (EndpointDef epDef : getEndpoints()) {
            size = size + epDef.getSize().getBytes();
        }
        return size;
    }
    
    protected void addEndpoint(EndpointDef ep) {
        endpoints.put(ep.getName(), ep);
    }
    

    void addParameter(Param par) {
        params.put(par.getName(), par);
    }

    
    private final int id;
    private final String name;
    private final Map<String, EndpointDef> endpoints = new HashMap<>();
    private final Map<String, Param> params = new HashMap<>();

    
}
