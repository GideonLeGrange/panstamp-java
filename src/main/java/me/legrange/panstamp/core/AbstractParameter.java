package me.legrange.panstamp.core;

import me.legrange.panstamp.Parameter;
import me.legrange.panstamp.Register;

/**
 * An abstract implementation of Parameter that can be extended for different types of
 * parameters. 
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

    AbstractParameter(RegisterImpl reg, Param par) {
        this.reg = reg;
        this.par = par;

    }

    protected final RegisterImpl reg;
    protected final Param par;

}
