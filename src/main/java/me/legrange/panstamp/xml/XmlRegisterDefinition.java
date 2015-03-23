package me.legrange.panstamp.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.legrange.panstamp.definition.EndpointDefinition;
import me.legrange.panstamp.definition.ParameterDefinition;
import me.legrange.panstamp.definition.RegisterDefinition;

/**
 * A device register definition.
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class XmlRegisterDefinition implements RegisterDefinition {

    public XmlRegisterDefinition(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<EndpointDefinition> getEndpoints() {
        List<EndpointDefinition> all = new ArrayList<>();
        all.addAll(endpoints.values());
        return all;
    }
    
    @Override
    public List<ParameterDefinition> getParameters() { 
        List<ParameterDefinition> all = new ArrayList<>();
        all.addAll(params.values());
        return all;
    }
    
    @Override
    public int getByteSize() {
        int size = 0;
        for (EndpointDefinition epDef : getEndpoints()) {
            size = size + epDef.getSize().getBytes();
        }
        return size;
    }
    
    protected void addEndpoint(XmlEndpointDefinition ep) {
        endpoints.put(ep.getName(), ep);
    }
    

    void addParameter(XmlParameterDefinition par) {
        params.put(par.getName(), par);
    }

    
    private final int id;
    private final String name;
    private final Map<String, XmlEndpointDefinition> endpoints = new HashMap<>();
    private final Map<String, XmlParameterDefinition> params = new HashMap<>();

    
}
