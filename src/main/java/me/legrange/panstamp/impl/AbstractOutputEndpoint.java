
package me.legrange.panstamp.impl;

import me.legrange.panstamp.OutputEndpoint;
import me.legrange.panstamp.PanStamp;
import me.legrange.panstamp.def.EndpointDef;

/**
 *
 * @author gideon
 */
abstract class AbstractOutputEndpoint<T> extends AbstractEndpoint<T> implements OutputEndpoint<T> {

    protected AbstractOutputEndpoint(PanStamp ps, EndpointDef epDef) {
        super(ps, epDef);
    }

    
}
