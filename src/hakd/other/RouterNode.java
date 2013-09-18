package hakd.other;

import ai.pathfinder.Node;
import hakd.networks.devices.Router;

public class RouterNode extends Node {
	private final Router router;

	public RouterNode(Router router) {
		super();
		this.router = router;
	}

	public Router getRouter() {
		return router;
	}

}
