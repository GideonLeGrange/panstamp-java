package me.legrange.swap.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.swap.MessageListener;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.SwapException;
import me.legrange.swap.SwapModem;
import me.legrange.swap.SwapMessage;
import me.legrange.swap.SerialException;

/**
 * A TCP server that serves SWAP comms from a modem over TCP/IP 
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public class TcpServer {

    public TcpServer(SwapModem sm, int port) throws TcpException {
        this.sm = sm;
        running = true;
        try {
            sock = new  ServerSocket(port);
        } catch (IOException ex) {
            throw new TcpException(ex.getMessage(), ex);
        }
        service = new Service();
        service.start();
        sm.addListener(new SwapListener());
    }

    public void close() throws TcpException {
        running = false;
        try {
            sock.close();
        } catch (IOException ex) {
            throw new TcpException(ex.getMessage(), ex);
        }
    }

    private void sendSetup(TcpTransport tt) throws SerialException, SwapException {
        ModemSetup setup = sm.getSetup();
        tt.sendSetup(setup);
    }

    private class Service extends Thread {

        private Service() {
            super("TcpServer accept thread");
        }

        @Override
        public void run() {
            while (running) {
                try {
                    final TcpTransport trans = new TcpTransport(sock.accept());
                    trans.addListener(new TcpListener() {

                        @Override
                        public void messgeReceived(SwapMessage msg) {
                            try {
                                sm.send(msg);
                                fireEvent(trans, msg);
                            } catch (SwapException ex) {
                                Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        @Override
                        public void setupReceived(ModemSetup setup) {
                            try {
                                sm.setSetup(setup);
                                fireEvent(trans, setup);
                            } catch (SwapException ex) {
                                Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);

                            }
                        }
                    });
                    sessions.add(trans);
                    sendSetup(trans);
                } catch (IOException ex) {
                    Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SerialException ex) {
                    Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SwapException ex) {
                    Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private void fireEvent(TcpTransport trans, SwapMessage msg) {
        for (TcpTransport t : sessions) {
            if (t != trans) {
                t.sendMessage(msg);
            }
        }
    }

    private void fireEvent(TcpTransport trans, ModemSetup setup) {
        for (TcpTransport t : sessions) {
            if (t != trans) {
                t.sendSetup(setup);
            }
        }
    }

    private class SwapListener implements MessageListener {

        @Override
        public void messageReceived(SwapMessage msg) {
            for (TcpTransport tt : sessions) {
                if (!tt.isClosed()) {
                    tt.sendMessage(msg);
                } else {
                    sessions.remove(tt);
                }
            }
        }

        @Override
        public void messageSent(SwapMessage msg) {
            for (TcpTransport tt : sessions) {
                if (!tt.isClosed()) {
                    tt.sendMessage(msg);
                } else {
                    sessions.remove(tt);
                }
            }
        }

    }

    private final SwapModem sm;
    private final ServerSocket sock;
    private final Service service;
    private final List<TcpTransport> sessions = new CopyOnWriteArrayList<>();
    private boolean running;

}
