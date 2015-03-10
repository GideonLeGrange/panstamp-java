package me.legrange.panstamp.definition;

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
public interface RegisterDefinition {


    /** Get the register ID of the register defined by this defintion. 
     * 
     * @return The register ID. 
     */
    public int getId();

    /** Get the name of the register. 
     * 
     * @return The name.
     */
    public String getName();
    
    /** Get the endpoint definitions defined for this register definition. 
     * 
     * @return This of endpoint definitions. 
     */
    public List<EndpointDefinition> getEndpoints();
    
    /** Get the parameter definitions defined for this register definition. 
     * 
     * @return This of parameter definitions. 
     */
    public List<ParameterDefinition> getParameters();
    
    /** Get the size in bytes of this register. 
     * 
     * @return The size. 
     */
    public int getByteSize();
    
}
