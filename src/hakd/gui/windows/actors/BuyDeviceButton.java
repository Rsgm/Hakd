package hakd.gui.windows.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import hakd.gui.Assets;
import hakd.gui.windows.newdevice.NewServerWindow;
import hakd.networks.devices.Device;
import hakd.networks.devices.parts.Cpu;
import hakd.networks.devices.parts.Gpu;
import hakd.networks.devices.parts.Memory;
import hakd.networks.devices.parts.Part.PartType;
import hakd.networks.devices.parts.Storage;

public final class BuyDeviceButton extends TextButton {
    private Cpu cpu;
    private Gpu gpu;
    private Memory memory;
    private Storage storage;

    private final Device device;
    private int level;

    private final NewServerWindow newServerWindow;

    /*
     * For ease, you can only buy whole servers with one part each. You can buy
     * more parts after that though.
     */

    public BuyDeviceButton(Device device, final int level, final NewServerWindow newServerWindow, Skin skin, final Cpu cpu, final Gpu gpu, final Memory memory, final Storage storage) {
        super("buy", skin);

        this.newServerWindow = newServerWindow;

        this.device = device;

        // switch (device.getType()) {
        // case DNS:
        // device = new Dns(device.getNetwork(), level, DeviceType.SERVER, 1,
        // 1, 1, 1);
        // break;
        // default:
        // device = new Server(device.getNetwork(), level, DeviceType.SERVER,
        // 1, 1, 1, 1);
        // break;
        // }

        if (cpu != null) {
            this.cpu = cpu;
        } else {
            this.cpu = new Cpu(level, device);
        }

        if (gpu != null) {
            this.gpu = gpu;
        } else {
            this.gpu = new Gpu(level, device);
        }

        if (memory != null) {
            this.memory = memory;
        } else {
            this.memory = new Memory(level, device);
        }

        if (storage != null) {
            this.storage = storage;
        } else {
            this.storage = new Storage(level, device);
        }

        addListener();
    }

    private void addListener() {
        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                device.setCpuSockets(1);
                device.setGpuSlots(1);
                device.setMemorySlots(1);
                device.setStorageSlots(1);

                cpu.setDevice(device);
                gpu.setDevice(device);
                memory.setDevice(device);
                storage.setDevice(device);

                device.addPart(PartType.CPU, cpu);
                device.addPart(PartType.GPU, gpu);
                device.addPart(PartType.MEMORY, memory);
                device.addPart(PartType.STORAGE, storage);
                device.setMasterStorage(storage);

                device.getNetwork().setLevel(0);
                device.setLevel(0);

                device.setTile(device.getTile());
                device.getTile().setRegion(Assets.nearestTextures.findRegion("s" + device.getLevel()));

                newServerWindow.close();
            }
        });
    }

    public Cpu getCpu() {
        return cpu;
    }

    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }

    public Gpu getGpu() {
        return gpu;
    }

    public void setGpu(Gpu gpu) {
        this.gpu = gpu;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Device getDevice() {
        return device;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
