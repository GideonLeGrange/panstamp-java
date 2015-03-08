package me.legrange.panstamp.def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A panStamp device configuration. Typically loaded from XML.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public final class DeviceDefinition {
    
    /** Get the definition ID (product ID)
     * 
     * @return The product ID. 
     */
    public int getId() {
        return id;
    }

    /** Get the developer owning this device definition 
     * 
     * @return The developer object. 
     */
    public Developer getDeveloper() {
        return developer;
    }

    /** Get the name of the device or product
     * 
     * @return The name. 
     */
    public String getName() {
        return name;
    }

    /** Get the label (more informative description / human readable description) of the 
     * device / product. This comes from the label attribute in the devices.xml file. 
     * 
     * @return The label.
     */
    public String getLabel() {
        return label;
    }

    /** Get the product description of the 
     * device / product. This comes from the product tag in the device's specific XML file. 
     * 
     * @return The label.
     */
    public String getProduct() {
        return product;
    }

    /** Get the power down mode of this device. 
     * 
     * @return True if the definition specifies that the device operates in power down mode. 
     */
    public boolean isPowerDownMode() {
        return powerDownMode;
    }

    /** Get the definitions for the registers defined in the device configuration. 
     * 
     * @return The list of register definitions. 
     */
    public List<RegisterDef> getRegisters() {
        List<RegisterDef> res = new ArrayList<>();
        res.addAll(registers.values());
        return res;

    }

    /** Check if the device definition contains a register definition for the given register ID. 
     * 
     * @param id The ID of the register requested. 
     * @return True if the register is defined. 
     */
    public boolean hasRegister(int id) {
        return registers.get(id) != null;
    }

    /** Return the register definition for the register with the given ID. 
     * 
     * @param id The ID of the register requested. 
     * @return The register definition. 
     */
    public RegisterDef getRegister(int id) {
        return registers.get(id);
    }

    @Override
    public String toString() {
        return "Device{" + "id=" + id + ", developer=" + developer + ", name=" + name + ", label=" + label + ", product=" + product + ", powerDownMode=" + powerDownMode + '}';
    }

    DeviceDefinition(Developer developer, int id, String name, String label) {
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
