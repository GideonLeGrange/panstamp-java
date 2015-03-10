package me.legrange.panstamp.definition;

import java.util.List;

/**
 * A panStamp device configuration. Typically loaded from XML, but a user can implement their own version of this.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public interface DeviceDefinition {
    
    /** Get the definition ID (product ID)
     * 
     * @return The product ID. 
     */
    public int getId(); 
    
    /** Get the developer owning this device definition 
     * 
     * @return The developer object. 
     */
    public DeveloperDefinition getDeveloper();

    /** Get the name of the device or product
     * 
     * @return The name. 
     */
    public String getName();

    /** Get the label (more informative description / human readable description) of the 
     * device / product. This comes from the label attribute in the devices.xml file. 
     * 
     * @return The label.
     */
    public String getLabel();

    /** Get the product description of the 
     * device / product. This comes from the product tag in the device's specific XML file. 
     * 
     * @return The label.
     */
    public String getProduct();

    /** Get the power down mode of this device. 
     * 
     * @return True if the definition specifies that the device operates in power down mode. 
     */
    public boolean isPowerDownMode();

    /** Get the definitions for the registers defined in the device configuration. 
     * 
     * @return The list of register definitions. 
     */
    public List<RegisterDefinition> getRegisters();

    /** Check if the device definition contains a register definition for the given register ID. 
     * 
     * @param id The ID of the register requested. 
     * @return True if the register is defined. 
     */
    public boolean hasRegister(int id);

    /** Return the register definition for the register with the given ID. 
     * 
     * @param id The ID of the register requested. 
     * @return The register definition. 
     */
    public RegisterDefinition getRegister(int id);
}
