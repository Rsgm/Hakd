package hakd.networks.devices.parts;

public class PartFactory {

    /**
     * Most basic constructor.
     *
     * @param type - The type of part to create.
     */
    public static Part createPart(Part.PartType type) {
        switch (type) {
            default:
                return new Part();
            case CPU:
                return new Cpu();
            case GPU:
                return new Gpu();
            case MEMORY:
                return new Memory();
            case STORAGE:
                return new Storage();
        }
    }

    /**
     * Create a CPU and give it properties.
     *
     * @param level - The level of part to create which tells how to generate the part.
     */
    public static Cpu createCpu(int level) {
        Cpu c = (Cpu) createPart(Part.PartType.CPU);
        switch (level) {
            case 0:
                c.setSpeed(((int) (Math.random() * 5 + 1)) * 105 + 100);
                c.setCores(1);
                break;
            default:
                c.setSpeed((level + 1) * 625 + (((int) (Math.random() * 400 + 1)) * 5 - 1000));
                if (level >= 4) {
                    c.setCores((int) Math.pow(2, (level - 3) - ((int) (Math.random() * 2))));
                } else {
                    c.setCores(1);
                }
                break;
        }
        return c;
    }

    /**
     * Create a GPU and give it properties.
     *
     * @param level - The level of part to create which tells how to generate the part.
     */
    public static Gpu createGpu(int level) {
        Gpu g = (Gpu) createPart(Part.PartType.GPU);

        switch (level) {
            case 0:
                g.setSpeed((int) (Math.random() * 200 + 100));
                break;
            default:
                g.setSpeed((level + 1) * 150 + (int) (Math.random() * 400 - 200));
                break;
        }
        return g;
    }

    /**
     * Create a Memory and give it properties.
     *
     * @param level - The level of part to create which tells how to generate the part.
     */
    public static Memory createMemory(int level) {
        Memory m = (Memory) createPart(Part.PartType.MEMORY);

        switch (level) {
            case 0:
                m.setCapacity(1 + (int) (Math.random() * 3 - 3));
                break;
            default:
                m.setCapacity((int) Math.pow(2, (level + 1) / 2 + ((int) (Math.random() * 3 + 1) - 2)));
                break;
        }
        return m;
    }

    /**
     * Create a Storage and give it properties.
     *
     * @param level - The level of part to create which tells how to generate the part.
     */
    public static Storage createStorage(int level, boolean isSSD) {
        Storage s = (Storage) createPart(Part.PartType.STORAGE);

        switch (level) {
            case 0:
                s.setSpeed((level + 1) * 30 + (int) (Math.random() * 30));
                s.setCapacity((int) Math.pow(2, (level + 4) + (int) (Math.random() * 3 - 1)));
            default:
                s.setSpeed((level + 1) * 30 + ((int) (Math.random() * 60 - 30)));
                s.setCapacity((int) Math.pow(2, (level + 4) + (int) (Math.random() * 3 + 1) - 2)); // start at 16 GB at level 1 and make sure the OS takes up 15 GB
                if (1 == (int) ((Math.random() * 30) + 1)) {
                    s.setSsd(true);
                    s.setSpeed(2);
                    s.setCapacity(2);
                }
        }
        return s;
    }
}
