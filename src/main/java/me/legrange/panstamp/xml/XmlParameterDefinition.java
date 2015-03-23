package me.legrange.panstamp.xml;

import me.legrange.panstamp.definition.ParameterDefinition;
import me.legrange.panstamp.definition.Position;
import me.legrange.panstamp.definition.Size;
import me.legrange.panstamp.definition.Type;

/**
 * A device parameter definition.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class XmlParameterDefinition implements ParameterDefinition {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    void setPosition(Position position) {
        this.position = position;
    }
    
    @Override
    public Size getSize() {
        return size;
    }

    void setSize(Size size) {
        this.size = size;
    }

    @Override
    public String getDefault() {
        return def;
    }

    void setDefault(String def) {
        this.def = def;
    }

    @Override
    public String getVerif() {
        return verif;
    }

    void setVerif(String verif) {
        this.verif = verif;
    }

    XmlParameterDefinition(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    private final String name;
    private final Type type;
    private Position position;
    private Size size;
    private String def;
    private String verif;
}
