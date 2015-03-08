package me.legrange.panstamp.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A device register definition.
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class RegisterDef {

    public RegisterDef(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /** Get the register ID of the register defined by this defintion. 
     * 
     * @return The register ID. 
     */
    public int getId() {
        return id;
    }

    /** Get the name of the register. 
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    /** Get the endpoint definitions defined for this register definition. 
     * 
     * @return This of endpoint definitions. 
     */
    public List<EndpointDef> getEndpoints() {
        List<EndpointDef> all = new ArrayList<>();
        all.addAll(endpoints.values());
        return all;
    }
    
    /** Get the parameter definitions defined for this register definition. 
     * 
     * @return This of parameter definitions. 
     */
    public List<Param> getParameters() { 
        List<Param> all = new ArrayList<>();
        all.addAll(params.values());
        return all;
    }
    
    /** Get the size in bytes of this register. 
     * 
     * @return The size. 
     */
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
