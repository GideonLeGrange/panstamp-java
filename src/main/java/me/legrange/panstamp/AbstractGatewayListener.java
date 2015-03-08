package me.legrange.panstamp;

import me.legrange.panstamp.Gateway;
import me.legrange.panstamp.GatewayListener;
import me.legrange.panstamp.PanStamp;

/**
 * An abstract implementation for a GatewayListener that developers can extend if 
 * they only what to implement one method in the GatewayListener interface. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public abstract class AbstractGatewayListener implements GatewayListener {

    @Override
    public void deviceDetected(Gateway gw, PanStamp dev) {
    }

    @Override
    public void deviceRemoved(Gateway gw, PanStamp dev) {
    }
    
    
}
