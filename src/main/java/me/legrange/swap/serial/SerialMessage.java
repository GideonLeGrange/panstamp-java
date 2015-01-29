package me.legrange.swap.serial;

import me.legrange.swap.*;

/**
 * A SWAP message received from the serial modem.
 *
 * @author gideon
 */
public class SerialMessage implements SwapMessage {
    
    @Override
    public Type getType() {
        return type;
    }

    @Override
    public int getFunction() {
        return type.function();
    }

    @Override
    public String getText() {
        return text;
    }
    @Override
    public int getRssi() {
        return rssi;
    }

    @Override
    public int getLqi() {
        return lqi;
    }

    @Override
    public int getSender() {
        return sender;
    }

    @Override
    public int getReceiver() {
        return receiver;
    }

    @Override
    public int getHops() {
        return hops;
    }

    @Override
    public int getSecurity() {
        return security;
    }

    @Override
    public int getSecurityNonce() {
        return securityNonce;
    }

    @Override
    public int getRegisterAddress() {
        return registerAddress;
    }

    @Override
    public int getRegisterID() {
        return registerID;
    }

    @Override
    public byte[] getRegisterValue() {
        return registerValue;
    }

    @Override
    public boolean isExtended() {
        return extended;
    }

    @Override
    public boolean isStandardRegister() {
        return getRegisterID() <= Registers.MAX_STANDARD_REGISTER.position();
    }

    @Override
    public Registers.Register getStandardRegister() throws SWAPException {
        return Registers.Register.byId(registerID);
    }

    @Override
    public String toString() {
        return text;
    }

    public SerialMessage(String text) throws DecodingException {
        this.text = text;
        if (text.contains(("("))) {
           text = text.replace("(", "").replace(")", ""); // get rid of the brackets around RSSI,LQI, WTF is up with that?
        }
        else {
            text = "0000" + text;
        }
        // convert the hex data to ints and pack it in an array
        int data[] = new int[text.length() / 2];
        for (int i = 0; i < text.length(); i = i + 2) {
            try {
                data[i / 2] = Integer.parseInt("" + text.charAt(i) + text.charAt(i + 1), 16);
            } catch (NumberFormatException ex) {
                throw new DecodingException(String.format("Invalid numeric data: '%s' (%02x %02x)", text, (int) text.charAt(i), (int) text.charAt(i + 1)), ex);
            }
        }
        int func = data[6];
        extended = ((func & 0b10000000) != 0);
        func = func & 0b01111111;
        switch (func) {
            case 0x0 : type = Type.STATUS;
                break;
            case 0x1  : type = Type.QUERY;
                break;
            case 0x2 : type = Type.COMMAND;
                break;
            default : 
                throw new DecodingException(String.format("Unknown function code 0x%x (message: '%s')", func, text));
        }
        rssi = (data[0]);
        lqi = (data[1]);
        hops = ((data[4] & 0xf0) >> 4);
        security = ((data[4] & 0x0f));
        securityNonce = (data[5]);
        byte value[] = new byte[data.length - 9];

        if (extended) {
            // set the values
            receiver = ((data[7] << 8) | data[8]);
            sender = ((data[2] << 8) | data[3]);
            registerAddress = (data[9] << 8 | data[10]);
            registerID = (data[10]);

            if (data.length > 9) {
                for (int i = 0; i < value.length; ++i) {
                    value[i] = (byte) data[12 + i];
                }
            }
        } else {
            // set the values
            sender = (data[3]);
            receiver = ((data[2]));
            registerAddress = (data[7]);
            registerID = (data[8]);
            if (data.length > 9) {
                for (int i = 0; i < value.length; ++i) {
                    value[i] = (byte) data[9 + i]; //gross
                }
            }
        }
        registerValue = (value);
    }

    private final Type type;
    private final boolean extended;
    private final int rssi;
    private final int lqi;
    private final int sender;
    private final int receiver;
    private final int hops;
    private final int security;
    private final int securityNonce;
    private final int registerAddress;
    private final int registerID;
    private final byte registerValue[];
    private final String text;

}
