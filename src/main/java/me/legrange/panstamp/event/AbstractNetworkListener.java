package me.legrange.panstamp.event;

import me.legrange.panstamp.Network;
import me.legrange.panstamp.NetworkListener;
import me.legrange.panstamp.PanStamp;

/**
 * An abstract implementation for a NetworkListener that developers can extend if 
 * they only what to implement one method in the GatewayListener interface. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public abstract class AbstractNetworkListener implements NetworkListener {

    @Override
    public void deviceDetected(Network gw, PanStamp dev) {
    }

    @Override
    public void deviceRemoved(Network gw, PanStamp dev) {
    }

    @Override
    public void networkOpened(Network nw) {
    }

    @Override
    public void networkClosed(Network nw) {
    }
    
    
    
    
}
