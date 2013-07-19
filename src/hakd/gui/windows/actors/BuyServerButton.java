package hakd.gui.windows.actors;

import hakd.gui.windows.NewServerWindow;
import hakd.networks.devices.Device;
import hakd.networks.devices.Device.DeviceType;
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

    private Server currentServer;
    private int level;

    private final NewServerWindow newServerWindow;
    private final Server s;

    /*
     * For ease, you can only buy whole servers with one part each. You can buy
     * more parts after that though.
     */

    public BuyServerButton(Server currentServer, final int level,
	    final NewServerWindow newServerWindow, Skin skin, final Cpu cpu,
	    final Gpu gpu, final Memory memory, final Storage storage) {
	super("buy", skin);

	this.newServerWindow = newServerWindow;

	this.currentServer = currentServer;
	s = new Server(currentServer.getNetwork(), level, DeviceType.SERVER, 1,
		1, 1, 1);

	if (cpu != null) {
	    this.cpu = cpu;
	} else {
	    this.cpu = new Cpu(level, s);
	}

	if (gpu != null) {
	    this.gpu = gpu;
	} else {
	    this.gpu = new Gpu(level, s);
	}

	if (memory != null) {
	    this.memory = memory;
	} else {
	    this.memory = new Memory(level, s);
	}

	if (storage != null) {
	    this.storage = storage;
	} else {
	    this.storage = new Storage(level, s);
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

		cpu.setDevice(s);
		gpu.setDevice(s);
		memory.setDevice(s);
		storage.setDevice(s);

		s.addPart(PartType.CPU, cpu);
		s.addPart(PartType.GPU, gpu);
		s.addPart(PartType.MEMORY, memory);
		s.addPart(PartType.STORAGE, storage);
		s.setMasterStorage(storage);

		s.getNetwork().removeDevice(currentServer);
		s.getNetwork().addDevice(s);
		s.getNetwork().setLevel(0);

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
	return currentServer;
    }

    public void setDevice(Server server) {
	this.currentServer = server;
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }
}
