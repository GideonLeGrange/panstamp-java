package me.legrange.panstamp;

/**
 *
 * @author gideon
 */
public interface Register {
    
    public static class RegisterEvent {

        public Register getRegister() {
            return reg;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public RegisterEvent(Register reg, byte bytes[]) {
            this.reg = reg;
            this.bytes = bytes;
        }

        private final Register reg;
        private final byte[] bytes;
    }

    /**
     * Implement this to receive updates when registers change.
     */
    public interface RegisterListener {

        void registerUpdated(RegisterEvent ev);
    }

    /**
     * Add a listener to receive register updates
     *
     * @param l listener to add
     */
    public void addListener(RegisterListener l);

    /**
     * remove a listener
     *
     * @param l listener to remove
     */
    public void removeListener(RegisterListener l);
    /**
     * set the register value and send to remote node
     *
     * @param value the new value
     */
    public void setValue(byte value[]) throws GatewayException;
    
    public byte[] getValue() throws GatewayException;
    
}

