package me.legrange.swap;

/**
 *
 * @author gideon
 */
public class UserMessage implements SwapMessage {

    public UserMessage(Type type, int sender, int receiver, int registerID, byte[] registerValue) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.registerAddress = receiver;
        this.registerID = registerID;
        this.registerValue = registerValue;
    }

    @Override
    public int getFunction() {
        return type.function();
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
    public Type getType() {
        return type;
    }

    @Override
    public boolean isExtended() {
        return extended;
    }

    @Override
    public int getRssi() {
        return 0;
    }

    @Override
    public int getLqi() {
        return 0;
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
        return 0;
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
    public String getText() {
        if (text == null) {
            text = pack();
        }
        return text;
    }
    
    @Override
    public String toString() { 
        return getText();
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
        text = null;
    }

    public void setSecurity(int security) {
        this.security = security;
        text = null;
    }

    public void setSecurityNonce(int securityNonce) {
        this.securityNonce = securityNonce;
        text = null;
    }

    public void setRegisterAddress(int registerAddress) {
        this.registerAddress = registerAddress;
        text = null;
    }
    
    /**
     * pack the message into a hex text for transmission to the modem
     */
    private String pack() {
        int data[];
        if (extended) {
            data = packExtended();
        } else {
            data = packStandard();
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            buf.append(String.format("%02X", data[i]));
        }
        return buf.toString();
    }

    /**
     * pack the standard address format message
     */
    private int[] packStandard() {
        int data[] = new int[7 + getRegisterValue().length];
        data[0] = getReceiver();
        data[1] = getSender();
        data[2] = (getHops() << 4) | getSecurity();
        data[3] = getSecurityNonce();
        data[4] = getFunction();
        data[5] = getRegisterAddress();
        data[6] = getRegisterID();
        if (type != Type.QUERY) {
            byte value[] = getRegisterValue();
            for (int i = 0; i < value.length; ++i) {
                int v = value[i];
                data[i + 7] = (v < 0 ? 256 + v : v);
            }
        }
        return data;
    }

    /**
     * pack the extended address format message
     */
    private int[] packExtended() {
        int data[] = new int[10 + getRegisterValue().length];
        data[0] = getReceiver() & 0xFF00 >>> 8;
        data[1] = getReceiver() & 0xFF;
        data[2] = (getHops() << 4) | getSecurity();
        data[3] = getSecurityNonce();
        data[4] = getFunction();
        data[5] = getSender() & 0xFF00 >>> 8;
        data[6] = getSender() & 0xFF;
        data[7] = getRegisterAddress() & 0xFF00 >>> 8;
        data[8] = getRegisterAddress() & 0xFF;
        data[9] = getRegisterID();
        if (type != Type.QUERY) {
            byte value[] = getRegisterValue();
            for (int i = 0; i < value.length; ++i) {
                int v = value[i];
                data[i + 10] = (v < 0 ? 256 + v : v);
            }
        }
        return data;
    }

    private final Type type;
    private boolean extended;
    private final int sender;
    private final int receiver;
    private int security;
    private int securityNonce;
    private int registerAddress;
    private final int registerID;
    private final byte registerValue[];
    private String text;

}
