package me.legrange.panstamp.at;


import static me.legrange.panstamp.at.Direction.*;

/**
 * Abstraction of the AT commands supported by the SWAP modem
 * @author gideon
 */
public enum ATCommand {
    
    PC("Product code", 0, 8, READ_ONLY),
    HV("Hardware Version", 1, 4,  READ_ONLY),
    FV("Firmware Version", 2, 4, READ_ONLY),
    SS("System state", 3, 1, READ_WRITE),
    FC("Frequency channel", 4,1, READ_WRITE),
    SO("Security option", 5, 1, READ_WRITE),
    SN("Security nonce", 7, 1, READ_ONLY),
    NI("Network ID", 8, 2, READ_WRITE),
    DA("Device address", 9, 1,  READ_WRITE),
    TI("Transmit Interval", 10, 2, READ_WRITE);    
        
    public static ATCommand find(String name) throws InvalidCommandException {
        try {
            return ATCommand.valueOf(name.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            throw new InvalidCommandException(String.format("No command '%s' is known", name));
        }
    }
    
    public int register() {
        return reg;
    }
  
    private ATCommand(String desc, int reg, int len, Direction dir) {
        this.desc = desc;
        this.reg = reg;
        this.len = len;
        this.dir = dir;
    }
    
    private final String desc;
    private final int len;
    private final Direction dir;
    private final int reg;
    
}

