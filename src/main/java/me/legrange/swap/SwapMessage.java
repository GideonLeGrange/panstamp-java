package me.legrange.swap;

/**
 *
 * @author gideon
 */
public interface SwapMessage {
    
    /** Types of SWAP messages */
    public enum Type { QUERY(1), COMMAND(2), STATUS(0); 
        
        private Type(int function) {
            this.function = function;
        }
        
        public int function() { 
            return function;
        }
        
        private final int function;
        
    }
    
    /** Maximum register ID for standard SWAP messages */
    public static final int MAX_STANDARD_REGISTER = 10;
    
    Type getType();

    /**
     * @return function code
     */
    int getFunction();

    int getHops();

    int getLqi();

    int getReceiver();

    int getRegisterAddress();

    int getRegisterID();

    byte[] getRegisterValue();

    int getRssi();

    int getSecurity();

    int getSecurityNonce();

    int getSender();

    boolean isExtended();

    boolean isStandardRegister();
    
    String getText();
    
}
