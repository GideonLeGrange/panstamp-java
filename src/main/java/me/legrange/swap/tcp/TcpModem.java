package me.legrange.swap.tcp;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.swap.MessageListener;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.SwapException;
import me.legrange.swap.SwapModem;
import me.legrange.swap.SwapMessage;
import me.legrange.swap.SerialModem;

/**
 * A SWAP modem implementation that works over TCP.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class TcpModem implements SwapModem {

    public TcpModem(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void open() throws TcpException {
        try {
            running = true;
            setup = new ModemSetup(0, 0, 0);
            sock = new Socket(host, port);
            trans = new TcpTransport(sock);
            listener = new Listener();
            trans.addListener(listener);

        } catch (IOException ex) {
            throw new TcpException(ex.getMessage(), ex);
        }

    }

    @Override
    public void close() throws SwapException {
        running = false;
        try {
            trans.close();
            sock.close();
        } catch (IOException ex) {
            throw new TcpException(ex.getMessage(), ex);
        } finally {
            trans.removeListener(listener);
        }
    }

    @Override
    public boolean isOpen() {
        return running;
    }

    @Override
    public void send(SwapMessage msg) throws SwapException {
        trans.sendMessage(msg);
    }

    @Override
    public void addListener(MessageListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListener(MessageListener l) {
        listeners.remove(l);
    }

    @Override
    public ModemSetup getSetup() throws SwapException {
        if (setup == null) {
            setup = new ModemSetup(0,0,0);            
        }
        return setup;
    }

    @Override
    public void setSetup(ModemSetup newSetup) throws SwapException {
        trans.sendSetup(newSetup);
    }

    @Override
    public Type getType() {
        return Type.TCP_IP;
    }

    /**
     * Get the host the modem is connected to.
     *
     * @return The host name.
     */
    public String getHost() {
        return host;
    }

    /**
     * Get the TCP port the modem is connected to.
     *
     * @return The port.
     */
    public int getPort() {
        return port;
    }

    private void fireEvent(SwapMessage msg, ReceiveTask.Direction dir) {
        synchronized (listeners) {
            for (MessageListener l : listeners) {
                pool.submit(new ReceiveTask(l, msg, dir));
            }
        }
    }

    private class Listener implements TcpListener {

        @Override
        public void messgeReceived(SwapMessage msg) {
            fireEvent(msg, ReceiveTask.Direction.IN);
        }

        @Override
        public void setupReceived(ModemSetup newSetup) {
            setup = newSetup;
        }
    }

    private static class ReceiveTask implements Runnable {

        private enum Direction {

            IN, OUT;
        }

        private ReceiveTask(MessageListener listener, SwapMessage msg, Direction dir) {
            this.msg = msg;
            this.l = listener;
            this.dir = dir;
        }

        @Override
        public void run() {
            try {
                if (dir == Direction.IN) {
                    l.messageReceived(msg);
                } else {
                    l.messageSent(msg);
                }
            } catch (Throwable e) {
                Logger.getLogger(SerialModem.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        private final SwapMessage msg;
        private final MessageListener l;
        private final Direction dir;
    }

    private Socket sock;
    private final String host;
    private final int port;
    private TcpTransport trans;
    private boolean running;
    private final List<MessageListener> listeners = new CopyOnWriteArrayList<>();
    private ModemSetup setup;
    private Listener listener;
    private final ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "SWAP Listener Notification");
            t.setDaemon(true);
            return t;
        }
    });

}
