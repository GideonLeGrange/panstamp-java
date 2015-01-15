package me.legrange.swap.serial;

import me.legrange.swap.ModemSetup;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.legrange.swap.DecodingException;
import me.legrange.swap.SwapMessage;
import me.legrange.swap.MessageListener;
import me.legrange.swap.SWAPException;
import me.legrange.swap.SWAPModem;

/**
 * An interface providing access through the serial port to the SWAP modem.
 *
 * @author gideon
 */
public final class SerialModem implements SWAPModem {

    public static SerialModem open(String port, int baud) throws SWAPException {
        SerialModem modem = new SerialModem(port, baud);
        modem.open();
        return modem;
    }

    @Override
    public void close() throws SerialException {
        running = false;
        com.close();
    }

    @Override
    public synchronized void send(SwapMessage msg) throws SerialException {
        com.send(msg.getText() + "\r");
        fireEvent(msg, ReceiveTask.Direction.OUT);
    }

    @Override
    public void addListener(MessageListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    @Override
    public void removeListener(MessageListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    @Override
    public ModemSetup getSetup() throws SerialException {
        if (setup == null) {
            synchronized (this) {

                enterCommandMode();
                setup = new ModemSetup(readATasInt("ATCH?"), readATasHex("ATSW?"),
                        readATasHex("ATDA?"));
                leaveCommandMode();
            }
        }
        return setup;
    }
    
    @Override
    public void setSetup(ModemSetup setup) throws SerialException {
        synchronized(this) {
            enterCommandMode();
            sendATCommand(String.format("ATCH=%2d", setup.getChannel()));
            sendATCommand(String.format("ATSW=%4h", setup.getNetworkID()));
            sendATCommand(String.format("ATDA=%2d", setup.getDeviceAddress()));
            leaveCommandMode();
            this.setup = setup;
        }
    }
    
    @Override
    public Type getType() {
        return Type.SERIAL;
    }
    
    private int readATasInt(String cmd) throws SerialException {
        String res = readAT(cmd);
        try {
            return Integer.parseInt(res);
        } catch (NumberFormatException e) {
            throw new SerialException(String.format("Malformed integer response '%s' to %s comamnd", res, cmd));
        }
    }

    private int readATasHex(String cmd) throws SerialException {
        String res = readAT(cmd);
        try {
            return Integer.parseInt(res, 16);
        } catch (NumberFormatException e) {
            throw new SerialException(String.format("Malformed hex response '%s' to %s comamnd", res, cmd));
        }
    }

    private String readAT(String cmd) throws SerialException {
        String res = sendATCommand(cmd);
        switch (res) {
            case "ERROR":
                throw new SerialException(String.format("Error on %s command", cmd));
            case "OK":
                throw new SerialException(String.format("Unexpected OK in %s command", cmd));
            default:
                return res;
        }
    }

    private void enterCommandMode() throws SerialException {
        if (mode == Mode.COMMAND) {
            return;
        }
        while (mode == Mode.INIT)  {
            System.out.println("Waiting for modem");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialModem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        com.send("+++");
        while (mode != Mode.COMMAND) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }

    private void leaveCommandMode() throws SerialException {
        if (mode == Mode.DATA) {
            return;
        }
        com.send("ATO\r");
        while (mode != Mode.DATA) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }

        }
    }

    private String sendATCommand(String cmd) throws SerialException {
        com.send(cmd + "\r");
        String res;
        try {
            return results.take();
        } catch (InterruptedException ex) {
            throw new SerialException("Interruped waiting for AT response");
        }
    }

    private SerialModem(String port, int baud) {
        this.listeners = new LinkedList<>();
        this.port = port;
        this.baud = baud;
    }

    /**
     * open the modem
     *
     * @throws me.legrange.swap.SWAPException Thrown if the gateway has problems
     * starting
     */
    private void open() throws SWAPException {
        com = ComPort.open(port, baud);
        running = true;
        reader = new Reader();
        reader.setDaemon(true);
        reader.setName(String.format("%s Reader Thread", getClass().getSimpleName()));
        reader.start();
    }

    /**
     * send the received message to listeners
     */
    private void fireEvent(SwapMessage msg, ReceiveTask.Direction dir) {
        synchronized (listeners) {
            for (MessageListener l : listeners) {
                pool.submit(new ReceiveTask(l, msg, dir));
            }
        }
    }

    private enum Mode {

        INIT, DATA, COMMAND
    };

    private ComPort com;
    private Mode mode = Mode.DATA;
    private ModemSetup setup;
    private final BlockingQueue<String> results = new LinkedBlockingQueue<>();
    private Reader reader;
    private boolean running;
    private final List<MessageListener> listeners;
    private final int baud;
    private final String port;
    private final ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "SWAP Listener Notification");
            t.setDaemon(true);
            return t;
        }
    });

    /**
     * The reader thread that receives data from the modem, unpacks it into
     * messages and send the messages to the listeners.
     */
    private class Reader extends Thread {

        @Override
        public void run() {
            while (running) {
                try {
                    String in = com.read();
                    if (in.length() == 0) {
                        continue; 
                    }
                    System.out.printf("R: [%s]\n", in);
                    if ((in.length() >= 12) && in.startsWith("(")) {
                        mode = Mode.DATA;
                        fireEvent(new SerialMessage(in), ReceiveTask.Direction.IN);
                    } else {
                        // handle AT responses here
                        switch (in) {
                            case "OK-Command mode":
                                mode = Mode.COMMAND;
                                break;
                            case "Modem ready!":
                            case "OK-Data mode":
                                mode = Mode.DATA;
                                break;
                            case "OK":
                            case "ERROR":
                            default:
                                results.add(in);

                        }
                    }
                } catch (SerialException ex) {
                    Logger.getLogger(SerialModem.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DecodingException ex) {
                    Logger.getLogger(SerialModem.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Throwable ex) {
                    Logger.getLogger(SerialModem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
}
