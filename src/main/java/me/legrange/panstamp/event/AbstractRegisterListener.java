package me.legrange.panstamp.event;

import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.Parameter;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterListener;

/**
 * An abstract implementation for a RegisterListener that developers can extend if 
 * they only what to implement one method in the RegisterListener interface. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public abstract class AbstractRegisterListener implements RegisterListener {

    @Override
    public void valueReceived(Register reg, byte[] value) {
    }

    @Override
    public void valueSet(Register reg, byte[] value) {
    }

    @Override
    public void endpointAdded(Register reg, Endpoint ep) {
    }

    @Override
    public void parameterAdded(Register reg, Parameter par) {
    }
    
}
