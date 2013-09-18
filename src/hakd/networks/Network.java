package hakd.networks;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import hakd.game.gameplay.Player;
import hakd.internet.Internet;
import hakd.networks.devices.Device;
import hakd.networks.devices.Dns;
import hakd.networks.devices.Router;
import hakd.networks.devices.Server;

import java.util.List;

public interface Network {
	/**
	 * removes a device from the network
	 */
	public void removeDevice(Router r, Device d);

	/**
	 * adds a device if the limit has not been reached
	 */
	public boolean addDevice(Router r, Device device);

	public int getLevel();

	public void setLevel(int level);

	public String getOwner();

	public void setOwner(String owner);

	public int getServerLimit();

	public void setServerLimit(int serverLimit);

	public int getDnsLimit();

	public void setDnsLimit(int dnsLimit);

	public Stance getStance();

	public void setStance(Stance stance);

	public IpRegion getRegion();

	public void setRegion(IpRegion ipRegion);

	public int getRouterLimit();

	public void setRouterLimit(int routerLimit);

	public NetworkType getType();

	public void setType(NetworkType type);

	public List<Device> getOtherDevices();

	public List<Server> getServers();

	public List<Dns> getDnss();

	public List<Router> getRouters();

	public Player getPlayer();

	public void setPlayer(Player player);

	public Internet getInternet();

	public void setInternet(Internet internet);

	public Model getModel();

	public void setModel(Model model);

	public Vector3 getPosition();

	public void setPosition(Vector3 position);

	public Model getSphere();

	public void setSphere(Model sphere);

	public ModelInstance getInstance();

	public void setInstance(ModelInstance sphereInstance);

	/**
	 * these define how to generate a network
	 */
	public enum NetworkType {
		PLAYER(), BUSINESS(), TEST(), ISP(), NPC(), BACKBONE(), EDUCATION(), BANK(), MILITARY(), GOVERNMENT(),
		RESEARCH();

		NetworkType() {
		}
	}

	public enum Stance {
		FRIENDLY(), NEUTRAL(), ENEMY();

		Stance() {
		}
	}

	public enum IpRegion {
		BUSINESS(1, 56), SA(57, 62), NA(63, 76), EUROPE(77, 91), ASIA(92, 114), AFRICA(115, 126), PRIVATE(128, 172),
		EDUCATION(173, 182), GOVERNMENT(214, 220), MILITARY(220, 255), none(1, 255);

		public int min; // min backbone ip range
		public int max; // max ip range

		IpRegion(int min, int max) {
			this.min = min;
			this.max = max;
		}
	}

	public enum Owner {
		COMPANY("Company"), TEST("Test"); // these will be replaced with better
		// ones

		public String company;

		Owner(String company) {
			this.company = company;
		}
	}
}
