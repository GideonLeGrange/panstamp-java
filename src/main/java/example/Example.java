package example;

import me.legrange.panstamp.Network;
import me.legrange.panstamp.NetworkException;

/**
 *
 * @author gideon
 */
public abstract class Example {

    private static final String PORT = "/dev/tty.usbserial-A800HNMV";
    private static final int BAUD = 38400;
    protected Network nw;
    
    protected abstract void doExampleCode(Network nw) throws NetworkException;
    
    protected void run() throws NetworkException {
         nw = Network.openSerial(PORT, BAUD);
         doExampleCode(nw);
         nw.close();
         System.exit(0);
    }
   

}
