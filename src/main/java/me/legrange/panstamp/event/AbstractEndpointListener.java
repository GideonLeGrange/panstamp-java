package me.legrange.panstamp.event;

import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointListener;

/**
 * An abstract implementation for an EndpointListener that developers can extend if 
 * they only what to implement one method in the EndpointListener interface. 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public abstract class AbstractEndpointListener<T> implements EndpointListener<T> {

    @Override
    public void valueReceived(Endpoint<T> ep, T value) {
    }
    
}
