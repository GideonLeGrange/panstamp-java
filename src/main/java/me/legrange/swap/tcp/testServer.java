
package me.legrange.swap.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import me.legrange.swap.SWAPModem;

/**
 *
 * @author gideon
 */
public class testServer {
    
    public static final String PORT = "/dev/tty.usbserial-A800HNMV";
    
        public static void main(String...args) throws Exception {
            SWAPModem m = SWAPModem.openSerial(PORT,38400);
            TcpServer s = new TcpServer(m, 3333);
            System.out.println("Press enter to quit");
            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
            r.readLine();
            s.close();
            m.close();
        }
}
