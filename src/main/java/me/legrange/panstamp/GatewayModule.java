
package me.legrange.panstamp;

import java.util.Properties;

/**
 * Implement this module to integrate your code into the XBeeGatewayServer
 * @author gideon
 */
public interface GatewayModule {
    
    /** start the module. The gateway on which the module runs is passed */
    void start(SWAPGateway gw, Properties conf) throws ModuleException;
    
    /** stop the module. The module or possibly the whole gateway is stopping, so do the needed cleanup */
    void stop() throws ModuleException;
}
