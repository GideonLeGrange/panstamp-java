package me.legrange.panstamp.core;

import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.Parameter;
import me.legrange.panstamp.Register;
import me.legrange.panstamp.RegisterListener;

/**
 *
 * @author gideon
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
    public void parameteradded(Register reg, Parameter par) {
    }
    
}
