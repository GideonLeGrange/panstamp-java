package me.legrange.panstamp.impl;

import me.legrange.panstamp.Parameter;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterEvent;
import me.legrange.panstamp.RegisterListener;
import me.legrange.panstamp.def.Param;

/**
 *
 * @author gideon
 * @param <T>
 */
public abstract class AbstractParameter<T> implements Parameter<T>, RegisterListener {

    @Override
    public String getName() {
        return par.getName();
    }

    @Override
    public final boolean hasValue() {
        return reg.hasValue();
    }


    AbstractParameter(RegisterImpl reg, Param par) {
        this.reg = reg;
        this.par = par;

    }

  
    @Override
    public void registerUpdated(RegisterEvent ev) {
        switch (ev.getType()) {
            case VALUE_RECEIVED:
                
            break;
        }
        
    }

    @Override
    public Register getRegister() {
        return reg;
    }

    protected final RegisterImpl reg;
    protected final Param par;



}
