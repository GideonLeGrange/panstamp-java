package me.legrange.panstamp.xml;

import java.util.LinkedList;
import java.util.List;
import me.legrange.panstamp.definition.Direction;
import me.legrange.panstamp.definition.EndpointDefinition;
import me.legrange.panstamp.definition.Position;
import me.legrange.panstamp.definition.Size;
import me.legrange.panstamp.definition.Type;
import me.legrange.panstamp.definition.Unit;

/**
 * Definition of an endpoint as defined by XML.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class XmlEndpointDefinition implements EndpointDefinition {

    @Override
    public XmlRegisterDefinition getRegister() {
        return reg;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public Type getType() {
        return type;
    }


    @Override
    public Position getPosition() {
        return position;
    }

    protected void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Size getSize() {
        return size;
    }

    protected void setSize(Size size) {
        this.size = size;
    }

    @Override
    public List<Unit> getUnits() {
        return units;
    }

    protected void addUnit(Unit unit) {
        units.add(unit);
    }

    protected XmlEndpointDefinition(XmlRegisterDefinition reg, String name, Direction direction, Type type) {
        this.reg = reg;
        this.name = name;
        this.direction = direction;
        this.type = type;
        this.units = new LinkedList<>();
    }


    private final XmlRegisterDefinition reg;
    private final String name;
    private final Direction direction;
    private final Type type;
    private Position position = new Position(0); // default to byte 0
    private Size size = new Size(1);
    private final List<Unit> units;

}
