package me.legrange.swap.serial;

import static me.legrange.swap.serial.ATCommand.Type.CMD;
import static me.legrange.swap.serial.ATCommand.Type.RO;
import static me.legrange.swap.serial.ATCommand.Type.RW;

/**
 * AT Command supported by the modem application. 
 * @author gideon
 */
public enum ATCommand {
    
    AT(CMD),
    ATZ(CMD),
    ATO(CMD),
    ATCH(RW, 2),
    ATSW(RW, 4),
    ATDA(RW, 2),
    ATAC(RW, 2),
    ATHV(RO),
    ATFV(RO);
    
    
    enum Type { CMD, RO, RW };

    private ATCommand(Type type, int len) {
        this.type = type;
        this.len = len;
    }
    
    private ATCommand(Type type) {
        this(type, 0);
    }

    private final Type type;
    private final int len;
}
