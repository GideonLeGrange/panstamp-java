package me.legrange.panstamp.core;

import me.legrange.panstamp.Parameter;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.def.Param;

/**
 *
 * @author gideon
 * @param <T>
 */
public abstract class AbstractParameter<T> implements Parameter<T>{

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
