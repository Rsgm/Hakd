package hakd.networks.devices.parts;

public final class Cpu extends Part {
    private int cores; // core modifier speed = speed (1.8*cores) in MHz, 3.5GHz -> 3500MHz
    int speed; // either MHz or MB/s(megabyte/s, not megabit/s) depending on the part cpu also has core modifier speed = speed (1.8*cores)

    public Cpu() {
        super();
        type = PartType.CPU;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}