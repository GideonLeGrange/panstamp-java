package me.legrange.panstamp.core;

import me.legrange.panstamp.Endpoint;
import me.legrange.panstamp.EndpointListener;

/**
 *
 * @author gideon
 */
public abstract class AbstractEndpointListener<T> implements EndpointListener<T> {

    @Override
    public void valueReceived(Endpoint<T> ep, T value) {
    }
    
}
