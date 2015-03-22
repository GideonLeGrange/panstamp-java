package example;

import me.legrange.panstamp.Network;

/**
 * Simple example: Open a gateway for a serial connected network.
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class OpenSerial {
    
    public static void main(String...args) throws Exception {
        Network nw = Network.openSerial("/dev/ttyUSB0", 38400);
        // Do things with the network 
        // close when done 
        nw.close();
        
    }

}
