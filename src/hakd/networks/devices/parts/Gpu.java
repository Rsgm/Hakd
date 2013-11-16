package hakd.networks.devices.parts;

public final class Gpu extends Part {
    int speed; // either MHz or MB/s(megabyte/s, not megabit/s) depending on the part cpu also has core modifier speed = speed (1.8*cores)

    public Gpu() {
        super();
        type = PartType.GPU;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
