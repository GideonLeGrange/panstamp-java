package me.legrange.panstamp.def;

import java.util.LinkedList;
import java.util.List;

/**
 * A device endpoint 
 * @author gideon
 */
public class Endpoint {
    
    


     Endpoint(String name, Direction direction, Type type) {
        this.name = name;
        this.direction = direction;
        this.type = type;
        this.units = new LinkedList<>();
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

    void setPosition(Position position) {
        this.position = position;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public List<Unit> getUnits() {
        return units;
    }
    
    void addUnit(Unit unit) {
        units.add(unit);
    }
    
    
    
    private final String name;
    private final  Direction direction;
    private final Type type;
    private Position position;
    private Size size;
        private final List<Unit> units;

}
