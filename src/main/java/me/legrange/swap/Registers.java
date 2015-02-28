package me.legrange.swap;

/**
 * Standard SWAP registers as per
 * https://code.google.com/p/panstamp/wiki/SWAPregisters
 *
 * @author gideon
 */
public class Registers {
    
    public static final Register MAX_STANDARD_REGISTER = Register.PERIODIC_TX_INTERVAL;
   
    public enum Access {

        READ_ONLY, READ_WRITE
    };

    public enum Register {

        PRODUCT_CODE(0, 8, Access.READ_ONLY),
        HARDWARE_VERSION(1, 4, Access.READ_ONLY),
        FIRMWARE_VERSION(2, 4, Access.READ_ONLY),
        SYSTEM_STATE(3, 1, Access.READ_WRITE),
        FREQUENCY_CHANNEL(4, 1, Access.READ_WRITE),
        SECURITY_OPTION(5, 1, Access.READ_WRITE),
        SECURITY_PASSWORD(6, Access.READ_WRITE),
        SECURITY_NONCE(7, 1, Access.READ_ONLY),
        NETWORK_ID(8, 2, Access.READ_WRITE),
        DEVICE_ADDRESS(9, 1, Access.READ_WRITE),
        PERIODIC_TX_INTERVAL(10, 2, Access.READ_WRITE);
        
            public int position() { 
            return pos;
        }
        
        public static Register byId(int id) throws SWAPException {
            for (Register reg  :values()) {
                if (reg.pos == id) return reg;
            }
            throw new SWAPException(String.format("No register found for id %d", id));
        }

        private Register(int pos, Access access) {
            this(pos, -1, access);
        }

        private Register(int pos, int size, Access access) {
            this.pos = pos;
            this.size = size;
            this.access = access;
        }

        private final int pos;
        private final int size;
        private final Access access;
    }
}
