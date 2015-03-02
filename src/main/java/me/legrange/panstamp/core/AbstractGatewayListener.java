package me.legrange.panstamp.core;

import me.legrange.panstamp.Gateway;
import me.legrange.panstamp.GatewayListener;
import me.legrange.panstamp.PanStamp;

/**
 *
 * @author gideon
 */
public abstract class AbstractGatewayListener implements GatewayListener {

    @Override
    public void deviceDetected(Gateway gw, PanStamp dev) {
    }

    @Override
    public void deviceRemoved(Gateway gw, PanStamp dev) {
    }
    
    
}
