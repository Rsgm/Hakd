package hakd.gui.windows.actors;

import hakd.gui.windows.newdevice.NewServerWindow;
import hakd.networks.devices.Device;
import hakd.networks.devices.Device.DeviceType;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;
import hakd.networks.devices.Server;
import hakd.networks.devices.parts.Cpu;
import hakd.networks.devices.parts.Gpu;
import hakd.networks.devices.parts.Memory;
import hakd.networks.devices.parts.Part.PartType;
import hakd.networks.devices.parts.Storage;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class BuyServerButton extends TextButton {
    private Cpu cpu;
    private Gpu gpu;
    private Memory memory;
    private Storage storage;

    private Device currentDevice;
    private int level;

    private final NewServerWindow newServerWindow;
    private final Device d;

    /*
     * For ease, you can only buy whole servers with one part each. You can buy
     * more parts after that though.
     */

    public BuyServerButton(Device currentDevice, final int level,
	    final NewServerWindow newServerWindow, Skin skin, final Cpu cpu,
	    final Gpu gpu, final Memory memory, final Storage storage) {
	super("buy", skin);

	this.newServerWindow = newServerWindow;

	this.currentDevice = currentDevice;

	switch (currentDevice.getType()) {
	case DNS:
	    d = new Dns(currentDevice.getNetwork(), level, DeviceType.SERVER,
		    1, 1, 1, 1);
	    break;
	case ROUTER:
	    d = new Router(currentDevice.getNetwork(), level,
		    DeviceType.SERVER, 1, 1, 1, 1);
	    break;
	default:
	    d = new Server(currentDevice.getNetwork(), level,
		    DeviceType.SERVER, 1, 1, 1, 1);
	    break;
	}

	if (cpu != null) {
	    this.cpu = cpu;
	} else {
	    this.cpu = new Cpu(level, d);
	}

	if (gpu != null) {
	    this.gpu = gpu;
	} else {
	    this.gpu = new Gpu(level, d);
	}

	if (memory != null) {
	    this.memory = memory;
	} else {
	    this.memory = new Memory(level, d);
	}

	if (storage != null) {
	    this.storage = storage;
	} else {
	    this.storage = new Storage(level, d);
	}

	addListener();
    }

    private void addListener() {
	addListener(new InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y,
		    int pointer, int button) {
		return true;
	    }

	    @Override
	    public void touchUp(InputEvent event, float x, float y,
		    int pointer, int button) {

		cpu.setDevice(d);
		gpu.setDevice(d);
		memory.setDevice(d);
		storage.setDevice(d);

		d.addPart(PartType.CPU, cpu);
		d.addPart(PartType.GPU, gpu);
		d.addPart(PartType.MEMORY, memory);
		d.addPart(PartType.STORAGE, storage);
		d.setMasterStorage(storage);

		d.getNetwork().removeDevice(currentDevice);
		d.getNetwork().addDevice(d);
		d.getNetwork().setLevel(0);

		newServerWindow.close();

		super.touchUp(event, x, y, pointer, button);
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
	return currentDevice;
    }

    public void setDevice(Server server) {
	this.currentDevice = server;
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }
}
