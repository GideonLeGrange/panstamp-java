package me.legrange.panstamp.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A panStamp device configuration 
 * @author gideon
 */
public class DeviceDef {
    
    public DeviceDef(Developer developer, int id, String name, String label) {
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
    
    public List<RegisterDef> getRegisters() {
        List<RegisterDef> res = new ArrayList<>();
        res.addAll(registers.values());
        return res;
        
    }
 
    public boolean hasRegister(int id) {
        return registers.get(id) != null;
    }
    
    public RegisterDef getRegister(int id) {
        return registers.get(id);
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
    /*
    void addEndpoint(EndpointDef ep) {
        endpoints.put(ep.getName(), ep);
    }
    */
    
    void addRegister(RegisterDef rd) {
        registers.put(rd.getId(), rd);
    }
    
    private final int id;
    private final Developer developer;
    private final String name;
    private final String label;
    private String product;
    private boolean powerDownMode;
    private final Map<Integer, RegisterDef> registers = new HashMap<>();

}
