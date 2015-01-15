package me.legrange.swap;

/**
 *
 * @author gideon
 */
public interface SwapMessage {
    
    
    public enum Type { QUERY(1), COMMAND(2), STATUS(0); 
        
        private Type(int function) {
            this.function = function;
        }
        
        public int function() { 
            return function;
        }
        
        private final int function;
        
    }
    
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

    Registers.Register getStandardRegister() throws SWAPException;

    boolean isExtended();

    boolean isStandardRegister();
    
    String getText();
    
}
