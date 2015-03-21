
package example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import me.legrange.swap.SwapModem;
import me.legrange.swap.SerialModem;
import me.legrange.swap.tcp.TcpServer;

/**
 *
 * @author gideon
 */
public class testServer {
    
    public static final String PORT = "/dev/tty.usbserial-A800HNMV";
    
        public static void main(String...args) throws Exception {
            SwapModem m =  new SerialModem(PORT,38400);
            m.open();
            TcpServer s = new TcpServer(m, 3333);
            System.out.println("Press enter to quit");
            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
            r.readLine();
            s.close();
            m.close();
        }
}
