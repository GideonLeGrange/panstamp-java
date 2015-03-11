package me.legrange.swap;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * A wrapper around a RXTX serial port.
 * 
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
final class ComPort  {

    /**
     * open the com port.
     *
     * @param portName Name/device file to open
     * @param speed Speed at which to communicate
     * @return The ComPort object created
     * @throws SerialException Thrown if there is a problem opening the port.
     */
    static ComPort open(String portName, int speed) throws SerialException {
        ComPort port = new ComPort();
        port.findAndOpen(portName, speed);
        return port;
    }

    /**
     * send a line of text to the com port
     */
    void send(String msg) throws SerialException {
        try {
            synchronized (outLock) {
                out.write(msg.getBytes());
                out.flush();
                System.out.println("sent: " + msg);
            }
        } catch (IOException ex) {
            throw new SerialException("IO error sending data to serial port: " + ex.getMessage(), ex);
        }
    }

    /**
     * read a line of text from the com port
     */
     String read() throws SerialException {
        try {
            synchronized (inLock) {
                return in.readLine();
            }
        } catch (IOException ex) {
            throw new SerialException("IO error reading data from serial port: " + ex.getMessage(), ex);
        }
    }

    /**
     * Close serial port
     */
     void close() throws SerialException {
        try {
            in.close();
            out.close();
            port.close();
        } catch (IOException ex) {
            throw new SerialException(ex.getMessage(), ex);
        }
    }

    /**
     * create a new instance
     */
    private ComPort() {
    }

    /**
     * find the port, open it and configure it to the correct baud, stop, parity
     * etc
     */
    private void findAndOpen(String portName, int baudRate) throws SerialException {
        CommPortIdentifier portId;
        try {
            portId = CommPortIdentifier.getPortIdentifier(portName);
        } catch (NoSuchPortException ex) {
            throw new SerialException(String.format("Could not find serial port '%s'", portName), ex);
        }
        try {
            port = portId.open(getClass().getSimpleName(), timeout);
            port.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            port.enableReceiveTimeout(Integer.MAX_VALUE);
            port.enableReceiveThreshold(0);
            in = new BufferedReader(new InputStreamReader(port.getInputStream()));
            out = port.getOutputStream();
            out.flush();
        } catch (PortInUseException ex) {
            throw new SerialException(String.format("Serial port '%s' is in use", portName), ex);
        } catch (UnsupportedCommOperationException | IOException ex) {
            throw new SerialException(String.format("Error accessing serial port '%s': %s", portName, ex.getMessage()), ex);
        }
    }

    private BufferedReader in;
    private OutputStream out;
    private SerialPort port;
    private final Object inLock = new Object();
    private final Object outLock = new Object();
    private static final int timeout = 60000;
}
