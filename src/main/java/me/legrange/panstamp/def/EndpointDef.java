package me.legrange.panstamp.def;

import java.util.LinkedList;
import java.util.List;

/**
 * Definition of an endpoint as defined by XML.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class EndpointDef {
    
    /** Get the register definition to which this endpoint definition applies. 
     * 
     * @return The Register definition.
     */
    public RegisterDef getRegister() {
        return reg;
    }
    
    /** Get the name of the endpoint. 
     * 
     * @return The endpoint name.
     */
    public String getName() {
        return name;
    }

    /** Get the direction of data flow for this endpoint. 
     * 
     * @return The direction.
     */
    public Direction getDirection() {
        return direction;
    }

    /** Get the data type for this endpoint. 
     * 
     * @return The type.
     */
    public Type getType() {
        return type;
    }

    /** Get the position of the endpoint data within the register. 
     * 
     * @return The position object. 
     */
    public Position getPosition() {
        return position;
    }

    protected void setPosition(Position position) {
        this.position = position;
    }

    /** Get the size of the endpoint data within the register. 
     * 
     * @return The size object. 
     */
    public Size getSize() {
        return size;
    }

    protected void setSize(Size size) {
        this.size = size;
    }

    /** Get the data units supported by the endpoint. 
     * 
     * @return The list of units. 
     */
    public List<Unit> getUnits() {
        return units;
    }

    protected void addUnit(Unit unit) {
        units.add(unit);
    }

    protected EndpointDef(RegisterDef reg, String name, Direction direction, Type type) {
        this.reg = reg;
        this.name = name;
        this.direction = direction;
        this.type = type;
        this.units = new LinkedList<>();
    }


    private final RegisterDef reg;
    private final String name;
    private final Direction direction;
    private final Type type;
    private Position position = new Position(0); // default to byte 0
    private Size size = new Size(1);
    private final List<Unit> units;

}
