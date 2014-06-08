package me.legrange.panstamp.device;

/**
 * A Panstamp device configuration 
 * @author gideon
 */
public class Device {
    
    public Device(Developer developer, int id, String name, String label) {
        this.id = id;
        this.developer = developer;
        this.name = name;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getProduct() {
        return product;
    }

    public boolean isPowerDownMode() {
        return powerDownMode;
    }

    @Override
    public String toString() {
        return "Device{" + "id=" + id + ", developer=" + developer + ", name=" + name + ", label=" + label + ", product=" + product + ", powerDownMode=" + powerDownMode + '}';
    }
    
    void setProduct(String product) {
        this.product = product;
    }

    void setPowerDownMode(Boolean pdm) {
        this.powerDownMode = pdm;
    }
    
    private final int id;
    private final Developer developer;
    private final String name;
    private final String label;
    private String product;
    private boolean powerDownMode;
}
