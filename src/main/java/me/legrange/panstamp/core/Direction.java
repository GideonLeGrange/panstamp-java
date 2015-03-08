package me.legrange.panstamp.core;

/**
 * Data flow direction defined.  
 *
 * @since 1.0
 * @author Gideon le Grange https://github.com/GideonLeGrange *
 */
public enum Direction {
    IN("inp"), OUT("out");

    static Direction forTag(String tag) {
        for (Direction dir : Direction.values()) {
            if (dir.tag.equals(tag)) {
                return dir;
            }
        }
        return null;
    }

    private Direction(String tag) {
        this.tag = tag;
    }
    private final String tag;
    
}
