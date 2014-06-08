package me.legrange.panstamp.device;

/**
 * A panstamp developer
 * @author gideon
 */
public class Developer {

    public Developer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Developer{" + "id=" + id + ", name=" + name + '}';
    }
    
    private final int id;
    private final String name;
    
}
