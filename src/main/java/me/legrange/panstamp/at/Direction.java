package me.legrange.panstamp.at;

/**
 * Type of command: Read-Write, Read-Only, Write-Only 
 * @author gideon
 */
public enum Direction {
    
    READ_WRITE("Read-Write"), READ_ONLY("Read-Only"), WRITE_ONLY("Write-Only");
    
    private Direction(String desc) {
        this.description = desc;
    }
   
    private final String description;
    
}
