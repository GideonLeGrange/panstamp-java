package me.legrange.panstamp.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.legrange.panstamp.definition.DeveloperDefinition;
import me.legrange.panstamp.definition.DeviceDefinition;
import me.legrange.panstamp.definition.RegisterDefinition;

/**
 * A panStamp device configuration. Typically loaded from XML.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class XMLDeviceDefinition implements DeviceDefinition {
    

    @Override
    public int getId() {
        return id;
    }

  
    @Override
    public DeveloperDefinition getDeveloper() {
        return developer;
    }

   
    @Override
    public String getName() {
        return name;
    }


    @Override
    public String getLabel() {
        return label;
    }


    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public boolean isPowerDownMode() {
        return powerDownMode;
    }

    @Override
    public List<RegisterDefinition> getRegisters() {
        List<RegisterDefinition> res = new ArrayList<>();
        res.addAll(registers.values());
        return res;

    }

    @Override
    public boolean hasRegister(int id) {
        return registers.get(id) != null;
    }

    @Override
    public RegisterDefinition getRegister(int id) {
        return registers.get(id);
    }

    @Override
    public String toString() {
        return "Device{" + "id=" + id + ", developer=" + developer + ", name=" + name + ", label=" + label + ", product=" + product + ", powerDownMode=" + powerDownMode + '}';
    }

    XMLDeviceDefinition(XMLDeveloperDefinition developer, int id, String name, String label) {
        this.id = id;
        this.developer = developer;
        this.name = name;
        this.label = label;
    }

    void setProduct(String product) {
        this.product = product;
    }

    void setPowerDownMode(Boolean pdm) {
        this.powerDownMode = pdm;
    }
   
    void addRegister(XMLRegisterDefinition rd) {
        registers.put(rd.getId(), rd);
    }

    private final int id;
    private final XMLDeveloperDefinition developer;
    private final String name;
    private final String label;
    private String product;
    private boolean powerDownMode;
    private final Map<Integer, XMLRegisterDefinition> registers = new HashMap<>();

}
