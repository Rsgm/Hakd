package hakd.networks.devices.parts;

public class Storage extends Part {
    private boolean ssd; // doubles the speed
    private int capacity; // in GB
    private int speed; // either MHz or MB/s(megabyte/s, not megabit/s) depending on the part cpu also has core modifier speed = speed (1.8*cores)

    public Storage() {
        super();
        type = PartType.STORAGE;
    }

    public boolean isSsd() {
        return ssd;
    }

    public void setSsd(boolean ssd) {
        this.ssd = ssd;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
