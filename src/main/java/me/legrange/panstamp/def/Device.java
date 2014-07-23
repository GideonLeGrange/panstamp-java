package me.legrange.panstamp.def;

import java.util.HashMap;
import java.util.Map;

/**
 * A Panstamp device configuration 
 * @author gideon
 */
public class Device {
    
    public Device(Developer developer, int id, String name, String label) {
        this.id = id;
        this.developer = developer;
        this.name = name;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getProduct() {
        return product;
    }

    public boolean isPowerDownMode() {
        return powerDownMode;
    }
    
    public EndpointDef getEndpoint(String eName) throws NoSuchEndpointException {
        EndpointDef ep = endpoints.get(eName);
        if (ep == null) throw new NoSuchEndpointException(String.format("No endpoint '%s' in definition for device %s", eName, name));
        return ep;
    }
    
    public boolean hasEndpoint(String eName) { 
        return  endpoints.get(eName) != null;
    }

    @Override
    public String toString() {
        return "Device{" + "id=" + id + ", developer=" + developer + ", name=" + name + ", label=" + label + ", product=" + product + ", powerDownMode=" + powerDownMode + '}';
    }
    
    void setProduct(String product) {
        this.product = product;
    }

    void setPowerDownMode(Boolean pdm) {
        this.powerDownMode = pdm;
    }
    
    void addEndpoint(EndpointDef ep) {
        endpoints.put(ep.getName(), ep);
    }
    
    private final int id;
    private final Developer developer;
    private final String name;
    private final String label;
    private String product;
    private boolean powerDownMode;
    private final Map<String, EndpointDef> endpoints = new HashMap<>();

}
