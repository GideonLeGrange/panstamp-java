package me.legrange.panstamp.def;

/**
 * Enum used for defining data flow direction 
 * @author gideon
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
