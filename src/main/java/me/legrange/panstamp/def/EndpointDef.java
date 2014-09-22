package me.legrange.panstamp.def;

import java.util.LinkedList;
import java.util.List;

/**
 * A device endpoint
 *
 * @author gideon
 */
public class EndpointDef {
    
    

    protected EndpointDef(RegisterDef reg, String name, Direction direction, Type type) {
        this.reg = reg;
        this.name = name;
        this.direction = direction;
        this.type = type;
        this.units = new LinkedList<>();
    }

    public RegisterDef getRegister() {
        return reg;
    }
    
    public String getName() {
        return name;
    }

    public Direction getDirection() {
        return direction;
    }

    public Type getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    protected void setPosition(Position position) {
        this.position = position;
    }

    public Size getSize() {
        return size;
    }

    protected void setSize(Size size) {
        this.size = size;
    }

    public List<Unit> getUnits() {
        return units;
    }

    protected void addUnit(Unit unit) {
        units.add(unit);
    }

    private final RegisterDef reg;
    private final String name;
    private final Direction direction;
    private final Type type;
    private Position position = new Position(0); // default to byte 0
    private Size size;
    private final List<Unit> units;

}
