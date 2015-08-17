package me.legrange.panstamp;

import me.legrange.panstamp.definition.ParameterDefinition;

/**
 * An abstract implementation of Parameter that can be extended for different
 * types of parameters.
 *
 * @param <T>
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
abstract class AbstractParameter<T> implements Parameter<T> {

    @Override
    public String getName() {
        return par.getName();
    }

    @Override
    public final boolean hasValue() {
        return reg.hasValue();
    }

    @Override
    public Register getRegister() {
        return reg;
    }

    @Override
    public String getPattern() {
        String pat = par.getVerif().trim();
        if (pat.equals("")) {
            return "(.*)";
        }
        return pat;
    }

    @Override
    public int compareTo(Parameter<T> o) {
        int dif = getRegister().getDevice().getAddress() - o.getRegister().getDevice().getAddress();
        if (dif == 0) {
            dif = getRegister().getId() - o.getRegister().getId();
            if (dif == 0) {
                if (o instanceof AbstractParameter) {
                    AbstractParameter pa = (AbstractParameter) o;
                    dif = par.getPosition().getBytePos() - pa.par.getPosition().getBytePos();
                    if (dif == 0) {
                        dif = par.getPosition().getBitPos() - pa.par.getPosition().getBitPos();
                    }
                } else {
                    return getName().compareTo(o.getName()); // fall back to alpha order if we really can't figure out natural order.
                }
            }
        }
        return dif;
    }

    AbstractParameter(Register reg, ParameterDefinition par) {
        this.reg = reg;
        this.par = par;

    }

    protected final Register reg;
    protected final ParameterDefinition par;

}
