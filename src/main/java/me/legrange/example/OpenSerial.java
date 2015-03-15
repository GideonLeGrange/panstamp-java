package me.legrange.example;

import me.legrange.panstamp.Gateway;

/**
 * Simple example: Open a gateway for a serial connected network.
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange
 */
public class OpenSerial {
    
    public static void main(String...args) throws Exception {
        Gateway gw = Gateway.createSerial("/dev/ttyUSB0", 38400);

        // close when done 
        gw.close();
        
    }

}
