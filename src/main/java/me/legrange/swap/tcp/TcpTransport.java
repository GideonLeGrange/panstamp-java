package me.legrange.swap.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.swap.DecodingException;
import me.legrange.swap.ModemSetup;
import me.legrange.swap.SwapMessage;
import me.legrange.swap.serial.SerialMessage;

/**
 * A TCP transport that serializes and de-serializes certain data used by our
 * SWAP implementation over a TCP socket.
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
class TcpTransport {

    private static final String MESSAGE_START = "-";
    private static final String SETUP_START = "+";
    private static final String COMMAND_START = "!";
    private static final String SETUP_DEVICE_ADDRESS = "DA";
    private static final String SETUP_NETWORK_ID = "SW";
    private static final String SETUP_CHANNEL = "CH";

    /**
     * Create new transport using the given socket for IO
     */
    TcpTransport(Socket sock) throws IOException {
        this.sock = sock;
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
        reader = new Reader();
        start();
    }

    boolean isClosed() {
        return sock.isClosed();
    }

    /**
     * Add a listener to the transport. The listener will be called when
     * incoming data is received and decoded.
     *
     * @param l The listener to add
     */
    void addListener(TcpListener l) {
        listeners.add(l);
    }

    /**
     * Remove a listener from the transport
     *
     * @param l The listener to remove
     */
    void removeListener(TcpListener l) {
        listeners.remove(l);
    }

    /**
     * Send a SWAP message through the transport
     *
     * @param msg The message to send
     */
    void sendMessage(SwapMessage msg) {
        synchronized (out) {
            out.write(MESSAGE_START);
            out.write(msg.getText());
            out.write("\n");
            out.flush();
        }
    }

    /**
     * Send the modem setup through the transport
     *
     * @param setup The setup to send
     */
    void sendSetup(ModemSetup setup) {
        synchronized (out) {
            out.write(SETUP_START);
            out.write(String.format("%s=%d,", SETUP_DEVICE_ADDRESS, setup.getDeviceAddress()));
            out.write(String.format("%s=%d,", SETUP_CHANNEL, setup.getChannel()));
            out.write(String.format("%s=%d", SETUP_NETWORK_ID, setup.getNetworkID()));
            out.write("\n");
            out.flush();
        }
    }

    /**
     * Close the transport.
     */
    void close() throws IOException {
        sendCommand("quit");
        stop();
    }

    private void sendCommand(String cmd) {
        synchronized (out) {
            out.write(COMMAND_START);
            out.write(cmd);
            out.write("\n");
            out.flush();
        }
    }

    /**
     * start the transport
     */
    private void start() {
        running = true;
        reader.start();
    }

    private void stop() throws IOException {
        running = false;
        in.close();
        out.close();
        sock.close();
        reader.interrupt();
        listeners.clear();
    }

    private void fireEvent(SwapMessage msg) {
        for (TcpListener l : listeners) {
            l.messgeReceived(msg);
        }
    }

    private void fireEvent(ModemSetup setup) {
        for (TcpListener l : listeners) {
            l.setupReceived(setup);
        }
    }

    private void decodeSetup(String line) {
        int addr = 0;
        int chan = 0;
        int netId = 0;
        String parts[] = line.split(",");
        for (String part : parts) {
            String bits[] = part.split("=");
            if (bits.length == 2) {
                String name = bits[0];
                switch (name) {
                    case SETUP_DEVICE_ADDRESS:
                        addr = Integer.parseInt(bits[1]);
                        break;
                    case SETUP_CHANNEL:
                        chan = Integer.parseInt(bits[1]);
                        break;
                    case SETUP_NETWORK_ID:
                        netId = Integer.parseInt(bits[1]);
                        break;
                }
            }
        }
        ModemSetup setup = new ModemSetup(chan, netId, addr);
        fireEvent(setup);
    }

    private void decodeCommand(String cmd) throws IOException {
        switch (cmd.toLowerCase()) {
            case "quit":
                stop();
                break;
        }
    }

    /**
     * This class reads input from the TCP socket and decodes it, generating
     * events for messages or setup instructions
     */
    private class Reader extends Thread {

        private Reader() {
            super("TcpTransport reader");
        }

        @Override
        public void run() {
            while (running) {
                try {
                    String line = in.readLine();
                    if ((line != null) && (line.length() > 0)) {
                        String c = line.substring(0, 1);
                        switch (c) {
                            case MESSAGE_START:
                                fireEvent(new SerialMessage(line.substring(1)));
                                break;
                            case SETUP_START:
                                decodeSetup(line.substring(1));
                                break;
                            case COMMAND_START:
                                decodeCommand(line.substring(1));
                                break;
                            default:

                        }
                    }
                } catch (IOException | DecodingException ex) {
                    Logger.getLogger(TcpTransport.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private final Socket sock;
    private boolean running = true;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Reader reader;
    private final List<TcpListener> listeners = new CopyOnWriteArrayList<>();

}
