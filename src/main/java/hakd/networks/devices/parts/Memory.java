package hakd.networks.devices.parts;

public class Memory extends Part {
    private int capacity;

    public Memory() {
        super();
        type = PartType.MEMORY;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
